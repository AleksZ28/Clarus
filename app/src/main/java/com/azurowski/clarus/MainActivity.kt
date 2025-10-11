package com.azurowski.clarus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.azurowski.clarus.ui.theme.ClarusTheme
import com.azurowski.clarus.WeatherApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val city = "Kraków"
        lifecycleScope.launch {
            try {
                val weather = WeatherApi.getCurrentWeather(50.06, 19.94)
                Log.d("WeatherTest", "Miasto: ${city}")
                Log.d("WeatherTest", "Temperatura: ${weather.current_weather.temperature} °C")
                Log.d("WeatherTest", "Wiatr: ${weather.current_weather.windspeed} m/s")
            } catch (e: Exception) {
                Log.e("WeatherTest", "Błąd pobierania pogody: ${e.message}")
            }
        }

        enableEdgeToEdge()
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ładowanie pogody...")
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherApp()
}