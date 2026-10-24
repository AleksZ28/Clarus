package com.azurowski.clarus.ui.screens.moon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.azurowski.clarus.ui.theme.MoonPhasesBackgrounds
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
fun MoonScreen(
    hazeState: HazeState = remember { HazeState() }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .background(brush = MoonPhasesBackgrounds.NewMoonBg)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Moon Screen")
    }
}