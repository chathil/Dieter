package com.example.dieter.data.source.domain

data class IngredientModel(
    val id: String,
    val label: String,
    val nutrients: NutrientSnippet,
    val category: String,
    val categoryLabel: String,
    val measures: List<MeasureModel>,
    val image: String?
) {
    data class NutrientSnippet(
        val calorie: Float?,
        val protein: Float?,
        val fat: Float?,
        val carbs: Float?,
        val fiber: Float?
    )
}
