package com.azurowski.clarus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.azurowski.clarus.ui.navigation.NavigationRoot
import com.azurowski.clarus.ui.theme.ClarusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ClarusTheme{
                NavigationRoot()
            }
        }
    }
}