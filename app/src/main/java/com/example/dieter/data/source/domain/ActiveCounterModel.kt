package com.example.dieter.data.source.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActiveCounterModel(
    val now: Long,
    val peak: Long,
    val workout: WorkoutModel
) : Parcelable
