package com.example.dieter.data.source.domain

import com.example.dieter.data.source.firebase.request.BodyWeightRequest
import java.util.Date

data class SetBodyWeightModel(
    val current: Int,
    val target: Int,
    val date: Date = Date()
)

fun SetBodyWeightModel.asRequest() = BodyWeightRequest(current, target, date.time)
