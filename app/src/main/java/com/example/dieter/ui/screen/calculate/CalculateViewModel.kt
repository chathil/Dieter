package com.example.dieter.ui.screen.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.DetailIngredientModel
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.data.source.domain.NutrientType
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
    private val dieterRepository: EdamamRepository,
) : ViewModel() {

    private val fakeSummary = mapOf(
        NutrientType.ENERC_KCAL to 100f,
        NutrientType.CHOCDF to 5f,
        NutrientType.FAT to 5f,
        NutrientType.FIBTG to 10f,
        NutrientType.PROCNT to 10f
    )

    private val fakeIngredientModel = IngredientModel(
        "01ff", "Broccoli",
        mapOf(
            NutrientType.ENERC_KCAL to 9f,
            NutrientType.FIBTG to 10f,
            NutrientType.CA to 2f,
            NutrientType.FAT to 39f,
            NutrientType.P to 45f
        ),
        "Meal", "Meal Label", emptyList(), null
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

    /**
     * Add summary to firebase
     */
    fun saveToFirebase() {}

    /**
     * Sum everything and save it to summary state
     */
    fun review() {
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
}
