package com.example.dieter.data.source

import android.util.Log
import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.data.source.domain.asRequest
import com.example.dieter.data.source.firebase.DieterFirebaseAuth
import com.example.dieter.data.source.firebase.DieterRealtimeDatabase
import com.example.dieter.data.source.firebase.response.asDomainModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DieterRepository @Inject constructor(
    private val dieterFirebaseAuth: DieterFirebaseAuth,
    private val realtimeDatabase: DieterRealtimeDatabase
) : DieterDataSource {

    override fun authWithGoogle(idToken: String) =
        dieterFirebaseAuth.authWithGoogle(idToken)

    override fun setGoal(setGoalModel: SetGoalModel) =
        realtimeDatabase.setGoal(setGoalModel.userRepId, setGoalModel.asRequest())

    override fun goal(userRepId: String) = realtimeDatabase.goal(userRepId).map {
        Log.d(TAG, "goal: $it")
        when (it) {
            is DataState.Success -> {
                if (it.data.target == null)
                    DataState.Loading(null)
                else
                    DataState.Success(it.data.asDomainModel())
            }
            is DataState.Error -> DataState.Error(it.exception)
            is DataState.Loading -> DataState.Loading(null)
            is DataState.Empty -> DataState.Empty
        }
    }

    override fun temporaryId(token: String) = realtimeDatabase.temporaryId(token)
    override fun linkUserDevice(userId: String, temporaryId: String) =
        realtimeDatabase.linkUserDevice(userId, temporaryId)

    companion object {
        private val TAG = DieterRepository::class.java.simpleName
    }
}
