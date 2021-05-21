package com.example.dieter.application

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.dieter.DieterAppViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DieterApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: DieterApplication? = null
        fun applicationContext() = instance
        private val TAG = DieterApplication::class.java.simpleName
    }
}
