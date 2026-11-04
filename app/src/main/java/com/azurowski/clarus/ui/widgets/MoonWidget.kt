package com.azurowski.clarus.ui.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.layout.Alignment
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

object MoonWidget: GlanceAppWidget(){
    override val stateDefinition = PreferencesGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme{
                MoonWidgetContent()
            }
        }
    }
}

@Composable
fun MoonWidgetContent(){
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Moon Widget",
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
        )
    }
}

class MoonWidgetReceiver: GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget = MoonWidget
}