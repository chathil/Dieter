package com.example.dieter

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.utils.FirebaseIDGenerator
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "init")
const val USER_REP_ID = "USER_REP_ID"
const val IS_WELCOME_SHOWN = "IS_WELCOME_SHOW"

@HiltViewModel
class DieterAppViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
) : ViewModel() {
    private val userRepKey = stringPreferencesKey(USER_REP_ID)
    private val isWelcomeShownKey = booleanPreferencesKey(IS_WELCOME_SHOWN)

    private val _userRepIdState = MutableStateFlow<DataState<String>>(DataState.Empty)

    val userRepIdState: StateFlow<DataState<String>>
        get() = _userRepIdState

    private val _isWelcomeShownState = MutableStateFlow(false)
    val isWelcomeShownState: StateFlow<Boolean>
        get() = _isWelcomeShownState

    init {
        _userRepIdState.value = DataState.Loading(null)

        viewModelScope.launch {
            // TODO: Remove bang operator
            userRepId()!!.collect { token ->
                if (token == null) {
                    val temporaryId = FirebaseIDGenerator.generateId()
                    userRepId(temporaryId)
                    dieterRepository.temporaryId(temporaryId).collect {
                        when (it) {
                            is DataState.Success ->
                                _userRepIdState.value =
                                    DataState.Success(temporaryId)
                            is DataState.Error ->
                                _userRepIdState.value =
                                    DataState.Error(it.exception)
                            else -> {
                            }
                        }
                    }
                } else {
                    _userRepIdState.value =
                        DataState.Success(token)
                }
            }
        }
        viewModelScope.launch {
            isWelcomeShown()!!.collect {
                Log.d(TAG, "init of app: $it")
                _isWelcomeShownState.value = it ?: false
            }
        }
    }

    private suspend fun userRepId(id: String) {
        DieterApplication.applicationContext()?.dataStore?.edit { values ->
            values[userRepKey] = id
        }
    }

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    fun isWelcomeShown(isIt: Boolean) {
        Log.d(TAG, "isWelcomeShown: $isIt")
        viewModelScope.launch {
            DieterApplication.applicationContext()?.dataStore?.edit { values ->
                values[isWelcomeShownKey] = isIt
            }
        }
    }

    private fun isWelcomeShown() =
        DieterApplication.applicationContext()?.dataStore?.data?.map { preferences -> preferences[isWelcomeShownKey] }

    companion object {
        private val TAG = DieterAppViewModel::class.java.simpleName
    }
}
