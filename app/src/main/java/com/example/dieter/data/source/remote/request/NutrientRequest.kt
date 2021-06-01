package com.example.dieter.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class NutrientRequest(
    val ingredients: List<Ingredient>
) {
    @Serializable
    data class Ingredient(
        val quantity: Int,
        val measureURI: String = "http://www.edamam.com/ontologies/edamam.owl#Measure_gram",
        val foodId: String
    )
}
