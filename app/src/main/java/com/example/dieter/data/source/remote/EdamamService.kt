package com.example.dieter.data.source.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

val contentType = MediaType.parse("application/json")!!
const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
    .build()

interface EdamamService

object EdamamApi {
    @ExperimentalSerializationApi
    val appPrimaryService: EdamamService by lazy {
        retrofit.create(
            EdamamService::class.java
        )
    }
}
