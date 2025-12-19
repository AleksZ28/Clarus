package com.azurowski.clarus.ui.charts

/*
 * Copyright 2025 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.azurowski.clarus.ui.theme.WeatherBlue
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import java.text.DecimalFormat
import java.time.LocalDateTime

private val YDecimalFormat = DecimalFormat("##'°'")
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

@Composable
private fun TemperatureChart(
    modelProducer: CartesianChartModelProducer,
    RangeProvider: CartesianLayerRangeProvider,
    modifier: Modifier = Modifier,
) {
    val lineColor = WeatherBlue
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                            areaFill = LineCartesianLayer.AreaFill.single(
                                fill(
                                    ShaderProvider.verticalGradient(
                                        arrayOf(lineColor.copy(alpha = 0.4f), Color.Transparent)
                                    )
                                )
                            ),
                        )
                    ),
                rangeProvider = RangeProvider
            ),
            marker = rememberMarker(MarkerValueFormatter),
        ),
        modelProducer,
        modifier.height(120.dp),
        rememberVicoScrollState(scrollEnabled = false),
    )
}


@Composable
fun MakeTemperatureChart(modifier: Modifier = Modifier, x: List<Int>, y: List<Int>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val RangeProvider = CartesianLayerRangeProvider.fixed(maxY = y.max().toDouble())
    LaunchedEffect(Unit) {
        val current = LocalDateTime.now()
        val xAdjusted = x.map { it + current.hour }
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/vmml6t.
            lineSeries { series(xAdjusted, y) }
        }
    }

    TemperatureChart(modelProducer, RangeProvider, modifier)

}