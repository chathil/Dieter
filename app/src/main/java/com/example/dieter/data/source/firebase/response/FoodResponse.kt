package com.example.dieter.data.source.firebase.response

import com.example.dieter.data.source.domain.FoodType
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FoodResponse(
    val addedAt: Long? = null,
    val date: String? = null,
    val time: String? = null,
    val cautions: List<String>? = emptyList(),
    val dietLabels: List<String>? = emptyList(),
    val healthLabels: List<String>? = emptyList(),
    val summary: SummaryResponse? = null
) {
    data class SummaryResponse(
        val name: String? = null,
        val type: FoodType? = null,
        val nutrients: Map<String, Float?>? = null,
    )
}
