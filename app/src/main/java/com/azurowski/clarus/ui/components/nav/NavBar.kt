package com.azurowski.clarus.ui.components.nav

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

sealed class NavBarTab(val title: String, val icon: ImageVector) {
    data object Weather : NavBarTab(
        title = "Prognoza",
        icon = Icons.Default.WbSunny
    )
    data object Moon : NavBarTab(
        title = "Księżyc",
        icon = Icons.Default.NightsStay
    )
}

val tabs = listOf(
    NavBarTab.Weather,
    NavBarTab.Moon,
)

@Composable
fun NavBar(modifier: Modifier, hazeState: HazeState) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val hazeStyle = HazeStyle(
        tint = HazeTint(Color.White.copy(alpha = 0.7f)),
        blurRadius = 8.dp,
    )

    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(vertical = 12.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(100))
            .hazeEffect(state = hazeState, style = hazeStyle)
            .border(
                width = Dp.Hairline,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = .6f),
                        Color.Black.copy(alpha = .2f),
                    ),
                ),
                shape = CircleShape
            )
            .dropShadow(
                shape = CircleShape,
                block = {
                    radius = 5f
                    alpha = 0.1f
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        NavBarTabs(
            tabs,
            selectedTab = selectedTabIndex,
            onTabSelected = {
                selectedTabIndex = tabs.indexOf(it)
            }
        )
    }
}