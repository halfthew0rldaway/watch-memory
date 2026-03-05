package com.example.watchmemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.watchmemory.ui.navigation.WatchMemoryNavGraph
import com.example.watchmemory.ui.theme.WatchMemoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as WatchMemoryApp

        setContent {
            WatchMemoryTheme {
                val navController = rememberNavController()
                WatchMemoryNavGraph(
                    navController = navController,
                    repository = app.repository,
                    userPreferences = app.userPreferences
                )
            }
        }
    }
}
