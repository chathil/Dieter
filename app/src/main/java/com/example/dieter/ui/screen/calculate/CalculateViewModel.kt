package com.example.dieter.ui.screen.calculate

import android.util.Log
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

    private val fakeSummary = mapOf(
        NutrientType.ENERC_KCAL to 100f,
        NutrientType.CHOCDF to 5f,
        NutrientType.FAT to 5f,
        NutrientType.FIBTG to 10f,
        NutrientType.PROCNT to 10f
    )

    private val fakeIngredientModel = IngredientModel(
        id = "01ff", label = "Broccoli",
        weight = 56.0f,
        nutrients = mapOf(
            NutrientType.ENERC_KCAL to 9f,
            NutrientType.FIBTG to 10f,
            NutrientType.CA to 2f,
            NutrientType.FAT to 39f,
            NutrientType.P to 45f
        ),
        category = "Meal", categoryLabel = "Meal Label", measures = emptyList(), image = null
    )
    private val fakeDetailSuccess =
        DataState.Success(DetailIngredientModel(listOf(), listOf(), listOf(), fakeSummary))
    private val fakeEachIngredient = mapOf(fakeIngredientModel to fakeDetailSuccess)

    private val _summaryState = MutableStateFlow<Map<NutrientType, Float?>>(fakeSummary)

    val summaryState: StateFlow<Map<NutrientType, Float?>>
        get() = _summaryState

    private val _state = MutableStateFlow<Map<IngredientModel, DataState<DetailIngredientModel>>>(
        fakeEachIngredient
    )

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
        val saveFood = SaveFoodModel(
            emptyList(),
            emptyList(),
            emptyList(),
            summary,
            fakeEachIngredient.map { (k, v) -> k to v.data }.toMap()
        )
        // forward to firebase
        viewModelScope.launch {
            dieterRepository.saveFood(userRepId, saveFood).collect {
                Log.d(TAG, "saveToFirebase: $it")
                _saveFoodState.value = it
            }
        }
    }

    /**
     * Sum everything and save it to summary state
     */
    private fun review() {
    }

    fun details(ingredients: List<IngredientModel>) {
        ingredients.forEach {
            viewModelScope.launch {
                edamamRepository.ingredientDetail(it).collect {
                    _state.value += it
                    /**
                     * Possible problem
                     * Every value is going to be initialized by empty DataState causing
                     * review to calculate too early, even though it will re-calculate later
                     * */
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
