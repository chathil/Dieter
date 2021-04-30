package com.example.dieter.vo

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: String) : DataState<Nothing>()
    data class Loading<T>(val data: T?) : DataState<T>()
    object Empty : DataState<Nothing>()
}
