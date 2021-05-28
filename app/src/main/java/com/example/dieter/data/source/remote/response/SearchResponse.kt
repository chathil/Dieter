package com.example.dieter.data.source.remote.response

import com.example.dieter.data.source.domain.IngredientModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("hints")
    val responses: List<IngredientResponseWithMeasures>
)

@Serializable
data class IngredientResponseWithMeasures(
    @SerialName("food")
    val ingredients: IngredientResponse,
    @SerialName("measures")
    val measures: List<MeasureResponse>
)

fun SearchResponse.asDomainModel() =
    responses.map {
        IngredientModel(
            id = it.ingredients.id,
            label = it.ingredients.label,
            weight = 9f, // TODO: Fix fake weight in search response
            nutrients = it.ingredients.nutrients.asDomainModel(),
            category = it.ingredients.category,
            categoryLabel = it.ingredients.categoryLabel,
            measures = it.measures.asDomainModels(),
            image = it.ingredients.image
        )
    }
