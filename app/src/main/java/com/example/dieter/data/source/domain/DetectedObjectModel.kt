package com.example.dieter.data.source.domain

data class DetectedObjectModel(
    val boxes: List<Int>,
    val color: List<Int>,
    val confidence: Float,
    val label: String
)
