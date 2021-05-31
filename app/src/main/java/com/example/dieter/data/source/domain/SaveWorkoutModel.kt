package com.example.dieter.data.source.domain

import com.example.dieter.data.source.firebase.request.SaveWorkoutRequest

data class SaveWorkoutModel(
    val workouts: List<WorkoutModel>,
    val totalCaloriesBurned: Int,
) {
    data class WorkoutModel(
        val id: String,
        val name: String,
        val calorieBurnedPerMinute: Int,
        val targetMs: Int,
        val actualMs: Int
    )
}

fun SaveWorkoutModel.asRequest() = SaveWorkoutRequest(
    workouts.map {
        SaveWorkoutRequest.WorkoutModel(
            it.id,
            it.name,
            it.calorieBurnedPerMinute,
            it.targetMs,
            it.actualMs
        )
    },
    totalCaloriesBurned
)
