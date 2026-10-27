package com.azurowski.clarus.ui.screens.moon

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.azurowski.clarus.ui.moonUtils.getMoonIcon
import com.azurowski.clarus.ui.moonUtils.getMoonPhase
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.FullMoon
import com.azurowski.clarus.ui.theme.MoonPhasesBackgrounds
import com.google.android.gms.location.LocationServices
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.HazeColorEffect
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.blur.material3.Material3
import dev.chrisbanes.haze.glass.GlassOptics
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.OpticalSizeValue
import dev.chrisbanes.haze.glass.SurfaceProfile
import dev.chrisbanes.haze.glass.hazeGlass
import dev.chrisbanes.haze.glass.material3.material3
import dev.chrisbanes.haze.hazeSource
import dev.jamesyox.kastro.luna.calculateLunarState
import kotlin.time.Clock

@OptIn(ExperimentalHazeApi::class)
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
    val optics = GlassOptics(
        blurRadius = OpticalSizeValue.Fixed(30.dp),
        refractionStrength = 0.9f,
        refractionHeightFraction = 1f,
        refractionDisplacement = 20.dp,
        refractionFoldStrength = 0.7f,
        depth = OpticalSizeValue.Fixed(0.8f),
    )
    val hazeStyle = GlassStyle{
        optics(optics)
        surfaceProfile(SurfaceProfile.Squircle)

    }
//    val hazeStyle = HazeBlurStyle.Material3() {
//        blurEnabled(true)
//        blurRadius(24.dp)
//        noiseFactor(.08f)
//        colorEffects(listOf(HazeColorEffect.tint(Color.White.copy(alpha = 0.70f))))
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MoonPhasesBackgrounds.NewMoonBg)
            .statusBarsPadding()
            .hazeSource(hazeState)
            ,
        contentAlignment = Alignment.TopCenter


    ) {
//        Box(
//            modifier = Modifier
//                .size(100.dp)
//                .align(Alignment.TopCenter)
//                .offset(y = 50.dp)
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(
//                            FullMoon.copy(alpha = 0.35f),
//                            Color.Transparent
//                        )
//                    )
//                )
//        ){}
        Column(
            modifier = Modifier

                .padding(16.dp)
                .height(IntrinsicSize.Min)
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .45f),
                            Color.Black.copy(alpha = .1f),
                        ),
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .dropShadow(
                    shape = RoundedCornerShape(25.dp),
                    block = {
                        radius = 20f
                        alpha = 0.25f

                    }
                )
                .clip(RoundedCornerShape(25.dp))
//                .background(Color.Black.copy(alpha = .7f), RoundedCornerShape(25.dp))
                .hazeGlass(
                    input = HazeInput.Backdrop(hazeState),
                    style = hazeStyle
                )


                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            Text(text = getMoonPhase(moonState.illumination.phase),
                color = Color.White)
            Icon(
                tint = FullMoon,
                modifier = Modifier,
                imageVector = getMoonIcon(moonState.illumination.phase),
                contentDescription = "Faza księżyca"
            )
        }

    }
}

@Preview
@Composable
fun MoonPreview(){
    MoonScreen()
}