package com.example.dieter.ui.screen.welcome

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dieter.TEMPORARY_ID
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

    private val temporaryIdKey = stringPreferencesKey(TEMPORARY_ID)


    fun authWithGoogle(idToken: String) =
        dieterRepository.authWithGoogle(idToken)

    fun linkUserDevice(userId: String, temporaryId: String) = dieterRepository.linkUserDevice(userId, temporaryId)

    private fun temporaryId() = DieterApplication.applicationContext()?.dataStore?.data
        ?.map { preferences ->
            preferences[temporaryIdKey]
        }

    companion object {
        private val TAG = WelcomeViewModel::class.java.simpleName
    }
}
