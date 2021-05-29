package com.example.dieter.data.source.firebase

import android.util.Log
import com.example.dieter.BuildConfig
import com.example.dieter.data.source.firebase.request.BodyWeightRequest
import com.example.dieter.data.source.firebase.request.SaveFoodRequest
import com.example.dieter.data.source.firebase.request.SetGoalRequest
import com.example.dieter.data.source.firebase.response.BodyWeightResponse
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
        rootRef.child("user_goals").child(userRepId).push().setValue(goal).addOnSuccessListener {
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
        rootRef.child("user_devices").child(userId).setValue(temporaryId)
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

    fun goal(userRepId: String) = callbackFlow {
        offer(DataState.Loading(null))
        rootRef.child("user_goals").child(userRepId).orderByChild("addedAt").limitToFirst(1).get()
            .addOnSuccessListener {
                if (!isClosedForSend) {
                    val rawres = it.getValue<GoalResponse>()
                    if (rawres != null) {
                        rawres.let { res ->
                            Log.d(TAG, "goalInn: $res")
                            offer(DataState.Success(res))
                        }
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

    fun todayNutrient(userRepId: String, date: String) = callbackFlow<DataState<Map<String, Float>>> {

        offer(DataState.Loading(null))
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isClosedForSend) {
                    val res = snapshot.getValue<Map<String, Float>>() ?: emptyMap()
                    offer(DataState.Success(res))
                }
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                if (!isClosedForSend)
                    offer(DataState.Error(error.message))
                close(error.toException().cause)
            }
        }

        rootRef.child("user_daily").child(userRepId).child("nutrients").child(date)
            .addValueEventListener(listener)

        awaitClose {
            Log.e(TAG, "todayNutrient: CLOSE")
        }
    }

    fun todayFood(userRepId: String, date: String) = callbackFlow<DataState<List<Pair<String, FoodResponse>>>> {
        offer(DataState.Loading(null))

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isClosedForSend) {
                    // TODO: find a way to remove bang operator
                    if (snapshot.hasChildren()) {
                        val result =
                            snapshot.children.map { child -> child.key!! to child.getValue<FoodResponse>()!! }
                        offer(DataState.Success(result))
                    } else {
                        offer(DataState.Empty)
                    }
                }
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                if (!isClosedForSend)
                    offer(DataState.Error(error.message))
                close(error.toException().cause)
            }
        }

        rootRef.child("user_intakes").child(userRepId).orderByChild("date").equalTo(date)
            .addValueEventListener(listener)
        awaitClose {
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
                    if (!isClosedForSend) {
                        // TODO: find a way to remove bang operator
                        if (snapshot.hasChildren()) {
                            val result =
                                snapshot.children.map { child -> child.key!! to child.getValue<BodyWeightResponse>()!! }
                            offer(DataState.Success(result))
                        } else {
                            offer(DataState.Empty)
                        }
                    }
                    close()
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isClosedForSend)
                        offer(DataState.Error(error.message))
                    close(error.toException().cause)
                }
            }
            rootRef.child("user_weights").child(userRepId).orderByChild("addedAt")
                .addValueEventListener(listener)
            awaitClose {
                Log.e(TAG, "bodyWeights: CLOSE")
            }
        }

    fun clearAllWeights(userRepId: String) {
    }

    fun deleteWeight(userRepId: String, weighId: String) {
    }

    companion object {
        private val TAG = DieterRealtimeDatabase::class.java.simpleName
    }
}
