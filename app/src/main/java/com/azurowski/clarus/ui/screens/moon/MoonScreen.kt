package com.azurowski.clarus.ui.screens.moon

import android.R
import android.R.attr.radius
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azurowski.clarus.ui.moonUtils.getMoonIcon
import com.azurowski.clarus.ui.moonUtils.getMoonPhase
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.FullMoon
import com.azurowski.clarus.ui.theme.MoonPhasesBackgrounds
import com.google.android.gms.location.LocationServices
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.luna.LunarHorizonEventSequence
import dev.jamesyox.kastro.luna.LunarPhase
import dev.jamesyox.kastro.luna.LunarPosition
import dev.jamesyox.kastro.luna.LunarState
import dev.jamesyox.kastro.luna.calculateLunarState
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@SuppressLint("MissingPermission")
@Composable
fun MoonScreen(
    hazeState: HazeState = remember { HazeState() }
) {
    val clock = Clock.System
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
    val moonState = clock.now().calculateLunarState(
        longitude = longitude,
        latitude = latitude
    )
    val hazeStyle = HazeStyle(
        backgroundColor = Color.White.copy(alpha = 0.7f),
        tint = HazeTint(Color.White.copy(alpha = 0.7f)),
        blurRadius = 8.dp

    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .background(brush = MoonPhasesBackgrounds.NewMoonBg)
            .statusBarsPadding()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),


    ) {
        Column(
            modifier = Modifier

                .padding(16.dp)
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(25))
                .hazeEffect(state = hazeState,style = hazeStyle)
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .6f),
                            Color.Black.copy(alpha = .2f),
                        ),
                    ),
                    shape = RoundedCornerShape(25)
                )
                .dropShadow(
                    shape = RoundedCornerShape(25),
                    block = {
                        radius = 5f
                        alpha = 0.1f

                    }
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            Text(text = getMoonPhase(moonState.illumination.phase))
            Icon(
                tint = FullMoon,
                modifier = Modifier,
                imageVector = getMoonIcon(moonState.illumination.phase),
                contentDescription = "Faza księżyca"
            )
        }

    }
}