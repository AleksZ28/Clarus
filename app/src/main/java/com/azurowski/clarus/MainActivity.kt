package com.azurowski.clarus

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.model.HourlyWeather
import com.azurowski.clarus.ui.charts.MakeTemperatureChart
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.Black70
import com.azurowski.clarus.ui.theme.CardBackgrounds
import com.azurowski.clarus.ui.theme.WeatherBackgrounds
import com.azurowski.clarus.ui.theme.WeatherBlue
import com.azurowski.clarus.ui.theme.White25
import com.azurowski.clarus.ui.theme.WhiteTransparent
import com.azurowski.clarus.ui.weather.WeatherUiState
import com.azurowski.clarus.ui.weather.WeatherViewModel
import com.azurowski.clarus.ui.weather.getWeatherIcon
import com.azurowski.clarus.ui.weather.mapCurrentWeather
import com.azurowski.clarus.ui.weather.mapNext24Hours
import com.azurowski.clarus.ui.weather.mapWeatherBackground
import com.google.android.gms.location.LocationServices
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun Label(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                color = Black70,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun WeatherCard(modifier: Modifier = Modifier, day: Boolean, lowestTemperature: Int, highestTemperature: Int, temperature: Int, weatherTypes: Array<String>){
    Column(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .background(
                brush = if (day) CardBackgrounds.Day else CardBackgrounds.Night,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = lowestTemperature.toString(),
            style = TextStyle(
                color = if (day) Color(0x990062FF) else Color(0x99AFF3FF),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
        )

        Text(
            text = " $temperature°",
            style = TextStyle(
                color = WhiteTransparent,
                fontWeight = FontWeight.Black,
                fontSize = 48.sp
            )
        )

        Text(
            text = highestTemperature.toString(),
            style = TextStyle(
                color = Color(0x99FF0004),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
        )

        Box(
            modifier = Modifier.padding(top = 20.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White25,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),

                horizontalArrangement = Arrangement.Center

                ){
                    weatherTypes.forEach { weather ->
                        Image(
                            painter = painterResource(getWeatherIcon(weather)),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
        }

    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun HourlyWeatherRow(hourlyWeather: List<HourlyWeather>){

    val hours = hourlyWeather.map { it ->
        it.hour.toInt()
    }

    val temperatures = hourlyWeather.map { it ->
        round(it.temperature).toInt()
    }

    val scrollState = rememberScrollState()

    val itemWidthDp = 56.dp
    val totalWidth = itemWidthDp * 24 + 16.dp + 16.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val fadeWidth = 0.1f
                val fadeBrush = Brush.horizontalGradient(
                    0.0f to Color.Transparent,
                    fadeWidth to Color.Black,
                    (1.0f - fadeWidth) to Color.Black,
                    1.0f to Color.Transparent
                )

                onDrawWithContent {
                    drawContent()
                    drawRect(
                        brush = fadeBrush,
                        blendMode = BlendMode.DstIn
                    )
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Row (
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                hourlyWeather.forEach { weather ->
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Text(
                            text = if (weather.weatherType in listOf("snowy", "heavy_rain", "mid_rain", "low_rain")) weather.precipitationProbability.toString() + "%" else "",
                            style = TextStyle(
                                color = WeatherBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )

                        Image(
                            painter = painterResource(getWeatherIcon(weather.weatherType)),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )

                        Text(
                            text = if (weather.weatherType in listOf("snowy", "heavy_rain", "mid_rain", "low_rain")) weather.precipitation.toString() + " mm" else "",
                            style = TextStyle(
                                color = WeatherBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )

                        Text(
                            text=weather.hour,
                            style = TextStyle(
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(120.dp)
                    .width(totalWidth)
                    .padding(horizontal = 44.dp, vertical = 8.dp)
            ) {
                MakeTemperatureChart(modifier = Modifier.fillMaxWidth(), x = hours, y = temperatures)
            }
        }
    }


}

@Composable
fun WeatherApp(viewModel: WeatherViewModel = remember { WeatherViewModel(WeatherApi) }) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    RequestLocationPermissions(
        onPermissionGranted = {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            }
        }
    )


    Column(
        modifier = Modifier
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
            is WeatherUiState.Idle -> Text("Nie pobrano pogody")
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> {
                val currentWeather = (state as WeatherUiState.Success).data.current
                val currentTemp = round(currentWeather.apparent_temperature).toInt()
                val currentWeatherType = mapCurrentWeather(currentWeather)
                val currentWeatherIcon = getWeatherIcon(currentWeatherType)

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
                    text = " $currentTemp°",
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

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 30.dp),
//                    horizontalArrangement = Arrangement.spacedBy(20.dp)
//                ) {
//                    Label("Noc", modifier = Modifier.weight(1f))
//                    Label("Jutro", modifier = Modifier.weight(1f))
//                }

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 30.dp, end = 30.dp, bottom = 20.dp),
//                    horizontalArrangement = Arrangement.spacedBy(20.dp)
//                ) {
//                    WeatherCard(modifier = Modifier.weight(1f), false, 13, 17, 15,
//                        arrayOf("night", "mid_rain")
//                    )
//                    WeatherCard(modifier = Modifier.weight(1f), true, 15, 20, 18, arrayOf("sunny"))
//                }

                Label("Prognoza godzinowa")

                val hourlyWeatherFromApi = (state as WeatherUiState.Success).data.hourly
                val hourlyWeather = mapNext24Hours(hourlyWeatherFromApi)

                HourlyWeatherRow(hourlyWeather)

            }

            is WeatherUiState.Error -> Text("Błąd: ${(state as WeatherUiState.Error).message}")
        }

        Button(
            onClick = {
                if(latitude != null && longitude != null) {
                    viewModel.fetchWeather(latitude!!, longitude!!)
                }
            },
            enabled = latitude != null && longitude != null
        ) {
            Text("Pobierz pogodę")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherApp()
}