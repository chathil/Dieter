package com.example.dieter.data.source.domain

data class DetailIngredientModel(
    val cautions: List<String>,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val totalNutrients: Map<NutrientType, Float?>
)
