package com.example.dieter.data.source.firebase.request

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SaveWorkoutRequest(
    val workouts: List<WorkoutModel>,
    val totalCaloriesBurned: Int,
    val addedAt: Long = System.currentTimeMillis(),
    val date: String = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.UK
    ).format(Date(addedAt)), // get string date from addedAt
    val time: String = SimpleDateFormat(
        "HH:mm",
        Locale.UK
    ).format(Date(addedAt)), // get string time from addedAt
) {
    data class WorkoutModel(
        val id: String,
        val name: String,
        val calorieBurnedPerMinute: Int,
        val targetMs: Int,
        val actualMs: Int
    )
}
