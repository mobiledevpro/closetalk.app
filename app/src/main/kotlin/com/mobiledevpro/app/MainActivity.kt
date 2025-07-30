package com.mobiledevpro.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiledevpro.analytics.ImplAnalytics
import com.mobiledevpro.app.di.mainModule
import com.mobiledevpro.app.ui.MainApp
import com.mobiledevpro.di.koinScope
import com.mobiledevpro.main.view.state.MainUIState
import com.mobiledevpro.main.view.vm.MainViewModel
import com.mobiledevpro.sync.service.SyncDataService
import com.mobiledevpro.ui.compositionlocal.LocalAnalytics
import com.mobiledevpro.ui.theme.AppTheme
import org.koin.compose.KoinContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules

class MainActivity : ComponentActivity(), KoinComponent {

    private val viewModel: MainViewModel by koinScope<MainActivity>().inject()
    private val firebaseAnalytics by getKoin().inject<FirebaseAnalytics>()

    init {
        loadKoinModules(mainModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //False - allows to drawing the content "edge-to-edge"
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            var darkMode by remember { mutableStateOf(true) }

            if (uiState is MainUIState.Success) {
                (uiState as MainUIState.Success).settings.let {
                    darkMode = it.darkMode
                }
            }

            AppTheme(darkTheme = darkMode) {
                KoinContext {
                    CompositionLocalProvider(LocalAnalytics provides ImplAnalytics(firebaseAnalytics)) {
                        MainApp()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, SyncDataService::class.java)
            .also(this::startForegroundService)
    }

    override fun onStop() {
        super.onStop()
        Intent(this, SyncDataService::class.java)
            .also(this::stopService)
    }
}