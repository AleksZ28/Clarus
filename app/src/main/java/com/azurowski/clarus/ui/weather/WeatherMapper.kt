package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.CurrentWeather

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
        cloud_cover >= 50 -> "75_cloudy"
        cloud_cover >= 25 -> "50_cloudy"
        cloud_cover >= 5 -> "25_cloudy"
        is_day == 0 -> "clear_night"
        else -> "sunny"
    }
}