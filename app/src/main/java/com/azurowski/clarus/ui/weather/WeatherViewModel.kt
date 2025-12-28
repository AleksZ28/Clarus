package com.azurowski.clarus.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azurowski.clarus.WeatherApi
import com.azurowski.clarus.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(
    private val api: WeatherApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(api) as T
    }
}

class WeatherViewModel(private val api: WeatherApi) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var isDataLoaded = false

    fun fetchWeather(latitude: Double, longitude: Double, forceRefresh: Boolean = false) {
        if (isDataLoaded && !forceRefresh) return

        viewModelScope.launch {
            try {
                _uiState.value = WeatherUiState.Loading
                val response = api.getWeather(latitude,longitude)
                _uiState.value = WeatherUiState.Success(response)
                isDataLoaded = true
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
