package com.example.dieter.ui.screen.account

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.dataStore
import com.example.dieter.vo.DataState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val dieterRepository: DieterRepository) :
    ViewModel() {

    private val userRepKey = stringPreferencesKey(USER_REP_ID)
    private val _goal = MutableStateFlow<DataState<GoalModel?>>(DataState.Empty)
    val goal: StateFlow<DataState<GoalModel?>>
        get() = _goal

    init {
        viewModelScope.launch {
            userRepId()!!.collect { token ->
                goal(token!!)
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

    fun signOut() {
        // TODO: this won't work properly until we can check if the user already have a rep id.
        Firebase.auth.signOut()
    }

    private fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    companion object {
        val TAG = AccountViewModel::class.java.simpleName
    }
}
