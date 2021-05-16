package com.example.dieter.data.source.remote.response

import com.example.dieter.data.source.domain.MeasureModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeasureResponse(
    val uri: String,
    val label: String,
    @SerialName("weight")
    val weightInGrams: Float
)

fun MeasureResponse.asDomainModel() = MeasureModel(uri, label, weightInGrams)

fun List<MeasureResponse>.asDomainModels() =
    map { it.asDomainModel() }
