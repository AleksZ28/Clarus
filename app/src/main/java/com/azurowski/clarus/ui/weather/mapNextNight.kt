package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.HourlyWeatherFromAPI
import com.azurowski.clarus.model.HourlyWeatherToProcess

fun mapNextNight(hourlyWeatherFromApi: HourlyWeatherFromAPI): List<HourlyWeatherToProcess> {

    var nightHoursCount = 0
    var nightEnd = false

    hourlyWeatherFromApi.is_day.forEachIndexed { i, isDay ->
        if (isDay == 0 && !nightEnd && i > 12) {
            nightHoursCount++
        } else if (nightHoursCount > 0){
            nightEnd = true
        }
    }

    val nightHourlyWeather = hourlyWeatherFromApi.time.mapIndexedNotNull { i, _ ->
        if(i >= 12 && hourlyWeatherFromApi.is_day[i] == 0){
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
                wind_direction_10m = hourlyWeatherFromApi.wind_direction_10m[i]
            )
        } else null
    }.take(nightHoursCount)

    return nightHourlyWeather
}
