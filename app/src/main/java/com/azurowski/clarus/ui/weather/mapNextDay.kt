package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.HourlyWeatherFromAPI
import com.azurowski.clarus.model.HourlyWeatherToProcess
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.kastro.sol.SolarEventSequence
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun mapNextDay(hourlyWeatherFromApi: HourlyWeatherFromAPI, latitude: Double, longitude: Double): List<HourlyWeatherToProcess> {

    var isCurrentDayNight = false
    var dayHoursCount = 0
    var dayEnd = false

    val nextSunrise = SolarEventSequence(
        start = Clock.System.now(),
        latitude = latitude,
        longitude = longitude,
        requestedSolarEvents = listOf(SolarEvent.Sunrise)
    ).first()

    val sunriseLocal = ZonedDateTime.ofInstant(
        java.time.Instant.parse(nextSunrise.time.toString()),
        ZoneId.systemDefault()
    )

    val now = ZonedDateTime.now(ZoneId.systemDefault())
    val currentDayOfMonth = LocalDateTime.now().dayOfMonth

    val sunriseDayOfMonth = sunriseLocal.dayOfMonth

    if(now.isBefore(sunriseLocal) && currentDayOfMonth == sunriseDayOfMonth){
        isCurrentDayNight = true
    }

    val offset = if (isCurrentDayNight) 0 else 24

    hourlyWeatherFromApi.is_day.forEachIndexed { i, isDay ->
        if (isDay == 1 && !dayEnd && i > offset) {
            dayHoursCount++
        } else if (dayHoursCount > 0){
            dayEnd = true
        }
    }

    val nextDayHourlyWeather = hourlyWeatherFromApi.time.mapIndexedNotNull { i, _ ->
        if(i >= offset && hourlyWeatherFromApi.is_day[i] == 1){
            HourlyWeatherToProcess(
                time = hourlyWeatherFromApi.time[i],
                is_day = hourlyWeatherFromApi.is_day[i],
                temperature_2m = hourlyWeatherFromApi.temperature_2m[i],
                relative_humidity_2m = hourlyWeatherFromApi.relative_humidity_2m[i],
                apparent_temperature = hourlyWeatherFromApi.apparent_temperature[i],
                precipitation_probability = hourlyWeatherFromApi.precipitation_probability[i],
                precipitation = hourlyWeatherFromApi.precipitation[i],
                rain = hourlyWeatherFromApi.rain[i],
                showers = hourlyWeatherFromApi.showers[i],
                snowfall = hourlyWeatherFromApi.snowfall[i],
                snow_depth = hourlyWeatherFromApi.snow_depth[i],
                surface_pressure = hourlyWeatherFromApi.surface_pressure[i],
                cloud_cover = hourlyWeatherFromApi.cloud_cover[i],
                visibility = hourlyWeatherFromApi.visibility[i],
                wind_speed_10m = hourlyWeatherFromApi.wind_speed_10m[i],
                wind_direction_10m = hourlyWeatherFromApi.wind_direction_10m[i],
                isCurrentDayNight = isCurrentDayNight
            )
        } else null
    }.take(dayHoursCount)

    return nextDayHourlyWeather
}