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
fun AppNavHost(startRoute: StartRoute) {
    val navController = rememberNavController()

    val startDestination: Any =
        when (startRoute) {
            StartRoute.Login -> LoginRoute
            StartRoute.Home -> HomeListRoute
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
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
