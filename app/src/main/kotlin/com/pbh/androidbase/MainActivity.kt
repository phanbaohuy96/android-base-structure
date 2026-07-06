package com.pbh.androidbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pbh.androidbase.core.designsystem.AndroidBaseTheme
import com.pbh.androidbase.core.designsystem.AppThemeDefaults
import com.pbh.androidbase.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/** Main launcher activity that applies theme selection and hosts app navigation. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /** Creates the Compose content tree and waits for the session guard start route. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeConfig =
                if (isSystemInDarkTheme()) {
                    AppThemeDefaults.dark()
                } else {
                    AppThemeDefaults.light()
                }
            AndroidBaseTheme(config = themeConfig) {
                val appViewModel: AppViewModel = hiltViewModel()
                val startRoute by appViewModel.startRoute.collectAsStateWithLifecycle()
                when (val route = startRoute) {
                    null -> SplashPlaceholder()
                    else -> AppNavHost(startRoute = route)
                }
            }
        }
    }
}

@Composable
private fun SplashPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
