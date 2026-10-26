package com.azurowski.clarus.ui.moonUtils

import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.WeatherIcons
import compose.icons.weathericons.*

@Composable
fun getMoonIcon(phaseAngle: Double): ImageVector{
    return when(phaseAngle){
        in 0.0..6.0, in 354.0..360.0 -> WeatherIcons.MoonNew
        in 6.0..84.0 -> WeatherIcons.MoonWaxingCrescent3
        in 84.0..96.0 -> WeatherIcons.MoonFirstQuarter
        in 96.0..174.0 -> WeatherIcons.MoonAltWaxingGibbous3
        in 174.0..186.0 -> WeatherIcons.MoonFull
        in 186.0..264.0 -> WeatherIcons.MoonWaningGibbous3
        in 264.0..276.0 -> WeatherIcons.MoonThirdQuarter
        else -> WeatherIcons.MoonWaningCrescent3
    }
}