package com.example.dieter.data.source

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
}
