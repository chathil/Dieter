package com.example.dieter.data.source.remote.response

import com.example.dieter.data.source.domain.NutrientType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponse(
    @SerialName("foodId")
    val id: String,
    val label: String,
    val nutrients: NutrientSnippet,
    val category: String,
    val categoryLabel: String,
    val image: String? = null
) {
    @Serializable
    data class NutrientSnippet(
        @SerialName("ENERC_KCAL")
        val calorie: Float?,
        @SerialName("PROCNT")
        val protein: Float?,
        @SerialName("FAT")
        val fat: Float? = null,
        @SerialName("CHOCDF")
        val carbs: Float?,
        @SerialName("FIBTG")
        val fiber: Float? = null
    )
}

fun IngredientResponse.NutrientSnippet.asDomainModel() = mapOf(
    NutrientType.ENERC_KCAL to calorie,
    NutrientType.PROCNT to protein,
    NutrientType.FAT to fat,
    NutrientType.CHOCDF to carbs,
    NutrientType.FIBTG to fiber
)
