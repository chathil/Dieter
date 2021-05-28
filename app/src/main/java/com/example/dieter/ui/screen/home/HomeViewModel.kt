package com.example.dieter.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.NutrientType
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

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean>
        get() = _error

    private val _goal = MutableStateFlow<DataState<GoalModel?>>(DataState.Empty)
    val goal: StateFlow<DataState<GoalModel?>>
        get() = _goal

    private val _nutrients =
        MutableStateFlow<List<NutrientModel>>(emptyList())
    val nutrients: StateFlow<List<NutrientModel>>
        get() = _nutrients

    private val _todaysFood = MutableStateFlow<List<TodaysFoodModel>>(emptyList())
    val todaysFood: StateFlow<List<TodaysFoodModel>>
        get() = _todaysFood

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

    fun todayNutrient(userRepId: String) {
        val mapped = NutrientType.values().map { it.nutrientName to it }.toMap()
        viewModelScope.launch {
            dieterRepository.todayNutrient(userRepId).collect {
                when (it) {
                    is DataState.Success -> {
                        _nutrients.value = it.data.map { (k, v) ->
                            NutrientModel(
                                k,
                                v,
                                mapped[k]?.maxValue ?: 0,
                                mapped[k]?.nutrientUnit ?: "?"
                            )
                        }
                    }
                }
            }
        }
    }

    fun todayFood(userRepId: String) {
        viewModelScope.launch {
            dieterRepository.todayFood(userRepId).collect {
                when (it) {
                    is DataState.Success -> {
                        _todaysFood.value = it.data
                    }
                }
            }
        }
    }

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
