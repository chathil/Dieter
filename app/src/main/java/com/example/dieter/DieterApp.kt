package com.example.dieter

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.dieter.ui.theme.DieterTheme
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun DieterApp(
    appState: DieterAppState,
    showWelcomeInitially: Boolean,
    welcomeShown: () -> Unit,
    finishActivity: () -> Unit
) {
    ProvideWindowInsets {
        DieterTheme {
            // A surface container using the 'background' color from the theme
            val navController = rememberNavController()

            Surface(color = MaterialTheme.colors.background) {
                NavGraph(
                    appState = appState,
                    showWelcomeInitially = showWelcomeInitially,
                    welcomeShown = welcomeShown,
                    finishActivity = finishActivity,
                    navController = navController,
                )
            }
        }
    }
}
