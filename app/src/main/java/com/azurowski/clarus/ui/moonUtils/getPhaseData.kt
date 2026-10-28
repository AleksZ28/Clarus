package com.azurowski.clarus.ui.moonUtils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.WeatherIcons
import compose.icons.weathericons.*
import dev.jamesyox.kastro.luna.LunarEvent

@Composable
fun getPhaseData(phase: LunarEvent.PhaseEvent): Pair<String, ImageVector> {
    val isDark = isSystemInDarkTheme()
    if(isDark){
        return when(phase) {
            is LunarEvent.PhaseEvent.NewMoon -> Pair("Nów", WeatherIcons.MoonAltFull)
            is LunarEvent.PhaseEvent.FirstQuarter -> Pair("Pierwsza kwadra", WeatherIcons.MoonAltThirdQuarter)
            is LunarEvent.PhaseEvent.FullMoon -> Pair("Pełnia",WeatherIcons.MoonAltNew)
            is LunarEvent.PhaseEvent.LastQuarter -> Pair("Ostatnia kwadra", WeatherIcons.MoonAltFirstQuarter)
            else -> Pair("Inny event?",WeatherIcons.LunarEclipse)
        }
    }else{
        return when(phase) {
            is LunarEvent.PhaseEvent.NewMoon -> Pair("Nów", WeatherIcons.MoonAltNew)
            is LunarEvent.PhaseEvent.FirstQuarter -> Pair("Pierwsza kwadra", WeatherIcons.MoonAltFirstQuarter)
            is LunarEvent.PhaseEvent.FullMoon -> Pair("Pełnia",WeatherIcons.MoonAltFull)
            is LunarEvent.PhaseEvent.LastQuarter -> Pair("Ostatnia kwadra", WeatherIcons.MoonAltThirdQuarter)
            else -> Pair("Inny event?",WeatherIcons.LunarEclipse)
        }
    }

}