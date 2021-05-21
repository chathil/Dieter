package com.example.dieter.data.source.firebase.request

import com.example.dieter.data.source.domain.GoalType

data class SetGoalRequest(
    val target: GoalType,
    val age: Int,
    val isMale: Boolean,
    val height: Int,
    val weight: Int,
    val addedAt: Long
)