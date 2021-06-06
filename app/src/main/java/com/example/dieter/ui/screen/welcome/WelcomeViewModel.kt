package com.example.dieter.ui.screen.welcome

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dieter.USER_REP_ID
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dieterRepository: DieterRepository
) : ViewModel() {

    private val userRepKey = stringPreferencesKey(USER_REP_ID)

    fun authWithGoogle(idToken: String) =
        dieterRepository.authWithGoogle(idToken)

    fun linkUserDevice(userId: String, userRepId: String) = dieterRepository.linkUserDevice(userId, userRepId)

    suspend fun userRepId(id: String) {
        DieterApplication.applicationContext()?.dataStore?.edit { values ->
            values[userRepKey] = id
        }
    }

    fun userRepId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[userRepKey]
        }

    companion object {
        private val TAG = WelcomeViewModel::class.java.simpleName
    }
}
