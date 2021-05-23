package com.example.dieter.data.source.domain

data class GoalModel(
    val target: GoalType,
    val age: Int,
    val isMale: Boolean,
    val height: Int,
    val weight: Int,
    val targetWeight: Int,
    val addedAt: Long
)
