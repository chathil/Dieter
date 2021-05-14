package com.example.dieter.ui.screen.search.ingredient

import androidx.lifecycle.ViewModel
import com.example.dieter.data.source.EdamamRepository
import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SearchIngredientViewModel(
    private val edamamRepository: EdamamRepository
) : ViewModel() {
    fun searchIngredient(name: String): Flow<DataState<List<IngredientModel>>> {
        return emptyFlow()
    }
}
