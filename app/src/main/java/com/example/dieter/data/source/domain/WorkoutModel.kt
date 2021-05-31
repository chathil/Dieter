package com.example.dieter.data.source.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutModel(
    val id: String,
    val name: String,
    val calorieBurnedPerMinute: Int,
) : Parcelable
