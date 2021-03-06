package com.example.dieter.data.source.firebase

import android.util.Log
import com.example.dieter.BuildConfig
import com.example.dieter.utils.EmulatorHost
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class DieterFirebaseAuthModule {
    private val useEmulator = BuildConfig.EMULATE_SERVER

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        val auth = Firebase.auth
        if (useEmulator)
            auth.useEmulator(EmulatorHost.ip, EmulatorHost.authPort)
        return auth
    }
}

class DieterFirebaseAuth @Inject constructor(
    private val auth: FirebaseAuth
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun authWithGoogle(idToken: String): Flow<DataState<FirebaseUser>> = callbackFlow {
        offer(DataState.Loading(null))
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            if (!isClosedForSend)
                offer(DataState.Success(it.user))
            close()
        }.addOnFailureListener {
            if (!isClosedForSend)
                offer(DataState.Error(it.message!!))
            close(it)
        }
        awaitClose {
            Log.e(TAG, "authWithGoogle: CLOSE")
        }
    }

    private fun authWithTwitter() {
        // TODO: implement login with twitter
    }

    companion object {
        private val TAG = DieterFirebaseAuth::class.java.simpleName
    }
}
