package com.example.dieter.data.source.remote

import javax.inject.Inject

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class EdamamRemoteDataSource @Inject constructor() {
    suspend fun searchIngredient(ingredient: String) =
        EdamamApi.appPrimaryService.searchIngredient(ingredient = ingredient)
}
