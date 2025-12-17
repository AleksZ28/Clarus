package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.HourlyWeatherFromAPI
import com.azurowski.clarus.model.HourlyWeather
import com.azurowski.clarus.model.HourlyWeatherToProcess
import java.util.Calendar

fun mapNext24Hours(hourlyWeatherFromApi: HourlyWeatherFromAPI): List<HourlyWeather> {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val hourlyWeather = hourlyWeatherFromApi.time.mapIndexedNotNull { i, _ ->

        if(i >= currentHour){
            val hourlyData = HourlyWeatherToProcess(
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

            HourlyWeather(
                hour = hourlyData.time.substring(11,13),
                is_day = hourlyData.is_day,
                precipitationProbability = hourlyData.precipitation_probability,
                precipitation = hourlyData.precipitation,
                weatherType = mapHourlyWeather(hourlyData)
            )
        } else null
    }.take(24)

    return hourlyWeather
}
