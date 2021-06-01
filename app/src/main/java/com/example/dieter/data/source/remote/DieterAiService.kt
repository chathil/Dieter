package com.example.dieter.data.source.remote

import com.example.dieter.data.source.remote.response.DetectedObjectResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private val contentType = MediaType.parse("application/json")!!
private const val BASE_URL = "http://34.136.195.110:5000/"

@ExperimentalSerializationApi
private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
    .build()

interface DieterAiService {
    @Headers("Content-Type: application/json")
    @POST("predict")
    suspend fun predict(@Body data: RequestBody): List<DetectedObjectResponse>
}

object DieterAiApi {
    @ExperimentalSerializationApi
    val appAiService: DieterAiService by lazy {
        retrofit.create(
            DieterAiService::class.java
        )
    }
}