package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.CurrentWeather
import com.azurowski.clarus.model.HourlyWeatherToProcess

fun mapCurrentWeather(current: CurrentWeather): String{
    val rain = current.rain
    val showers = current.showers
    val snowfall = current.snowfall
    val cloud_cover = current.cloud_cover
    val is_day = current.is_day

    return when {
        snowfall > 0.1 -> "snowy"
        rain > 2.5 -> "heavy_rain"
        rain > 0.75 -> "mid_rain"
        rain > 0.3 -> "low_rain"
        showers > 0.3 -> "low_rain"
        cloud_cover >= 75 -> "cloudy"
        is_day == 0 -> "night"
        cloud_cover >= 50 -> "75_cloudy"
        cloud_cover >= 25 -> "50_cloudy"
        cloud_cover >= 5 -> "25_cloudy"
        else -> "sunny"
    }
}

fun mapHourlyWeather(hourly: HourlyWeatherToProcess): String{
    val precipitation_probability = hourly.precipitation_probability
    val rain = hourly.rain
    val showers = hourly.showers
    val snowfall = hourly.snowfall
    val cloud_cover = hourly.cloud_cover
    val hour = hourly.time.substring(11,13).toInt()

    return when {
        snowfall > 0.1 && precipitation_probability > 40 -> "snowy"
        precipitation_probability > 40 && rain > 2.5 -> "heavy_rain"
        precipitation_probability > 40 && rain > 0.75 -> "mid_rain"
        precipitation_probability > 40 && rain > 0.3 -> "low_rain"
        precipitation_probability > 40 && showers > 0.3 -> "low_rain"
        precipitation_probability in 30..<40 -> "low_rain"
        cloud_cover >= 75 -> "cloudy"
        hour in 22..24 || hour in 0..5 -> "night"
        cloud_cover >= 50 -> "75_cloudy"
        cloud_cover >= 25 -> "50_cloudy"
        cloud_cover >= 5 -> "25_cloudy"
        else -> "sunny"
    }
}