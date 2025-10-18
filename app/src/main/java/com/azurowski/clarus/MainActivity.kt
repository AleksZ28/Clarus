package com.azurowski.clarus

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.azurowski.clarus.ui.theme.ClarusTheme
import com.azurowski.clarus.WeatherApi
import com.azurowski.clarus.ui.theme.WeatherBackgrounds
import com.azurowski.clarus.ui.weather.WeatherUiState
import com.azurowski.clarus.ui.weather.WeatherViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.math.round
import com.azurowski.clarus.ui.theme.WhiteTransparent

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
            .background(brush = WeatherBackgrounds.Night)
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
                val temp = round((state as WeatherUiState.Success).data.current.apparent_temperature).toInt()
                Image(
                    painter = painterResource(R.drawable.moon),
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
                    text = "$temp°",
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
    }
}

@Composable
fun RequestLocationPermissions(onPermissionGranted: () -> Unit) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> onPermissionGranted()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> onPermissionGranted()
            else -> Toast.makeText(context, "Brak dostępu do lokalizacji", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherApp()
}