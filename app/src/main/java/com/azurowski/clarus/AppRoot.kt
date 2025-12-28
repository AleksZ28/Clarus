package com.azurowski.clarus

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azurowski.clarus.ui.screens.HomeScreen
import com.azurowski.clarus.ui.screens.weather.FullWeatherScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("weatherSummary") {
            FullWeatherScreen(navController)
        }

    }
}