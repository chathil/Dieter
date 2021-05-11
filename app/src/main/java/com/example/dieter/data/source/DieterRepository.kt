package com.example.dieter.data.source

import com.example.dieter.data.source.firebase.DieterFirebaseAuth
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
