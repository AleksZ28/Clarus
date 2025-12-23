package com.azurowski.clarus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azurowski.clarus.model.WeatherSummary
import com.azurowski.clarus.ui.theme.Black70
import com.azurowski.clarus.ui.theme.CardBackgrounds
import com.azurowski.clarus.ui.theme.White25
import com.azurowski.clarus.ui.theme.WhiteTransparent
import com.azurowski.clarus.ui.weather.getWeatherIcon

@Composable
fun WeatherCard(modifier: Modifier = Modifier, day: Boolean, weatherSummary: WeatherSummary){
    val lowestTemperature = weatherSummary.minTemperature
    val temperature = weatherSummary.medianTemperature
    val highestTemperature = weatherSummary.maxTemperature
    val weatherTypes = weatherSummary.weatherTypes

    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .background(
                brush = if (day) CardBackgrounds.Day else CardBackgrounds.Night,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = {},
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            )
    ) {
        Column(
            modifier = modifier.padding(20.dp),
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
                text = if (temperature >= 0) " $temperature°" else "$temperature°",
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

                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
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

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = if (!day) WhiteTransparent else Black70,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 64.dp, horizontal = 18.dp)
                .size(24.dp)
        )
    }
}