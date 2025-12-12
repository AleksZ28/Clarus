package com.azurowski.clarus.ui.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import com.azurowski.clarus.CurrentWeather
import com.azurowski.clarus.ui.theme.WeatherBackgrounds

@Composable
fun mapWeatherBackground(currentWeather: CurrentWeather): Brush{
    return when{
        currentWeather.is_day == 0 -> WeatherBackgrounds.Night
        currentWeather.snowfall > 0 -> WeatherBackgrounds.Snowy
        currentWeather.showers > 0 || currentWeather.rain > 0 -> WeatherBackgrounds.Rainy
        currentWeather.cloud_cover < 25 -> WeatherBackgrounds.Sunny
        currentWeather.cloud_cover >= 25 && currentWeather.cloud_cover < 75 -> WeatherBackgrounds.PartlyCloudy
        else -> WeatherBackgrounds.Cloudy
    }
}