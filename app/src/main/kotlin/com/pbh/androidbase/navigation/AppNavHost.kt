package com.pbh.androidbase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pbh.androidbase.feature.auth.navigation.LoginRoute
import com.pbh.androidbase.feature.auth.navigation.authGraph
import com.pbh.androidbase.feature.home.navigation.HomeDetailRoute
import com.pbh.androidbase.feature.home.navigation.HomeListRoute
import com.pbh.androidbase.feature.home.navigation.homeGraph

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
    ) {
        authGraph(
            onLoginComplete = {
                navController.navigate(HomeListRoute) {
                    popUpTo<LoginRoute> { inclusive = true }
                }
            },
        )
        homeGraph(
            onLogout = {
                navController.navigate(LoginRoute) {
                    popUpTo(0)
                }
            },
            onOpenDetail = { itemId ->
                navController.navigate(HomeDetailRoute(itemId = itemId))
            },
        )
    }
}
