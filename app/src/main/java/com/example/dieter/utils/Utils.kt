package com.example.dieter.utils

import kotlin.math.pow
import kotlin.math.roundToInt

object EmulatorHost {
    const val ip = "192.168.43.225"
    const val authPort = 9099
    const val databasePort = 9000
    const val functionsPort = 5001
}

fun Float.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}
