package com.azurowski.clarus.model

data class WeatherSummary (
    val minTemperature: Int,
    val maxTemperature: Int,
    val medianTemperature: Int,
    val weatherTypes: List<String>
)