package com.azurowski.clarus.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object WeatherBackgrounds {
    val Sunny = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF9D00),
            Color(0xFFB1EEFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val PartlyCloudy = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFDB65),
            Color(0xFF9DD6FF),
            Color(0xFFFFFFFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val Cloudy = Brush.linearGradient(
        colors = listOf(
            Color(0xFF9CB2C8),
            Color(0xFFD4D4D4)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val Rainy = Brush.linearGradient(
        colors = listOf(
            Color(0xFF578089),
            Color(0xFFCECECE)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val Snowy = Brush.linearGradient(
        colors = listOf(
            Color(0xFF9AD8E5),
            Color(0xFFE9E9E9)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val Night = Brush.linearGradient(
        colors = listOf(
            Color(0xFF7312F0),
            Color(0xFFBCD5D9)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )
}