package com.example.dieter.data.source.domain

import com.example.dieter.data.source.firebase.request.SaveFoodRequest

data class SaveFoodModel(
    val cautions: List<String>,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val summary: SaveSummaryModel,
    val ingredients: Map<IngredientModel, DetailIngredientModel>
) {
    data class SaveSummaryModel(
        val name: String,
        val type: FoodType,
        val nutrients: Map<NutrientType, Float?>,
    )
}

fun SaveFoodModel.asRequest(): SaveFoodRequest {
    val nuts = summary.nutrients.map { it.key.nutrientName to it.value }.toMap()
    val summary = SaveFoodRequest.SaveSummaryRequest(summary.name, summary.type, nuts)

    val ings = ingredients.map {
        SaveFoodRequest.SaveIngredientRequest(
            it.key.label,
            it.key.weight,
            it.value.totalNutrients.map { n -> n.key.nutrientName to n.value }.toMap()
        )
    }
    return SaveFoodRequest(
        cautions = cautions,
        dietLabels = dietLabels,
        healthLabels = healthLabels,
        summary = summary,
        ingredients = ings
    )
}
