package com.example.dieter

import android.net.Uri
import android.util.Log
import com.example.dieter.data.source.domain.IngredientModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Shared state across screens
 */
class DieterAppState {
    private val _ingredientsState = MutableStateFlow(mapOf<IngredientModel, Int>())

    val ingredientsState: StateFlow<Map<IngredientModel, Int>>
        get() = _ingredientsState

    var photoUri: Uri? = null

    fun addIngredient(ingredient: IngredientModel) {
        if (_ingredientsState.value.containsKey(ingredient)) {
            var current = _ingredientsState.value[ingredient]!!
            current++
            removeIngredient(ingredient)
            _ingredientsState.value += Pair(ingredient, current)
        } else {
            _ingredientsState.value += Pair(ingredient, 1)
        }
        Log.d(TAG, "addIngredient: ${_ingredientsState.value}")
    }

    fun updatePortion(ingredient: IngredientModel, portion: Int) {
        removeIngredient(ingredient)
        _ingredientsState.value += Pair(ingredient, portion)
    }

    fun removeIngredient(ingredient: IngredientModel) {
        _ingredientsState.value -= ingredient
    }

    fun clearIngredient() {
        _ingredientsState.value = emptyMap()
    }

    companion object {
        private val TAG = DieterAppState::class.java.simpleName
    }
}
