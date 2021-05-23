package com.example.dieter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import com.example.dieter.utils.LocalSysUiController
import com.example.dieter.utils.SystemUiController
import com.example.dieter.vo.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appViewModel: DieterAppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) /* Going edge to edge */
        setContent {
            val systemUiController = remember { SystemUiController(window) }
            CompositionLocalProvider(LocalSysUiController provides systemUiController) {
                val userRepId by appViewModel.userRepIdState.collectAsState()
                val isWelcomeShown by appViewModel.isWelcomeShownState.collectAsState()
                when (userRepId) {
                    is DataState.Success -> {
                        DieterApp(
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
}
