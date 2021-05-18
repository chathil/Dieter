package com.example.dieter.data.source.domain

data class IngredientModel(
    val id: String,
    val label: String,
    val nutrients: Map<NutrientType, Float?>,
    val category: String,
    val categoryLabel: String,
    val measures: List<MeasureModel>,
    val image: String?
)
