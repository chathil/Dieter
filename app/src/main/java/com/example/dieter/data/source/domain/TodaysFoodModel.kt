package com.example.dieter.data.source.domain

import java.util.Date

data class TodaysFoodModel(
    val id: String,
    val type: FoodType,
    val name: String,
    val img: String? = "fake_food.jpg",
    val cal: Int,
    val consumedAt: Date
)
