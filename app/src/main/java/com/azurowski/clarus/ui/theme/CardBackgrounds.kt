package com.azurowski.clarus.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object CardBackgrounds {
    val Night = Brush.linearGradient(
        colors = listOf(
            Color(0xFF855CFF),
            Color(0xFFA2ACCC)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )

    val Day = Brush.linearGradient(
        colors = listOf(
            Color(0xFF7FCCFF),
            Color(0xFFCDD5DB)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY)
    )
}