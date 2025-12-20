package com.azurowski.clarus.ui.weather

import com.azurowski.clarus.model.HourlyWeatherToProcess
import com.azurowski.clarus.model.NightWeather
import kotlin.math.round

fun parseNightWeather(hourlyWeatherToProcess: List<HourlyWeatherToProcess>): NightWeather {
    val temperatures = hourlyWeatherToProcess.map { it.apparent_temperature }.sorted()
    val size = temperatures.size
    val medianTemperature = if (size % 2 == 1) { round(temperatures[size / 2]).toInt() } else { round((temperatures[size / 2 - 1] + temperatures[size / 2]) / 2).toInt() }
    val minTemperature = round(temperatures[0]).toInt()
    val maxTemperature = round(temperatures[size-1]).toInt()

    val weatherTypes = hourlyWeatherToProcess.map { it ->
        mapHourlyWeather(it)
    }.distinct()

    return NightWeather(
        minTemperature,
        maxTemperature,
        medianTemperature,
        weatherTypes
    )
}