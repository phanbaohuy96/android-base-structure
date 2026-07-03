package com.pbh.androidbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.pbh.androidbase.core.designsystem.AndroidBaseTheme
import com.pbh.androidbase.core.designsystem.AppThemeDefaults
import com.pbh.androidbase.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                AppNavHost()
            }
        }
    }
}
