package com.example.dieter

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.dieter.ui.theme.DieterTheme
import com.example.dieter.vo.DataState
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun DieterApp(temporaryId: String, finishActivity: () -> Unit) {
    ProvideWindowInsets {
        DieterTheme {
            // A surface container using the 'background' color from the theme
            val navController = rememberNavController()

            Surface(color = MaterialTheme.colors.background) {
                NavGraph(
                    temporaryId = temporaryId,
                    finishActivity = finishActivity,
                    navController = navController,
                )
            }
        }
    }
}
