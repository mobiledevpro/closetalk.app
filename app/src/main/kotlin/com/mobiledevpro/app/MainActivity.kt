package com.mobiledevpro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobiledevpro.app.di.mainModule
import com.mobiledevpro.app.ui.MainApp
import com.mobiledevpro.di.koinScope
import com.mobiledevpro.main.view.state.MainUIState
import com.mobiledevpro.main.view.vm.MainViewModel
import com.mobiledevpro.ui.theme.AppTheme
import org.koin.compose.KoinContext
import org.koin.core.context.loadKoinModules
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by koinScope<MainActivity>().inject()

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
                    MainApp()
                }
            }
        }
    }
}