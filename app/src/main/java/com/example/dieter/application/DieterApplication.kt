package com.example.dieter.application

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DieterApplication : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
    }

    companion object {
        private var instance: DieterApplication? = null
        fun applicationContext() = instance
        private val TAG = DieterApplication::class.java.simpleName
    }
}
