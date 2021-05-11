package com.example.dieter.data.source.firebase

import android.util.Log
import com.example.dieter.vo.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class DieterFirebaseAuthModule {
    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}

class DieterFirebaseAuth @Inject constructor(
    private val auth: FirebaseAuth
) {

    // @OptIn(ExperimentalCoroutinesApi::class)
    // fun authWithGoogle(idToken: String): Flow<DataState<FirebaseUser>> = flow {
    //     emit(DataState.Loading(null))
    //     val s =  try {
    //         val credential = GoogleAuthProvider.getCredential(idToken, null)
    //         val data = auth.signInWithCredential(credential).await()
    //         Log.d(TAG, "authWithGoogle: ${data.user}")
    //         emit(DataState.Success(data.user))
    //     }catch(e: Exception) {
    //         emit(DataState.Error(e.message ?: "Unknown Error"))
    //     }
    // }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun authWithGoogle(idToken: String): Flow<DataState<FirebaseUser>> = callbackFlow {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            if (!isClosedForSend)
                offer(DataState.Success(it.user))
            close()
        }.addOnFailureListener {
            if(!isClosedForSend)
                offer(DataState.Error(it.message!!))
            close(it)
        }
        awaitClose {
            Log.e(TAG, "authWithGoogle: CLOSE", )
        }
    }

    private fun authWithTwitter() {
        // TODO: implement login with twitter
    }

    companion object {
        private val TAG = DieterFirebaseAuth::class.java.simpleName
    }
}



