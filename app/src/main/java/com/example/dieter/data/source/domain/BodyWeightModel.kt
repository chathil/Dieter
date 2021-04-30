package com.example.dieter.data.source.domain

import java.util.Date

data class BodyWeightModel(
    val current: Int,
    val target: Int,
    val date: Date = Date()
)
