package com.example.dieter

import androidx.compose.runtime.mutableStateOf
import com.example.dieter.data.source.domain.IngredientModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Shared state across screens
 */
class DieterAppState {
    val ingredientsToAdd = mutableStateOf(listOf<IngredientModel>())
    private val _ingredientsState = MutableStateFlow(listOf<IngredientModel>())

    val ingredientsState: StateFlow<List<IngredientModel>>
        get() = _ingredientsState

    fun addIngredient(ingredient: IngredientModel) {
        _ingredientsState.value += ingredient
    }
    fun removeIngredient(ingredient: IngredientModel) {
        _ingredientsState.value -= ingredient
    }
}
