package com.example.dieter.ui.screen.add.ingredients

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.DieterAiRepository
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.DetectedObjectModel
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddIngredientsViewModel @Inject constructor(
    private val dieterAiRepository: DieterAiRepository,
    private val edamamRepository: EdamamRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Set<IngredientModel>>(emptySet())

    val state: StateFlow<Set<IngredientModel>>
        get() = _state

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean>
        get() = _loading

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean>
        get() = _error

    // private val _detectState = MutableStateFlow<List<DetectedObjectModel>>(emptyList())
    // val detectState: StateFlow<List<DetectedObjectModel>>
    //     get() = _detectState

    fun predict(data: Uri) {
        _loading.value = true
        viewModelScope.launch {
            dieterAiRepository.predict(data).collect {
                when (it) {
                    is DataState.Success -> {
                        it.data.forEach { d ->
                            searchIngredient(d.label)
                        }
                    }
                    is DataState.Loading -> {

                    }
                    else -> {
                        _error.value = true
                        Log.e(TAG, "predict: $it", )
                    }
                }
            }
        }
    }

    fun searchIngredient(name: String) {
        viewModelScope.launch {
            edamamRepository.searchIngredient(name).collect {
                _loading.value = false
                when (it) {
                    is DataState.Success -> {
                        it.data.firstOrNull()?.let { d ->
                            _state.value += d
                        }
                    }
                    is DataState.Loading -> {
                    }
                    else -> {
                        _error.value = true
                        Log.e(TAG, "searchIngredient: $it", )
                    }
                }
            }
        }
    }

    companion object {
        val TAG = AddIngredientsViewModel::class.java.simpleName
    }
}
