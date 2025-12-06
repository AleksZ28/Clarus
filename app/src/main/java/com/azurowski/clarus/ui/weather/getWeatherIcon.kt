package com.azurowski.clarus.ui.weather

import androidx.compose.runtime.Composable
import com.azurowski.clarus.R

@Composable
fun getWeatherIcon(weatherType: String): Int {
    return when (weatherType) {
        "sunny" -> R.drawable.sun
        "25_cloudy" -> R.drawable._25_cloudly
        "50_cloudy" -> R.drawable._50_cloudly
        "75_cloudy" -> R.drawable._75_cloudly
        "cloudy" -> R.drawable.cloudly
        "low_rain" -> R.drawable.low_rain
        "mid_rain" -> R.drawable.mid_rain
        "heavy_rain" -> R.drawable.heavy_rain
        "snowy" -> R.drawable.snow
        "clear_night" -> R.drawable.moon
        else -> R.drawable.ic_launcher_foreground
    }
}