package com.example.dieter.data.source

import android.net.Uri
import androidx.core.net.toFile
import com.example.dieter.application.DieterApplication
import com.example.dieter.data.source.remote.DieterAiRemoteDataSource
import com.example.dieter.data.source.remote.response.asDomainModel
import com.example.dieter.vo.DataState
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class DieterAiRepository @Inject constructor(
    private val remoteDataSource: DieterAiRemoteDataSource
) {
    fun predict(data: Uri) = flow {
        emit(DataState.Loading(null))
        try {
            // val byteData = data.toFile().readBytes()
            val compressedFile = Compressor.compress(DieterApplication.applicationContext()!!, data.toFile()) {
                default(width = 320)
            }.readBytes()
            val body = RequestBody.create(MediaType.parse("image/jpg"), compressedFile)
            val result = remoteDataSource.predict(body).asDomainModel()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: "Unknown Error"))
        }
    }

    companion object {
        private val TAG = DieterAiRepository::class.java.simpleName
    }
}
