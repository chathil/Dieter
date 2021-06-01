package com.example.dieter.data.source.domain

import com.example.dieter.data.source.remote.request.NutrientRequest

data class IngredientModel(
    val id: String,
    val label: String,
    val nutrients: Map<NutrientType, Float?>,
    val category: String,
    val categoryLabel: String,
    val measures: List<MeasureModel>,
    val image: String?
)

fun IngredientModel.asRequest(quantity: Int): NutrientRequest {
    val ing = NutrientRequest.Ingredient(quantity = quantity, foodId = id)
    return NutrientRequest(listOf(ing))
}
