package com.azurowski.clarus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.model.HourlyWeather
import com.azurowski.clarus.ui.charts.MakeTemperatureChart
import com.azurowski.clarus.ui.theme.Black70
import com.azurowski.clarus.ui.theme.WeatherBlue
import com.azurowski.clarus.ui.theme.White25
import com.azurowski.clarus.ui.weather.getWeatherIcon
import kotlin.collections.forEach

@Composable
fun HourlyWeatherRow(hourlyWeather: List<HourlyWeather>){

    val temperatures = hourlyWeather.map { it -> it.temperature }

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
                            text = weather.hour + ":00",
                            style = TextStyle(
                                color = Black70,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(White25, shape = CircleShape)
                        ) {
                            Text(
                                text = weather.temperature.toString() + "°",
                                style = TextStyle(
                                    color = Black70,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(totalWidth)
                    .padding(horizontal = 44.dp)
            ) {
                MakeTemperatureChart(modifier = Modifier.fillMaxWidth(), y = temperatures)
            }
        }
    }
}