package com.example.dieter.data.source.remote

import okhttp3.RequestBody
import javax.inject.Inject

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class DieterAiRemoteDataSource @Inject constructor() {
    suspend fun predict(data: RequestBody) = DieterAiApi.appAiService.predict(data)
}
