package com.azurowski.clarus.ui.screens

import android.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.azurowski.clarus.WeatherApi
import com.azurowski.clarus.ui.components.HourlyWeatherRow
import com.azurowski.clarus.ui.components.Label
import com.azurowski.clarus.ui.components.WeatherCard
import com.azurowski.clarus.ui.components.nav.NavBar
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.WeatherBackgrounds
import com.azurowski.clarus.ui.theme.WhiteTransparent
import com.azurowski.clarus.ui.weather.WeatherUiState
import com.azurowski.clarus.ui.weather.WeatherViewModel
import com.azurowski.clarus.ui.weather.WeatherViewModelFactory
import com.azurowski.clarus.ui.weather.getWeatherIcon
import com.azurowski.clarus.ui.weather.mapCurrentWeather
import com.azurowski.clarus.ui.weather.mapNext24Hours
import com.azurowski.clarus.ui.weather.mapNextDay
import com.azurowski.clarus.ui.weather.mapNextNight
import com.azurowski.clarus.ui.weather.mapWeatherBackground
import com.azurowski.clarus.ui.weather.parseDayWeather
import com.azurowski.clarus.ui.weather.parseNightWeather
import com.google.android.gms.location.LocationServices
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlin.math.round

fun downloadWeather(latitude: Double?, longitude: Double?, viewModel: WeatherViewModel, forceRefresh: Boolean = false) {
    if(latitude != null && longitude != null) {
        viewModel.fetchWeather(latitude, longitude, forceRefresh)
    }
}
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: WeatherViewModel = viewModel(factory = WeatherViewModelFactory(WeatherApi))
    val state by viewModel.uiState.collectAsState()
    val isRefreshing = state is WeatherUiState.Loading


    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var longitude by rememberSaveable { mutableStateOf<Double?>(null) }

    RequestLocationPermissions(
        onPermissionGranted = {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude                }
            }
        }
    )

    val hazeState = remember { HazeState() }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { downloadWeather(latitude, longitude, viewModel, true) },
        modifier = Modifier.fillMaxSize()
    ) {
        val buttonHazeStyle = HazeStyle(
            tint = HazeTint(Color.White.copy(alpha = 0.7f)),
            blurRadius = 8.dp
        )
        val buttonShape = CircleShape

        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .background(
                    brush = when (state) {
                        is WeatherUiState.Success -> mapWeatherBackground((state as WeatherUiState.Success).data.current)
                        else -> WeatherBackgrounds.Sunny
                    }
                )
                .verticalScroll(rememberScrollState())
                .padding(
                    vertical = 30.dp
                )
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            when (state) {
                is WeatherUiState.Idle -> Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = .7f), RoundedCornerShape(50.dp))
                        .hazeEffect(state = hazeState, style = buttonHazeStyle)
                        .border(
                            width = Dp.Hairline,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Black.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .dropShadow(
                            shape = RoundedCornerShape(50.dp),
                            block = {
                                radius = 5f
                                alpha = .1f
                            }
                        )

                        .padding(16.dp)

                ){Text("Nie pobrano pogody")}
                is WeatherUiState.Loading -> CircularProgressIndicator()
                is WeatherUiState.Success -> {
                    val currentWeather = (state as WeatherUiState.Success).data.current
                    val currentTemp = round(currentWeather.apparent_temperature).toInt()
                    val currentWeatherType = mapCurrentWeather(currentWeather)
                    val currentWeatherIcon = getWeatherIcon(currentWeatherType)
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(currentWeatherIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .dropShadow(
                                shape = RectangleShape,
                                block = {
                                    radius = 1000f
                                    spread = 0.05f
                                    alpha = 0.25f
                                }
                            )
                    )
                    Text(
                        text = if (currentTemp >= 0) " $currentTemp°" else "$currentTemp°",
                        style = TextStyle(
                            color = WhiteTransparent,
                            fontSize = 128.sp,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.25f),
                                blurRadius = 25f
                            )
                        )
                    )

                    val hourlyWeatherFromApi = (state as WeatherUiState.Success).data.hourly

                    val nextNightWeather = mapNextNight(hourlyWeatherFromApi)
                    val parsedNextNightWeather = parseNightWeather(nextNightWeather)
                    Log.d("NIGHT_WEATHER", parsedNextNightWeather.toString())

                    val nextDayWeather = mapNextDay(hourlyWeatherFromApi, latitude!!, longitude!!)
                    val parsedNextDayWeather = parseDayWeather(nextDayWeather)
                    Log.d("DAY_WEATHER", parsedNextDayWeather.toString())

                    val isCurrentDayNight = nextDayWeather[0].isCurrentDayNight!!

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Label(text = if (!isCurrentDayNight) "Noc" else "Nadchodzący dzień", modifier = Modifier.weight(1f))
                        Label(text = if (!isCurrentDayNight) "Jutro" else "Następna noc", modifier = Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (!isCurrentDayNight){
                            WeatherCard(modifier = Modifier.weight(1f), false, parsedNextNightWeather, navController)
                            WeatherCard(modifier = Modifier.weight(1f), true, parsedNextDayWeather, navController)
                        } else {
                            WeatherCard(modifier = Modifier.weight(1f), true, parsedNextDayWeather, navController)
                            WeatherCard(modifier = Modifier.weight(1f), false, parsedNextNightWeather, navController)
                        }

                    }

                    Label("Prognoza godzinowa")

                    val hourlyWeather = mapNext24Hours(hourlyWeatherFromApi)

                    HourlyWeatherRow(hourlyWeather)

                    Spacer(
                        modifier = Modifier.height(50.dp)
                    )

                }

                is WeatherUiState.Error -> Text("Błąd: ${(state as WeatherUiState.Error).message}")
            }

//            Button(
//                onClick = {
//                    if(latitude != null && longitude != null) {
//                        viewModel.fetchWeather(latitude!!, longitude!!)
//                    }
//                },
//                enabled = latitude != null && longitude != null
//            ) {
//                Text("Pobierz pogodę")
//            }
        }



        Button(
            onClick = { navController.navigate("locationChange") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            shape = buttonShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .navigationBarsPadding()
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .size(48.dp)
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
                )
                .clip(buttonShape)
                .hazeEffect(state = hazeState, style = buttonHazeStyle)
                .align(Alignment.TopStart)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Lokalizacja"
                )
//                    Text(
//                        text = "Twoja lokalizacja",
//                        fontWeight = FontWeight.Medium
//                    )
            }
        }

        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            hazeState = hazeState
        )
    }
}


@Preview
@Composable
fun HomePreview(){
    HomeScreen(navController = NavController(LocalContext.current))
}