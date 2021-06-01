package com.example.dieter.data.source.remote

import com.example.dieter.data.source.remote.request.NutrientRequest
import javax.inject.Inject

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class EdamamRemoteDataSource @Inject constructor() {
    suspend fun searchIngredient(ingredient: String) =
        EdamamApi.appPrimaryService.searchIngredient(ingredient = ingredient)

    suspend fun ingredientDetail(request: NutrientRequest) =
        EdamamApi.appPrimaryService.ingredientDetail(body = request)
}
