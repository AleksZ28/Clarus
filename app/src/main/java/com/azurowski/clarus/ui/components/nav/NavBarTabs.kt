package com.azurowski.clarus.ui.components.nav

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavBarTabs(
    tabs: List<NavBarTab>,
    selectedTab: Int,
    onTabSelected: (NavBarTab) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(36.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 27.dp)
    ) {
        for (tab in tabs) {
            val alpha by animateFloatAsState(
                targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .35f,
                label = "alpha"
            )
            val scale by animateFloatAsState(
                targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .98f,
                visibilityThreshold = .000001f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                ),
                label = "scale"
            )
            Column(
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onTabSelected(tab)
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(imageVector = tab.icon, contentDescription = "tab ${tab.title}")
                Text(
                    text = tab.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}