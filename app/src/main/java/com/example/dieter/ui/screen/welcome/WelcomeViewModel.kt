package com.example.dieter.ui.screen.welcome

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.DieterRepository
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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
