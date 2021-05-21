package com.example.dieter.data.source

import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.data.source.firebase.DieterFirebaseAuth
import com.example.dieter.data.source.firebase.DieterRealtimeDatabase
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class DieterRepository @Inject constructor(
    private val dieterFirebaseAuth: DieterFirebaseAuth,
    private val realtimeDatabase: DieterRealtimeDatabase
) : DieterDataSource {

    override fun authWithGoogle(idToken: String) =
        dieterFirebaseAuth.authWithGoogle(idToken)

    override fun setGoal(setGoalModel: SetGoalModel): Flow<DataState<Boolean>> {
       return emptyFlow()
    }

    override fun temporaryId(token: String) = realtimeDatabase.temporaryId(token)
    override fun linkUserDevice(userId: String, temporaryId: String) = realtimeDatabase.linkUserDevice(userId, temporaryId)

    companion object {
        private val TAG = DieterRepository::class.java.simpleName
    }
}
