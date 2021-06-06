package com.example.dieter.ui.screen.history

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.NutrientModel
import com.example.dieter.data.source.domain.NutrientType
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
class HistoryViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
) : ViewModel() {
    private val userRepKey = stringPreferencesKey(USER_REP_ID)

    private val _nutrients =
        MutableStateFlow<List<NutrientModel>>(emptyList())
    val nutrients: StateFlow<List<NutrientModel>>
        get() = _nutrients

    private val _todaysFood = MutableStateFlow<List<TodaysFoodModel>>(emptyList())
    val todaysFood: StateFlow<List<TodaysFoodModel>>
        get() = _todaysFood

    private var userRepId = ""

    init {
        viewModelScope.launch {
            val nowString = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.UK
            ).format(java.util.Date(System.currentTimeMillis()))
            userRepId()!!.collect { token ->
                userRepId = token!!
                todayNutrient(nowString)
                todayFood(nowString)
            }
        }
    }

    fun todayFood(date: String) {
        viewModelScope.launch {
            dieterRepository.todayFood(userRepId, date).collect {
                when (it) {
                    is DataState.Success -> {
                        _todaysFood.value = it.data
                    }
                }
            }
        }
    }

    fun todayNutrient(date: String) {
        val mapped = NutrientType.values().map { it.nutrientName to it }.toMap()
        viewModelScope.launch {
            dieterRepository.todayNutrient(userRepId, date).collect {
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

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }
}
