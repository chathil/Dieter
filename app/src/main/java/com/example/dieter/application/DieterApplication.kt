package com.example.dieter.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DieterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
