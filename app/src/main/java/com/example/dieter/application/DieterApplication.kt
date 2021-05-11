package com.example.dieter.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DieterApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: DieterApplication? = null
        fun applicationContext() = instance
    }
}
