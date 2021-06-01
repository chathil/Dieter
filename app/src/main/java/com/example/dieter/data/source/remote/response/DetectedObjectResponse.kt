package com.example.dieter.data.source.remote.response

import com.example.dieter.data.source.domain.DetectedObjectModel
import kotlinx.serialization.Serializable

@Serializable
data class DetectedObjectResponse(
    val boxes: List<Int>,
    val color: List<Int>,
    val confidence: Float,
    val label: String
)

fun DetectedObjectResponse.asDomainModel() = DetectedObjectModel(boxes, color, confidence, label)
fun List<DetectedObjectResponse>.asDomainModel() = map { it.asDomainModel() }
