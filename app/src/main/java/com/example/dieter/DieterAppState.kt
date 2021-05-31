package com.example.dieter

import android.net.Uri
import android.util.Log
import com.example.dieter.data.source.domain.ActiveCounterModel
import com.example.dieter.data.source.domain.IngredientModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Shared state across screens
 */
class DieterAppState {

    private val _activeCounterState = MutableStateFlow<ActiveCounterModel?>(null)
    val activeCounterState: StateFlow<ActiveCounterModel?>
        get() = _activeCounterState

    private val _ingredientsState = MutableStateFlow(mapOf<IngredientModel, Int>())

    val ingredientsState: StateFlow<Map<IngredientModel, Int>>
        get() = _ingredientsState

    private val _workoutDone = MutableStateFlow(false)
    val workoutDone: StateFlow<Boolean>
        get() = _workoutDone

    fun workoutDone() {
        _workoutDone.value = true
    }

    var photoUri: Uri? = null

    fun updateCounter(counter: ActiveCounterModel) {
        _activeCounterState.value = counter
    }

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
