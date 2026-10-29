package com.azurowski.clarus.ui.screens.moon

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.ui.moonUtils.getMoonBackground
import com.azurowski.clarus.ui.moonUtils.getMoonIcon
import com.azurowski.clarus.ui.moonUtils.getMoonPhase
import com.azurowski.clarus.ui.moonUtils.getMoonZodiacSign
import com.azurowski.clarus.ui.moonUtils.getPhaseData
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.FullMoon
import com.google.android.gms.location.LocationServices
import compose.icons.WeatherIcons
import compose.icons.weathericons.Moonrise
import compose.icons.weathericons.Moonset
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.HazeColorEffect
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.glass.GlassOptics
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.OpticalSizeValue
import dev.chrisbanes.haze.glass.SurfaceProfile
import dev.chrisbanes.haze.glass.hazeGlass
import dev.chrisbanes.haze.glass.material3.material3
import dev.chrisbanes.haze.hazeSource
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.luna.LunarEventSequence
import dev.jamesyox.kastro.luna.LunarPhaseSequence
import dev.jamesyox.kastro.luna.calculateLunarState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalHazeApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MoonScreen(
    hazeState: HazeState = remember { HazeState() }
) {
    val clock = remember{Clock.System}
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
    val moonState = remember(longitude,latitude,clock){clock.now().calculateLunarState(
        longitude = longitude,
        latitude = latitude
    )}
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
    val hazeBlurStyle = HazeBlurStyle {
        blurRadius(16.dp)
        colorEffects(listOf(
            HazeColorEffect.tint(Color.White.copy(alpha=0.4f))))
        backgroundColor(Color.White.copy(alpha = 1f))
    }
Box(
    modifier = Modifier
        .fillMaxSize()

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
            .background(brush = getMoonBackground(moonState.illumination.phase))
    ){

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,


        ) {
        Icon(
            tint = FullMoon,
            modifier = Modifier
                .size(120.dp),
            imageVector = getMoonIcon(moonState.illumination.phase),
            contentDescription = "Faza księżyca",

            )
        Text(
            text = getMoonPhase(moonState.illumination.phase),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = .25f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                        ),
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .dropShadow(
                    shape = RoundedCornerShape(25.dp),
                    block = {
                        radius = 2f
                        alpha = 0.25f
                    }
                )
                .clip(RoundedCornerShape(25.dp))
                .hazeBlur(input = HazeInput.Backdrop(hazeState),style=hazeBlurStyle)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            val nextMoonHorizonEvents = remember(longitude,latitude,clock){LunarEventSequence(
                start = clock.now(),
                longitude = longitude,
                latitude = latitude,
                requestedLunarEvents = listOf(
                    LunarEvent.HorizonEvent.Moonrise,
                    LunarEvent.HorizonEvent.Moonset
                )
            )}
            val nextMoonRise =remember(longitude,latitude,clock){
                nextMoonHorizonEvents.first().time.toLocalDateTime(TimeZone.currentSystemDefault())
                    .let { localTime ->
                        String.format(
                            "%02d:%02d:%02d",
                            localTime.hour,
                            localTime.minute,
                            localTime.second
                        )
                    } ?: "Brak danych"}
            val nextMoonSet =remember(longitude,latitude,clock){
                nextMoonHorizonEvents.last().time.toLocalDateTime(TimeZone.currentSystemDefault())
                    .let { localTime ->
                        String.format(
                            "%02d:%02d:%02d",
                            localTime.hour,
                            localTime.minute,
                            localTime.second
                        )
                    } ?: "Brak danych"}
            val moonPercentage =
                BigDecimal(moonState.illumination.fraction * 100).setScale(2, RoundingMode.HALF_UP)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    contentDescription = "Wschód ksieżyca",
                    imageVector = WeatherIcons.Moonrise,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = nextMoonRise,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    text = "Oświetlenie księżyca",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = moonPercentage.toString() + "%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    contentDescription = "Zachód ksieżyca",
                    imageVector = WeatherIcons.Moonset,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = nextMoonSet,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }
        HorizontalDivider(modifier = Modifier.padding(10.dp),color = MaterialTheme.colorScheme.onSurface)
        Text(
            text = "Nadchodzące fazy księżyca",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 16.dp,bottom=24.dp)
        )
        val upcomingMoonPhases = remember(clock){LunarPhaseSequence(
            start = clock.now(),
            requestedLunarPhases = listOf(
                LunarEvent.PhaseEvent.FullMoon,
                LunarEvent.PhaseEvent.NewMoon,
                LunarEvent.PhaseEvent.FirstQuarter,
                LunarEvent.PhaseEvent.LastQuarter
            ),
            limit = 30.days
        ).toList()}


        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            upcomingMoonPhases.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { phaseEvent ->
                        Box(modifier = Modifier
                            .weight(1f)
                            .dropShadow(
                                shape = RoundedCornerShape(25.dp),
                                block = {
                                    radius = 2f
                                    alpha = 0.1f
                                }

                            )
                            .border(
                                width = Dp.Hairline,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = .25f),
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                                    ),
                                ),
                                shape = RoundedCornerShape(25.dp))
                            .clip(RoundedCornerShape(25.dp))
                            .hazeBlur(input = HazeInput.Backdrop(hazeState),style=hazeBlurStyle)
                            .padding(8.dp)

                        ) {
                            val currentUpcomingEvent = getPhaseData(phaseEvent)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            )
                            {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(bottom=8.dp)
                                ) {
                                    Text(
                                        text = currentUpcomingEvent.first,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = phaseEvent.time.toLocalDateTime(TimeZone.currentSystemDefault())
                                            .let { localTime ->
                                                String.format(
                                                    "%02d.%02d.%04d",
                                                    localTime.day,
                                                    localTime.monthNumber,
                                                    localTime.year
                                                )
                                            },
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = phaseEvent.time.toLocalDateTime(TimeZone.currentSystemDefault())
                                            .let { localTime ->
                                                String.format(
                                                    "%02d:%02d:%02d",
                                                    localTime.hour,
                                                    localTime.minute,
                                                    localTime.second
                                                )
                                            },
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Icon(
                                    contentDescription = "Faza księżyca",
                                    imageVector = currentUpcomingEvent.second,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(40.dp)

                                )
                            }


                        }
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp,vertical=24.dp),color = MaterialTheme.colorScheme.onSurface)
        Text(
            text = "Znak zodiaku",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(bottom=24.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .dropShadow(
                    shape = RoundedCornerShape(25.dp),
                    block = {
                        radius = 2f
                        alpha = 0.1f
                    }
                )
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = .25f),
                        MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
                    )),
                    shape = RoundedCornerShape(25.dp)
                    )
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .hazeBlur(input = HazeInput.Backdrop(hazeState),style=hazeBlurStyle)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically

        ) {
            val currentMoonZodiacSign = getMoonZodiacSign()
            Text(
                text = currentMoonZodiacSign.first,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Icon(
                contentDescription = "Księżycowy znak zodiaku",
                imageVector = currentMoonZodiacSign.second,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)

            )
        }
        Column(
            modifier = Modifier

                .padding(16.dp)
                .height(IntrinsicSize.Min)
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .25f),
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

        ) {


            Text(
                text = moonState.toString(),
                color = Color.White
            )
        }

    }
}
}

@Preview
@Composable
fun MoonPreview(){
    MoonScreen()
}