package com.example.dieter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DieterTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DieterDarkColors
    } else {
        DieterLightColors
    }

    MaterialTheme(
        colors = colors,
        typography = DieterTypography,
        shapes = DieterShapes,
        content = content
    )
}
