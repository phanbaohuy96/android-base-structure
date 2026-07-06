package com.pbh.androidbase.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pbh.androidbase.feature.auth.ui.LoginScreen

/** Registers authentication destinations in the app navigation graph. */
fun NavGraphBuilder.authGraph(onLoginComplete: () -> Unit) {
    composable<LoginRoute> {
        LoginScreen(onLoginComplete = onLoginComplete)
    }
}
