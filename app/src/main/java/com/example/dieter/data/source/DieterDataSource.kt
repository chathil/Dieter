package com.example.dieter.data.source

import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.SaveFoodModel
import com.example.dieter.data.source.domain.SetGoalModel
import com.example.dieter.data.source.domain.TodaysFoodModel
import com.example.dieter.vo.DataState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface DieterDataSource {
    fun authWithGoogle(idToken: String): Flow<DataState<FirebaseUser>>
    fun setGoal(setGoalModel: SetGoalModel): Flow<DataState<Boolean>>
    fun goal(userRepId: String): Flow<DataState<GoalModel?>>
    fun temporaryId(token: String): Flow<DataState<Boolean>>
    fun linkUserDevice(userId: String, temporaryId: String): Flow<DataState<Boolean>>
    fun saveFood(userRepId: String, food: SaveFoodModel): Flow<DataState<Boolean>>
    fun todayNutrient(userRepId: String): Flow<DataState<Map<String, Float>>>
    fun todayFood(userRepId: String): Flow<DataState<List<TodaysFoodModel>>>
}
