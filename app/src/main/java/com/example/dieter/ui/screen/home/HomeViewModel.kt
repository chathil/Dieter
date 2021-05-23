package com.example.dieter.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
    edamamRepository: EdamamRepository
) : ViewModel() {

    init {
    }

    private val _goal = MutableStateFlow<DataState<GoalModel?>>(DataState.Empty)
    val goal: StateFlow<DataState<GoalModel?>>
        get() = _goal

    val nutrients = listOf(
        NutrientModel("Calorie", 1437, 2000, "kcal"),
        NutrientModel("Carbs", 27, 19, "g"),
        NutrientModel("Fat", 0, 19, "g"),
        NutrientModel("Fibers", 10, 40, "g"),
        NutrientModel("Protein", 2, 10, "g"),
        NutrientModel("Nutrient 1", 6, 10, "g"),
        NutrientModel("Extra Fat", 1, 10, "g"),
        NutrientModel("Lorem", 3, 10, "g"),
        NutrientModel("Ipsum", 8, 10, "g")
    )

    val bodyWeightEntries = listOf(
        BodyWeightModel(56, 70, Date(1618512113000)),
        BodyWeightModel(60, 70, Date(1618598513000)),
        BodyWeightModel(61, 70, Date(1618684913000)),
        BodyWeightModel(67, 70, Date(1618771313000)),
        BodyWeightModel(70, 70, Date(1618944113000)),
        BodyWeightModel(70, 70, Date(1619030513000)),
        BodyWeightModel(70, 70, Date(1619116913000)),
        BodyWeightModel(70, 70, Date(1619203313000)),
        BodyWeightModel(69, 70, Date(1619289713000))
    )

    val todaysFoods = listOf(
        TodaysFoodModel(
            FoodType.BREAKFAST,
            "Egg Sandwich",
            "fake_food.jpg",
            15,
            Date(1619289713000)
        ),
        TodaysFoodModel(FoodType.BRUNCH, "Egg Sandwich", "fake_food.jpg", 15, Date(1619203313000)),
        TodaysFoodModel(FoodType.LUNCH, "Egg Sandwich", "fake_food.jpg", 15, Date(1619203313000)),
        TodaysFoodModel(FoodType.DINNER, "Egg Sandwich", "fake_food.jpg", 15, Date(1618944113000)),
        TodaysFoodModel(FoodType.DRINK, "Egg Sandwich", "fake_food.jpg", 15, Date(1618944113000)),
    )

    fun goal(userRepId: String) {
        viewModelScope.launch {
            dieterRepository.goal(userRepId).collect {
                _goal.value = it
            }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}
