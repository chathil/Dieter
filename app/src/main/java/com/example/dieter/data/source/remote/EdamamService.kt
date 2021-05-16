package com.example.dieter.data.source.remote

import com.example.dieter.BuildConfig
import com.example.dieter.data.source.remote.response.SearchResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

val contentType = MediaType.parse("application/json")!!
const val API_KEY = BuildConfig.EDAMAM_API_KEY
const val APP_ID = BuildConfig.EDAMAM_APP_ID
const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
    .build()

interface EdamamService {
    @Headers("Content-Type: application/json")
    @GET("parser")
    suspend fun searchIngredient(
        @Query("app_id") appId: String = APP_ID,
        @Query("app_key") apiKey: String = API_KEY,
        @Query("ingr") ingredient: String
    ): SearchResponse
}

object EdamamApi {
    @ExperimentalSerializationApi
    val appPrimaryService: EdamamService by lazy {
        retrofit.create(
            EdamamService::class.java
        )
    }
}
