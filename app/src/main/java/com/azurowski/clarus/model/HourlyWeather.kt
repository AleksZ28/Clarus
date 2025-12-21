package com.azurowski.clarus.model

data class HourlyWeatherToProcess(
    val time: String,
    val is_day: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val apparent_temperature: Double,
    val precipitation_probability: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val snow_depth: Double,
    val surface_pressure: Double,
    val cloud_cover: Int,
    val visibility: Double,
    val wind_speed_10m: Double,
    val wind_direction_10m: Double,
    val isCurrentDayNight: Boolean? = null
)

data class HourlyWeather(
    val hour: String,
    val is_day: Int,
    val weatherType: String,
    val precipitation: Double,
    val precipitationProbability: Int,
    val temperature: Int,
)