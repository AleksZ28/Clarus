package com.azurowski.clarus

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object WeatherApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResponse {
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,snow_depth,surface_pressure,cloud_cover,visibility,wind_speed_10m,wind_direction_10m&current=apparent_temperature,is_day,precipitation,rain,showers,snowfall,cloud_cover,surface_pressure,wind_speed_10m,wind_direction_10m,relative_humidity_2m&timezone=auto"
        return client.get(url).body()
    }
}

@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather
)

@Serializable
data class CurrentWeather(
    val apparent_temperature: Double,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val cloud_cover: Int,
    val surface_pressure: Double,
    val wind_speed_10m: Double,
    val wind_direction_10m: Double,
    val relative_humidity_2m: Int,
    val is_day: Int,
    val time: String
)
