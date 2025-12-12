package com.azurowski.clarus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.ui.permissions.RequestLocationPermissions
import com.azurowski.clarus.ui.theme.Black70
import com.azurowski.clarus.ui.theme.CardBackgrounds
import com.azurowski.clarus.ui.theme.WeatherBackgrounds
import com.azurowski.clarus.ui.theme.White25
import com.azurowski.clarus.ui.theme.WhiteTransparent
import com.azurowski.clarus.ui.weather.WeatherUiState
import com.azurowski.clarus.ui.weather.WeatherViewModel
import com.azurowski.clarus.ui.weather.getWeatherIcon
import com.azurowski.clarus.ui.weather.mapCurrentWeather
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
        modifier = modifier.padding(top = 20.dp),
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Label("Noc", modifier = Modifier.weight(1f))
            Label("Jutro", modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WeatherCard(modifier = Modifier.weight(1f), false, 13, 17, 15,
                arrayOf("clear_night", "mid_rain")
            )
            WeatherCard(modifier = Modifier.weight(1f), true, 15, 20, 18, arrayOf("sunny"))
        }

        Label("Prognoza godzinowa")
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherApp()
}