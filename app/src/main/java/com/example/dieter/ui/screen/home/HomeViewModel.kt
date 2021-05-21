package com.example.dieter.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.FoodType
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
    edamamRepository: EdamamRepository
) : ViewModel() {
    
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
        TodaysFoodModel(FoodType.BREAKFAST, "Egg Sandwich", "fake_food.jpg", 15, Date(1619289713000)),
        TodaysFoodModel(FoodType.BRUNCH, "Egg Sandwich", "fake_food.jpg", 15, Date(1619203313000)),
        TodaysFoodModel(FoodType.LUNCH, "Egg Sandwich", "fake_food.jpg", 15, Date(1619203313000)),
        TodaysFoodModel(FoodType.DINNER, "Egg Sandwich", "fake_food.jpg", 15, Date(1618944113000)),
        TodaysFoodModel(FoodType.DRINK, "Egg Sandwich", "fake_food.jpg", 15, Date(1618944113000)),
    )

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}
