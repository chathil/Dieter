package com.example.dieter.data.source.firebase

import android.util.Log
import com.example.dieter.BuildConfig
import com.example.dieter.data.source.firebase.request.BodyWeightRequest
import com.example.dieter.data.source.firebase.request.SaveFoodRequest
import com.example.dieter.data.source.firebase.request.SaveWorkoutRequest
import com.example.dieter.data.source.firebase.request.SetGoalRequest
import com.example.dieter.data.source.firebase.response.BodyWeightResponse
import com.example.dieter.data.source.firebase.response.BurnCalorieResponse
import com.example.dieter.data.source.firebase.response.FoodResponse
import com.example.dieter.data.source.firebase.response.GoalResponse
import com.example.dieter.utils.EmulatorHost
import com.example.dieter.vo.DataState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class FirebaseDatabaseModule {
    private val useEmulator = BuildConfig.EMULATE_SERVER

    @Provides
    fun provideFirebaseRootRef(): DatabaseReference {
        val database = Firebase.database
        if (useEmulator)
            database.useEmulator(EmulatorHost.ip, EmulatorHost.databasePort)
        return database.reference
    }
}

// https://firebase.google.com/docs/database/android/read-and-write
@OptIn(ExperimentalCoroutinesApi::class)
class DieterRealtimeDatabase @Inject constructor(
    private val rootRef: DatabaseReference
) {
    fun setGoal(userRepId: String, goal: SetGoalRequest) = callbackFlow {
        offer(DataState.Loading(null))
        rootRef.child("user_goals").child(userRepId).setValue(goal).addOnSuccessListener {
            if (!isClosedForSend)
                offer(DataState.Success(true))
            close()
        }.addOnFailureListener {
            if (!isClosedForSend)
                offer(DataState.Error(it.message!!))
            close(it)
        }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun temporaryId(id: String) = callbackFlow {
        offer(DataState.Loading(null))
        rootRef.child("users").child(id).setValue("")
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(DataState.Success(true))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(DataState.Error(it.message!!))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun linkUserDevice(userId: String, temporaryId: String) = callbackFlow {
        offer(DataState.Loading(null))
        rootRef.child("user_devices").child(userId).get().addOnSuccessListener {
            if (!isClosedForSend) {
                if (it.exists()) {
                    offer(DataState.Success(it.getValue<String>()))
                    Log.d(TAG, "linkUserDevice: exist ${it.getValue<String>()}")
                } else {
                    it.ref.setValue(temporaryId)
                    offer(DataState.Success(temporaryId))
                    Log.d(TAG, "linkUserDevice: Doesnt")
                }
            }
            close()
        }.addOnFailureListener {
            if (!isClosedForSend)
                offer(DataState.Error(it.message!!))
            close(it)
        }
        // rootRef.child("user_devices").child(userId).setValue(temporaryId)
        //     .addOnSuccessListener {
        //         if (!isClosedForSend)
        //             offer(DataState.Success("true"))
        //         close()
        //     }.addOnFailureListener {
        //         if (!isClosedForSend)
        //             offer(DataState.Error(it.message!!))
        //         close(it)
        //     }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun goal(userRepId: String) = callbackFlow {
        offer(DataState.Loading(null))
        rootRef.child("user_goals").child(userRepId)
            // orderByChild("addedAt").limitToFirst(1)
            .get()
            .addOnSuccessListener {
                if (!isClosedForSend) {
                    val rawres = it.getValue<GoalResponse>()
                    Log.d(TAG, "goal: $rawres")
                    if (rawres != null) {
                        offer(DataState.Success(rawres))
                    } else {
                        offer(DataState.Empty)
                    }
                }
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(DataState.Error(it.message!!))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun saveFood(userRepId: String, request: SaveFoodRequest) = callbackFlow {
        offer(DataState.Loading(true))
        rootRef.child("user_intakes").child(userRepId).push().setValue(request)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(DataState.Success(true))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(DataState.Error(it.message!!))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun todayNutrient(userRepId: String, date: String) =
        callbackFlow<DataState<Map<String, Float>>> {

            offer(DataState.Loading(null))
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val res = snapshot.getValue<Map<String, Float>>() ?: emptyMap()
                    offer(DataState.Success(res))
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isClosedForSend)
                        offer(DataState.Error(error.message))
                    close(error.toException().cause)
                }
            }

            val ref = rootRef.child("user_daily").child(userRepId).child("nutrients").child(date)
            ref.addValueEventListener(listener)

            awaitClose {
                ref.removeEventListener(listener)
                Log.e(TAG, "todayNutrient: CLOSE")
            }
        }

    fun todayFood(userRepId: String, date: String) =
        callbackFlow<DataState<List<Pair<String, FoodResponse>>>> {
            offer(DataState.Loading(null))

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // TODO: find a way to remove bang operator
                    if (snapshot.hasChildren()) {
                        val result: List<Pair<String, FoodResponse>> =
                            snapshot.children.map { child ->
                                if (child.key != null && child.getValue<FoodResponse>() != null) {
                                    child.key!! to child.getValue<FoodResponse>()!!
                                } else return
                            }
                        offer(DataState.Success(result))
                    } else {
                        offer(DataState.Empty)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isClosedForSend)
                        offer(DataState.Error(error.message))
                    close(error.toException().cause)
                }
            }

            val ref =
                rootRef.child("user_intakes").child(userRepId).orderByChild("date").equalTo(date)
            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
                Log.e(TAG, "todayFood: CLOSE")
            }
        }

    fun newBodyWeight(userRepId: String, request: BodyWeightRequest) = callbackFlow {
        offer(DataState.Loading(true))
        rootRef.child("user_weights").child(userRepId).push().setValue(request)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(DataState.Success(true))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(DataState.Error(it.message!!))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "newBodyWeight: CLOSE")
        }
    }

    fun bodyWeights(userRepId: String) =
        callbackFlow<DataState<List<Pair<String, BodyWeightResponse>>>> {
            offer(DataState.Loading(null))
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    // TODO: find a way to remove bang operator
                    if (snapshot.hasChildren()) {
                        val result =
                            snapshot.children.map { child -> child.key!! to child.getValue<BodyWeightResponse>()!! }
                        offer(DataState.Success(result))
                    } else {
                        offer(DataState.Empty)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isClosedForSend)
                        offer(DataState.Error(error.message))
                    close(error.toException().cause)
                }
            }
            val ref = rootRef.child("user_weights").child(userRepId).orderByChild("addedAt")
            ref.addValueEventListener(listener)
            awaitClose {
                ref.removeEventListener(listener)
                Log.e(TAG, "bodyWeights: CLOSE")
            }
        }

    fun clearAllWeights(userRepId: String) {
    }

    fun deleteWeight(userRepId: String, weighId: String) {
    }

    fun saveWorkout(userRepId: String, workout: SaveWorkoutRequest) = callbackFlow {
        offer(DataState.Loading(true))
        rootRef.child("user_workouts").child(userRepId).push().setValue(workout)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(DataState.Success(true))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(DataState.Error(it.message!!))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "saveWorkout: CLOSE")
        }
    }

    fun caloriesBurned(userRepId: String, date: String) =
        callbackFlow<DataState<BurnCalorieResponse>> {
            offer(DataState.Loading(null))
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val res = snapshot.getValue<BurnCalorieResponse>()
                    if (res != null)
                        offer(DataState.Success(res))
                    else
                        offer(DataState.Empty)
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isClosedForSend)
                        offer(DataState.Error(error.message))
                    close(error.toException().cause)
                }
            }
            val ref = rootRef.child("user_daily").child(userRepId).child("workouts").child(date)
            ref.addValueEventListener(listener)

            awaitClose {
                ref.removeEventListener(listener)
                Log.e(TAG, "calorieBurned: CLOSE")
            }
        }

    fun deleteTodaysFood(userRepId: String, key: String) = callbackFlow {
        rootRef.child("user_intakes").child(userRepId).child(key).setValue(null)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(true, null))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(false, it))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun deleteTodaysWorkout(userRepId: String, key: String) = callbackFlow {
        rootRef.child("user_workouts").child(userRepId).child(key).setValue(null)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(true, null))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(false, it))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    fun deleteBodyWeight(userRepId: String, key: String) = callbackFlow {
        rootRef.child("user_weights").child(userRepId).child(key).setValue(null)
            .addOnSuccessListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(true, null))
                close()
            }.addOnFailureListener {
                if (!isClosedForSend)
                    offer(Pair<Boolean, Exception?>(false, it))
                close(it)
            }
        awaitClose {
            Log.e(TAG, "setToken: CLOSE")
        }
    }

    companion object {
        private val TAG = DieterRealtimeDatabase::class.java.simpleName
    }
}
