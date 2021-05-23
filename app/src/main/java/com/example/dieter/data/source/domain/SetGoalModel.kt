package com.example.dieter.data.source.domain

import com.example.dieter.data.source.firebase.request.SetGoalRequest

data class SetGoalModel(
    val userRepId: String,
    val target: GoalType,
    val age: Int,
    val isMale: Boolean,
    val height: Int,
    val weight: Int,
    val targetWeight: Int
)

fun SetGoalModel.asRequest() = SetGoalRequest(target, age, isMale, height, weight, targetWeight, System.currentTimeMillis())
