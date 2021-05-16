package com.example.dieter.data.source

import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.Flow

interface EdamamDataSource {
    fun searchIngredient(ingredient: String): Flow<DataState<List<IngredientModel>>>
}
