package com.example.dieter.data.source

import com.example.dieter.data.source.domain.IngredientModel
import com.example.dieter.data.source.domain.asRequest
import com.example.dieter.data.source.local.EdamamLocalDataSource
import com.example.dieter.data.source.remote.EdamamRemoteDataSource
import com.example.dieter.data.source.remote.response.asDomainModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EdamamRepository @Inject constructor(
    private val edamamLocalDataSource: EdamamLocalDataSource,
    private val edamamRemoteDataSource: EdamamRemoteDataSource
) : EdamamDataSource {
    override fun searchIngredient(ingredient: String) =
        flow {
            emit(DataState.Loading(null))
            try {
                val result = edamamRemoteDataSource.searchIngredient(ingredient)
                emit(DataState.Success(result.asDomainModel()))
            } catch (e: Exception) {
                emit(DataState.Error(e.message ?: "Unknown Error"))
            }
        }

    override fun ingredientDetail(ingredient: IngredientModel, quantity: Int) = flow {
        emit(Pair(ingredient, DataState.Loading(null)))
        try {
            val result = edamamRemoteDataSource.ingredientDetail(ingredient.asRequest(quantity))
            emit(Pair(ingredient, DataState.Success(result.asDomainModel())))
        } catch (e: Exception) {
            emit(Pair(ingredient, DataState.Error(e.message ?: "Unknown Error")))
        }
    }
    // emptyFlow<Pair<IngredientModel, DataState<DetailIngredientModel>>>()
}
