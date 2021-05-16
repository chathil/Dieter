package com.example.dieter.ui.screen.search.ingredient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchIngredientViewModel @Inject constructor(
    private val edamamRepository: EdamamRepository
) : ViewModel() {
    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow<DataState<List<IngredientModel>>>(DataState.Empty)

    val state: StateFlow<DataState<List<IngredientModel>>>
        get() = _state

    init {
        searchIngredient("broccoli")
    }

    fun searchIngredient(name: String) {
        viewModelScope.launch {
            edamamRepository.searchIngredient(name).collect {
                _state.value = it
            }
        }
    }
}
