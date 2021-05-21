package com.example.dieter.data.source.domain

import com.example.dieter.data.source.firebase.request.SetGoalRequest

data class SetGoalModel (
    val target: GoalType,
    val age: Int,
    val isMale: Boolean,
    val height: Int,
    val weight: Int
)

fun SetGoalModel.asRequest() = SetGoalRequest(target, age, isMale, height, weight, System.currentTimeMillis())