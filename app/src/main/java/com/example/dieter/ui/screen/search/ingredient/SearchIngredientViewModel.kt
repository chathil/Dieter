package com.example.dieter.ui.screen.search.ingredient

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.vo.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class SearchIngredientViewModel @Inject constructor(
    private val edamamRepository: EdamamRepository
) : ViewModel() {
    fun searchIngredient(name: String): Flow<DataState<List<IngredientModel>>> {
        return emptyFlow()
    }
}
