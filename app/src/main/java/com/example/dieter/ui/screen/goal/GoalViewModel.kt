package com.example.dieter.ui.screen.goal

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.GoalType
import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.dataStore
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val dieterRepository: DieterRepository
) : ViewModel() {

    private val userRepKey = stringPreferencesKey(USER_REP_ID)
    private val _saveGoalState = MutableStateFlow<DataState<Boolean>>(DataState.Empty)
    val saveGoalState: StateFlow<DataState<Boolean>>
        get() = _saveGoalState

    private var userRepId = ""

    init {
        viewModelScope.launch {
            userRepId()!!.collect { token ->
                userRepId = token!!
            }
        }
    }

    /**
     * Save to firebase
     */
    fun save(selectedGoal: GoalType, age: Int, isMale: Boolean, height: Int, weight: Int, targetWeight: Int) {
        viewModelScope.launch {
            dieterRepository.setGoal(SetGoalModel(userRepId, selectedGoal, age, isMale, height, weight, targetWeight))
                .collect {
                    _saveGoalState.value = it
                }
        }
    }

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    companion object {
        private val TAG = GoalViewModel::class.java.simpleName
    }
}
