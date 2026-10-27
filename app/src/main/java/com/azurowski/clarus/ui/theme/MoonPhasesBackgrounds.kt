package com.azurowski.clarus.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object MoonPhasesBackgrounds {
    val NewMoonBg = Brush.linearGradient(
        colors = listOf(
            MoonPurple,
            NewMoon
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val FullMoonBg = Brush.linearGradient(
        colors = listOf(
            MoonPurple,
            FullMoon
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val CrescentBg = Brush.linearGradient(
        colors = listOf(
            MoonPurple,
            FullMoon.copy(alpha = 0.25f)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val QuaterBg = Brush.linearGradient(
        colors = listOf(
            MoonPurple,
            FullMoon.copy(alpha = 0.5f)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val GibbousBg = Brush.linearGradient(
        colors = listOf(
            MoonPurple,
            FullMoon.copy(alpha = 0.75f)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )


}