package com.example.dieter.data.source

import com.example.dieter.data.source.local.EdamamLocalDataSource
import com.example.dieter.data.source.remote.EdamamRemoteDataSource
import javax.inject.Inject

class EdamamRepository @Inject constructor(
    private val edamamLocalDataSource: EdamamLocalDataSource,
    private val edamamRemoteDataSource: EdamamRemoteDataSource
) : EdamamDataSource
