package com.example.dieter.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val RedPrimary = Color(0xFFF30493)
val RedLight = Color(0xFFFF5CC3)
val RedDark = Color(0xFFBB0065)

val GreenPrimary = Color(0xFF00E676)
val GreenLight = Color(0xFF66FFA6)
val GreenDark = Color(0xFF00B248)

val YellowPrimary = Color(0xFFFFEA00)
val YellowLight = Color(0xFFFFFF56)
val YellowDark = Color(0xFFC7B800)

val PurplePrimary = Color(0xFFAF0CFF)
val PurpleLight = Color(0xFFE657FF)
val PurpleDark = Color(0xFF7800CB)

val DieterLightColors = lightColors(
    primary = RedPrimary,
    onPrimary = Color.Black,
    primaryVariant = RedLight,
    secondary = RedPrimary,
    onSecondary = Color.Black,
    error = RedDark,
    onError = Color.White
)

val DieterDarkColors = darkColors(
    primary = GreenPrimary,
    onPrimary = Color.Black,
    primaryVariant = RedLight,
    secondary = GreenPrimary,
    onSecondary = Color.Black,
    error = RedDark,
    onError = Color.White
)

const val AlphaNearOpaque = 0.95f
const val AlphaAlmostOpaque = 0.87f
const val AlphaNearTransparent = 0.4f
const val AlphaReallyTransparent = 0.04f
