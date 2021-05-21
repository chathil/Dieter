package com.example.dieter.data.source

import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.FirebaseUser

interface DieterDataSource {
    fun authWithGoogle(idToken: String): Flow<DataState<FirebaseUser>>
    fun setGoal(setGoalModel: SetGoalModel): Flow<DataState<Boolean>>
    fun temporaryId(token: String): Flow<DataState<Boolean>>
    fun linkUserDevice(userId: String, temporaryId: String): Flow<DataState<Boolean>>
}
