package com.example.dieter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dieter.R

private val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val DieterTypography = Typography(
    h1 = TextStyle(
        fontFamily = Poppins,
        fontSize = 96.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 112.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = Poppins,
        fontSize = 60.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 72.sp,
        letterSpacing = (-0.8).sp
    ),
    h3 = TextStyle(
        fontFamily = Poppins,
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 0.56.sp
    ),
    h4 = TextStyle(
        fontFamily = Poppins,
        fontSize = 34.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 0.36.sp
    ),
    h5 = TextStyle(
        fontFamily = Poppins,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.75.sp
    ),
    h6 = TextStyle(
        fontFamily = Poppins,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.75.sp,

    ),
    subtitle1 = TextStyle(
        fontFamily = Poppins,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.9.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Poppins,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.7.sp
    ),
    body1 = TextStyle(
        fontFamily = Poppins,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.31.sp
    ),
    body2 = TextStyle(
        fontFamily = Poppins,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.18.sp
    ),
    button = TextStyle(
        fontFamily = Poppins,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        letterSpacing = 0.89.sp
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.33.sp
    ),
    overline = TextStyle(
        fontFamily = Poppins,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.33.sp
    )
)
