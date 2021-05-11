package com.example.dieter.data.source

import android.util.Log
import com.example.dieter.data.source.firebase.DieterFirebaseAuth
import com.example.dieter.vo.DataState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DieterRepository @Inject constructor(
    private val dieterFirebaseAuth: DieterFirebaseAuth
) : DieterDataSource {

    fun authWithGoogle(idToken: String) =
        dieterFirebaseAuth.authWithGoogle(idToken)

    companion object {
        private val TAG = DieterRepository::class.java.simpleName
    }
}
