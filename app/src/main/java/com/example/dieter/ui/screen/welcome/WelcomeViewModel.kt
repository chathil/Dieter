package com.example.dieter.ui.screen.welcome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.DieterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dieterRepository: DieterRepository
) : ViewModel() {

    fun authWithGoogle(idToken: String) =
        dieterRepository.authWithGoogle(idToken)

    companion object {
        private val TAG = WelcomeViewModel::class.java.simpleName
    }
}
