package com.example.dieter.data.source.firebase

import android.util.Log
import com.example.dieter.data.source.firebase.request.SetGoalRequest
import com.example.dieter.vo.DataState
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
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
    @Provides
    fun provideFirebaseRootRef() = Firebase.database.reference
}

// https://firebase.google.com/docs/database/android/read-and-write
class DieterRealtimeDatabase @Inject constructor(
    private val rootRef: DatabaseReference
) {
    fun setGoal(userId: String, goal: SetGoalRequest) {
        // rootRef.child()
    }



    @OptIn(ExperimentalCoroutinesApi::class)
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

    @OptIn(ExperimentalCoroutinesApi::class)
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

    companion object {
        private val TAG = DieterRealtimeDatabase::class.java.simpleName
    }
}
