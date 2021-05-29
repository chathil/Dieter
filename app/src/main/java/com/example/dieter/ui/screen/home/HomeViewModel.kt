package com.example.dieter.ui.screen.home

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.NutrientType
import com.example.dieter.data.source.domain.SetBodyWeightModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.dataStore
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
    edamamRepository: EdamamRepository
) : ViewModel() {
    private val userRepKey = stringPreferencesKey(USER_REP_ID)

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

    private val _bodyWeightEntries = MutableStateFlow<List<BodyWeightModel>>(emptyList())

    val bodyWeightEntries: StateFlow<List<BodyWeightModel>>
        get() = _bodyWeightEntries
    init {
        viewModelScope.launch {
            userRepId()!!.collect { token ->
                goal(token!!)
                todayNutrient(token!!)
                todayFood(token!!)
                bodyWeights(token!!)
            }
        }
    }
    fun todayNutrient(userRepId: String) {
        val nowString = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.UK
        ).format(java.util.Date(System.currentTimeMillis()))
        val mapped = NutrientType.values().map { it.nutrientName to it }.toMap()
        viewModelScope.launch {
            dieterRepository.todayNutrient(userRepId, nowString).collect {
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
        val nowString = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.UK
        ).format(java.util.Date(System.currentTimeMillis()))
        viewModelScope.launch {
            dieterRepository.todayFood(userRepId, nowString).collect {
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

    fun newBodyWeight(userRepId: String, data: SetBodyWeightModel) {
        viewModelScope.launch {
            dieterRepository.newBodyWeight(userRepId, data).collect {
                Log.d(TAG, "newBodyWeight: $it")
                when (it) {
                    is DataState.Success -> bodyWeights(userRepId)
                }
            }
        }
    }

    fun bodyWeights(userRepId: String) {
        viewModelScope.launch {
            dieterRepository.bodyWeights(userRepId).collect {
                when (it) {
                    is DataState.Success -> _bodyWeightEntries.value = it.data
                }
            }
        }
    }

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
}
