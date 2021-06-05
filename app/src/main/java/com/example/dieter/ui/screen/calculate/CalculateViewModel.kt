package com.example.dieter.ui.screen.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.DetailIngredientModel
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.data.source.domain.NutrientType
import com.example.dieter.data.source.domain.SaveFoodModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel @Inject constructor(
    private val edamamRepository: EdamamRepository,
    private val dieterRepository: DieterRepository,
) : ViewModel() {

    private val _summaryState = MutableStateFlow<Map<NutrientType, Float?>>(emptyMap())

    val summaryState: StateFlow<Map<NutrientType, Float?>>
        get() = _summaryState

    private val _state =
        MutableStateFlow<Map<IngredientModel, DataState<DetailIngredientModel>>>(emptyMap())

    val state: StateFlow<Map<IngredientModel, DataState<DetailIngredientModel>>>
        get() = _state

    private val _saveFoodState = MutableStateFlow<DataState<Boolean>>(DataState.Loading(null))
    val saveFoodState: StateFlow<DataState<Boolean>>
        get() = _saveFoodState

    /**
     * Add summary to firebase
     */
    fun saveToFirebase(userRepId: String, name: String, type: FoodType) {
        val summary = SaveFoodModel.SaveSummaryModel(name, type, summaryState.value)
        val cautions = emptySet<String>().toMutableSet()
        val dietLabels = emptySet<String>().toMutableSet()
        val healthLabels = emptySet<String>().toMutableSet()

        val res = _state.value.map { (t, u) ->
            if (u is DataState.Success) {
                cautions.addAll(u.data.cautions)
                dietLabels.addAll(u.data.dietLabels)
                healthLabels.addAll(u.data.healthLabels)
                t to u.data
            } else {
                t to null
            }
        }.toMap()

        val saveFood = SaveFoodModel(
            cautions.toList(),
            dietLabels.toList(),
            healthLabels.toList(),
            summary,
            res
        )
        viewModelScope.launch {
            dieterRepository.saveFood(userRepId, saveFood).collect {
                _saveFoodState.value = it
            }
        }
    }

    /**
     * Sum everything and save it to summary state
     */
    private fun review() {
        _summaryState.value = emptyMap()
        _state.value.forEach { (_, u) ->
            if (u is DataState.Success) {
                u.data.totalNutrients.forEach { (v, w) ->
                    if (_summaryState.value.containsKey(v)) {
                        val current = _summaryState.value[v]!!
                        _summaryState.value += Pair(v, current + (w ?: 0f))
                    } else {
                        _summaryState.value += Pair(v, w)
                    }
                }
            }
        }
    }

    fun details(ingredients: Map<IngredientModel, Int>) {
        ingredients.forEach {
            viewModelScope.launch {
                edamamRepository.ingredientDetail(it.key, it.value).collect {
                    _state.value += it
                    if (_state.value.size == ingredients.size) {
                        review()
                    }
                }
            }
        }
    }

    companion object {
        val TAG = CalculateViewModel::class.java.simpleName
    }
}
