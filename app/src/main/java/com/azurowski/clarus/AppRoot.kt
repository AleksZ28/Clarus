package com.azurowski.clarus

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azurowski.clarus.ui.screens.HomeScreen
import com.azurowski.clarus.ui.screens.location.LocationChangeScreen
import com.azurowski.clarus.ui.screens.weather.FullWeatherScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }
            )
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }
            )
        }
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("weatherSummary") {
            FullWeatherScreen(navController)
        }
        composable("locationChange") {
            LocationChangeScreen(navController)
        }
    }
}