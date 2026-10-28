package com.azurowski.clarus.ui.moonUtils

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.AllIcons
import compose.icons.TablerIcons
import compose.icons.tablericons.Shadow
import compose.icons.tablericons.ZodiacAquarius
import compose.icons.tablericons.ZodiacAries
import compose.icons.tablericons.ZodiacCancer
import compose.icons.tablericons.ZodiacCapricorn
import compose.icons.tablericons.ZodiacGemini
import compose.icons.tablericons.ZodiacLeo
import compose.icons.tablericons.ZodiacLibra
import compose.icons.tablericons.ZodiacPisces
import compose.icons.tablericons.ZodiacSagittarius
import compose.icons.tablericons.ZodiacScorpio
import compose.icons.tablericons.ZodiacTaurus
import compose.icons.tablericons.ZodiacVirgo
import kotlin.math.sin
import kotlin.time.Clock


@Composable
fun getMoonZodiacSign(): Pair<String, ImageVector> {
    val instant = Clock.System.now()
    val epochMilli = instant.toEpochMilliseconds().toDouble()
    val jd = epochMilli / 86400000.0 + 2440587.5
    val t = (jd - 2451545.0) / 36525.0
    val lPrime = 218.3164477 + 481267.88123421 * t
    val d = 297.8501921 + 445267.1114034 * t
    val m = 357.5291092 + 35999.0502909 * t
    val mPrime = 134.9633964 + 477198.8675055 * t
    val f = 93.2720950 + 483202.0175233 * t
    fun toRad(deg: Double): Double = (deg % 360.0) * (Math.PI / 180.0)

    val perturbations =
        6.288774 * sin(toRad(mPrime)) +
                1.274027 * sin(toRad(2 * d - mPrime)) +
                0.658309 * sin(toRad(2 * d)) +
                0.213618 * sin(toRad(2 * mPrime)) -
                0.185116 * sin(toRad(m)) -
                0.114332 * sin(toRad(2 * f)) +
                0.058793 * sin(toRad(2 * d - 2 * mPrime)) +
                0.057066 * sin(toRad(2 * d - m - mPrime)) +
                0.053322 * sin(toRad(2 * d + mPrime)) +
                0.045758 * sin(toRad(2 * d - m)) -
                0.040923 * sin(toRad(m - mPrime)) -
                0.034720 * sin(toRad(d))
    val trueLongitude = (lPrime + perturbations) % 360.0
    val normalized = (trueLongitude + 360) % 360.0
    val index = (normalized / 30.0).toInt().coerceIn(0,11)
    return when(index){
        0 -> Pair("Baran", TablerIcons.ZodiacAries)
        1 -> Pair("Byk", TablerIcons.ZodiacTaurus)
        2 -> Pair("Bliźnięta", TablerIcons.ZodiacGemini)
        3 -> Pair("Rak", TablerIcons.ZodiacCancer)
        4 -> Pair("Lew", TablerIcons.ZodiacLeo)
        5 -> Pair("Panna", TablerIcons.ZodiacVirgo)
        6 -> Pair("Waga", TablerIcons.ZodiacLibra)
        7 -> Pair("Skorpion", TablerIcons.ZodiacScorpio)
        8 -> Pair("Strzelec", TablerIcons.ZodiacSagittarius)
        9 -> Pair("Kozirożec", TablerIcons.ZodiacCapricorn)
        10 -> Pair("Wodnik", TablerIcons.ZodiacAquarius)
        11 -> Pair("Ryby", TablerIcons.ZodiacPisces)
        else -> Pair("LWk impossible", TablerIcons.Shadow)
    }
}