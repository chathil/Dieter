package com.example.dieter

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "temporary-id")
const val TEMPORARY_ID = "TEMPORARY_ID"

@HiltViewModel
class DieterAppViewModel @Inject constructor(
    private val dieterRepository: DieterRepository,
) : ViewModel() {
    private val temporaryIdKey = stringPreferencesKey(TEMPORARY_ID)

    private val _temporaryIdState = MutableStateFlow<DataState<String>>(DataState.Empty)

    val temporaryIdState: StateFlow<DataState<String>>
        get() = _temporaryIdState

    init {
        _temporaryIdState.value = DataState.Loading(null)
        viewModelScope.launch {
            // TODO: Remove bang operator
            temporaryId()!!.collect { token ->
                Log.d(TAG, "init: $token")
                if (token == null) {
                    val temporaryId = FirebaseIDGenerator.generateId()
                    temporaryId(temporaryId)
                    dieterRepository.temporaryId(temporaryId).collect {
                        when (it) {
                            is DataState.Success -> _temporaryIdState.value =
                                DataState.Success(temporaryId)
                            is DataState.Error -> _temporaryIdState.value = DataState.Error(it.exception)
                            else -> {}
                        }
                    }
                } else {
                    _temporaryIdState.value =
                        DataState.Success(token)
                }

            }
        }
    }

    private suspend fun temporaryId(id: String) {
        DieterApplication.applicationContext()?.dataStore?.edit { values ->
            values[temporaryIdKey] = id
        }
    }

    private fun temporaryId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[temporaryIdKey]
        }

    companion object {
        private val TAG = DieterAppViewModel::class.java.simpleName
    }
}