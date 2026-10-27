package com.azurowski.clarus.ui.components.nav

import android.R
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.google.android.gms.location.LocationServices
import dev.jamesyox.kastro.luna.calculateLunarState
import kotlin.time.Clock
import com.azurowski.clarus.ui.moonUtils.getMoonPhase

@SuppressLint("MissingPermission")
@Composable
fun NavBarTabs(
    tabs: List<NavBarTab>,
    selectedTab: Int,
    onTabSelected: (NavBarTab) -> Unit,
) {
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = selectedTab.toFloat(),
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = tabs[selectedTab].color,
        label = "animatedColor",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow
        )
    )
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)

        ) {
            val tabWidth = size.width / tabs.size
            drawCircle(
                color = animatedColor.copy(alpha = .6f),
                radius = size.height / 2,
                center = Offset(
                    x = (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                    y = size.height / 2
                )
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        ) {
            val path = Path().apply {
                addRoundRect(RoundRect(size.toRect(), CornerRadius(size.height)))
            }
            val length = PathMeasure().apply { setPath(path, false) }.length

            val tabWidth = size.width / tabs.size
            drawPath(
                path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        animatedColor.copy(alpha = 0f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 0f),
                    ),
                    startX = tabWidth * animatedSelectedTabIndex,
                    endX = tabWidth * (animatedSelectedTabIndex + 1),
                ),
                style = Stroke(
                    width = 6f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(length / 2, length)
                    )
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 27.dp)
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
                        dampingRatio = Spring.DampingRatioLowBouncy,
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
                    val context = LocalContext.current
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    var longitude by rememberSaveable { mutableStateOf<Double>(0.0) }
                    var latitude by rememberSaveable { mutableStateOf<Double>(0.0) }
                    RequestLocationPermissions (
                        onPermissionGranted = {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                location?.let {
                                    latitude = it.latitude
                                    longitude = it.longitude                }
                            }
                        }
                    )
                    val moonState = Clock.System.now().calculateLunarState(
                        longitude = longitude,
                        latitude = latitude

                    )
                    Icon(imageVector = tab.icon, contentDescription = "tab ${tab.title}", tint = if (selectedTab == 1 && getMoonPhase(moonState.illumination.phase) == "Nów") Color.White else MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = tab.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedTab == 1 && getMoonPhase(moonState.illumination.phase) == "Nów") Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

}