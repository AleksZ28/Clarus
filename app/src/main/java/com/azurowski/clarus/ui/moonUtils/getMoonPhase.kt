package com.azurowski.clarus.ui.moonUtils

import androidx.compose.runtime.Composable

@Composable
fun getMoonPhase(phaseAngle:Double):String{
    return when(phaseAngle){
        in 0.0..6.0, in 354.0..360.0 -> "Nów"
        in 6.0..84.0 -> "Przybywający sierp"
        in 84.0..96.0 -> "Pierwsza kwadra"
        in 96.0..174.0 -> "Przybywajjący garb"
        in 174.0..186.0 -> "Pełnia"
        in 186.0..264.0 -> "Ubywający garb"
        in 264.0..276.0 -> "Ostatnia kwadra"
        else -> "Ubywający sierp"
    }
}