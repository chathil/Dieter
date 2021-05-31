package com.example.dieter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import com.example.dieter.data.source.domain.ActiveCounterModel
import com.example.dieter.service.CountdownService
import com.example.dieter.utils.LocalSysUiController
import com.example.dieter.utils.SystemUiController
import com.example.dieter.vo.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appViewModel: DieterAppViewModel by viewModels()
    private val appState = DieterAppState()
    private val countDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val counter = intent?.extras?.get("active_counter")
            val isDone = intent?.extras?.getBoolean("is_done")
            if (counter is ActiveCounterModel) {
                appState.updateCounter(counter)
            }
            if (isDone == true) {
                appState.workoutDone()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        WindowCompat.setDecorFitsSystemWindows(window, false) /* Going edge to edge */
        setContent {
            val systemUiController = remember { SystemUiController(window) }
            CompositionLocalProvider(LocalSysUiController provides systemUiController) {
                val userRepId by appViewModel.userRepIdState.collectAsState()
                val isWelcomeShown by appViewModel.isWelcomeShownState.collectAsState()
                when (userRepId) {
                    is DataState.Success -> {
                        DieterApp(
                            appState = appState,
                            userRepId = (userRepId as DataState.Success<String>).data,
                            showWelcomeInitially = !isWelcomeShown,
                            welcomeShown = {
                                appViewModel.isWelcomeShown(true)
                            },
                            finishActivity = { finish() }
                        )
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(countDownReceiver, IntentFilter(CountdownService.COUNTDOWN_BR))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(countDownReceiver)
    }

    override fun onStop() {
        try {
            unregisterReceiver(countDownReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "onStop: $e")
        }
        super.onStop()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(CountdownService.COUNTDOWN_BR, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
