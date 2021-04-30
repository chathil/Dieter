package com.example.dieter.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.NutrientModel
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
        BodyWeightModel(40, 70, Date(1618512113000)),
        BodyWeightModel(40, 70, Date(1618598513000)),
        BodyWeightModel(40, 70, Date(1618684913000)),
        BodyWeightModel(40, 70, Date(1618771313000)),
        BodyWeightModel(40, 70, Date(1618944113000)),
        BodyWeightModel(40, 70, Date(1619030513000)),
        BodyWeightModel(40, 70, Date(1619116913000)),
        BodyWeightModel(40, 70, Date(1619203313000)),
        BodyWeightModel(40, 70, Date(1619289713000))
    )

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}
