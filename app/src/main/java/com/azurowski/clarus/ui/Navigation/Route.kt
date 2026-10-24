package com.azurowski.clarus.ui.Navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object Home: Route, NavKey
    @Serializable
    data class LocationChange(val longitude: Double?,val latitude: Double?): Route, NavKey
    @Serializable
    data object FullWeather: Route, NavKey
    @Serializable
    data object Moon: Route, NavKey
}