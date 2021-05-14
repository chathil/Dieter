package com.example.dieter.data.source.domain

import java.util.Date

data class TodaysFoodModel(
    val type: FoodType,
    val name: String,
    val img: String,
    val cal: Int,
    val consumedAt: Date
)
