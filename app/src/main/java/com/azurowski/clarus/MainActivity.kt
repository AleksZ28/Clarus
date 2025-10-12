package com.azurowski.clarus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.azurowski.clarus.ui.theme.ClarusTheme
import com.azurowski.clarus.WeatherApi
import com.azurowski.clarus.ui.weather.WeatherUiState
import com.azurowski.clarus.ui.weather.WeatherViewModel
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
fun WeatherApp(viewModel: WeatherViewModel = remember { WeatherViewModel(WeatherApi) }) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        when (state) {
            is WeatherUiState.Idle -> Text("Nie pobrano pogody")
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> {
                val data = (state as WeatherUiState.Success).data
                Text("Dane: $data")
            }

            is WeatherUiState.Error -> Text("Błąd: ${(state as WeatherUiState.Error).message}")
        }

        Button(onClick = { viewModel.fetchWeather(50.06, 19.94) }) {
            Text("Pobierz pogodę")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherApp()
}