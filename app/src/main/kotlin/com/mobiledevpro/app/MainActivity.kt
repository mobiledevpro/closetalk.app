package com.mobiledevpro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.mobiledevpro.app.ui.MainApp
import com.mobiledevpro.ui.theme.AppTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //False - allows to drawing the content "edge-to-edge"
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // val darkModeState by darkModeState.collectAsStateWithLifecycle()

            AppTheme(darkTheme = true) {
                KoinContext {
                    MainApp()
                }
            }
        }
    }
}