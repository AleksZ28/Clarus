package com.azurowski.clarus.ui.moonUtils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import com.azurowski.clarus.ui.theme.MoonPhasesBackgrounds

@Composable
fun getMoonBackground(phaseAngle: Double): Brush{
    return when(phaseAngle){
        in 0.0..6.0, in 354.0..360.0 -> MoonPhasesBackgrounds.NewMoonBg
        in 6.0..84.0 -> MoonPhasesBackgrounds.CrescentBg
        in 84.0..96.0 -> MoonPhasesBackgrounds.QuaterBg
        in 96.0..174.0 -> MoonPhasesBackgrounds.GibbousBg
        in 174.0..186.0 -> MoonPhasesBackgrounds.FullMoonBg
        in 186.0..264.0 -> MoonPhasesBackgrounds.GibbousBg
        in 264.0..276.0 -> MoonPhasesBackgrounds.QuaterBg
        else -> MoonPhasesBackgrounds.CrescentBg
    }
}