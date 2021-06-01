package com.example.dieter.data.source.firebase.request

import com.example.dieter.data.source.domain.FoodType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SaveFoodRequest(
    val addedAt: Long = System.currentTimeMillis(),
    val date: String = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.UK
    ).format(Date(addedAt)), // get string date from addedAt
    val time: String = SimpleDateFormat(
        "HH:mm",
        Locale.UK
    ).format(Date(addedAt)), // get string time from addedAt
    val cautions: List<String>,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val summary: SaveSummaryRequest,
    val ingredients: List<SaveIngredientRequest>?
) {
    data class SaveSummaryRequest(
        val name: String,
        val type: FoodType,
        val nutrients: Map<String, Float?>,
    )

    data class SaveIngredientRequest(
        val name: String,
        val nutrients: Map<String, Float?>
    )
}
