package com.example.dieter.data.source

import com.example.dieter.data.source.domain.BodyWeightModel
import com.example.dieter.data.source.domain.SaveFoodModel
import com.example.dieter.data.source.domain.SetBodyWeightModel
import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.data.source.domain.asRequest
import com.example.dieter.data.source.firebase.DieterFirebaseAuth
import com.example.dieter.data.source.firebase.DieterRealtimeDatabase
import com.example.dieter.data.source.firebase.response.asDomainModel
import com.example.dieter.vo.DataState
import kotlinx.coroutines.flow.map
import java.util.Date
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

    override fun saveFood(userRepId: String, food: SaveFoodModel) =
        realtimeDatabase.saveFood(userRepId, food.asRequest()).map {
            when (it) {
                is DataState.Success -> DataState.Success(it.data)
                is DataState.Error -> DataState.Error(it.exception)
                is DataState.Loading -> DataState.Loading(null)
                is DataState.Empty -> DataState.Empty
            }
        }

    override fun todayNutrient(userRepId: String, date: String) = realtimeDatabase.todayNutrient(userRepId, date)
    override fun todayFood(userRepId: String, date: String) = realtimeDatabase.todayFood(userRepId, date).map {
        when (it) {
            is DataState.Success -> {
                if (it.data.isNotEmpty()) {
                    val remap = it.data.map { data ->
                        TodaysFoodModel(
                            data.first,
                            data.second.summary!!.type!!,
                            data.second.summary!!.name!!,
                            null,
                            data.second.summary!!.nutrients!!["Energy"]?.toInt() ?: 0,
                            Date(data.second.addedAt!!)
                        )
                    }
                    DataState.Success(remap)
                } else {
                    DataState.Loading(null)
                }
            }
            is DataState.Error -> DataState.Error(it.exception)
            is DataState.Loading -> DataState.Loading(null)
            is DataState.Empty -> DataState.Empty
        }
    }

    override fun newBodyWeight(
        userRepId: String,
        setBodyWeightModel: SetBodyWeightModel
    ) = realtimeDatabase.newBodyWeight(userRepId, setBodyWeightModel.asRequest())

    override fun bodyWeights(userRepId: String) =
        realtimeDatabase.bodyWeights(userRepId).map { data ->
            when (data) {
                is DataState.Success -> {
                    if (data.data.firstOrNull()?.second?.addedAt == null)
                        DataState.Loading(null)
                    else
                        DataState.Success(
                            data.data.map {
                                BodyWeightModel(
                                    it.first,
                                    it.second.weight!!,
                                    it.second.target!!,
                                    Date(it.second.addedAt!!)
                                )
                            }
                        )
                }
                is DataState.Error -> DataState.Error(data.exception)
                is DataState.Loading -> DataState.Loading(null)
                is DataState.Empty -> DataState.Empty
            }
        }

    companion object {
        private val TAG = DieterRepository::class.java.simpleName
    }
}
