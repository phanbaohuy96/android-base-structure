package com.pbh.androidbase.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pbh.androidbase.feature.home.ui.detail.ItemDetailScreen
import com.pbh.androidbase.feature.home.ui.list.HomeListScreen

/** Registers home list and item detail destinations in the app navigation graph. */
fun NavGraphBuilder.homeGraph(
    onLogout: () -> Unit,
    onOpenDetail: (String) -> Unit,
) {
    composable<HomeListRoute> {
        HomeListScreen(
            onLogout = onLogout,
            onOpenDetail = onOpenDetail,
        )
    }
    composable<HomeDetailRoute> { entry ->
        val route = entry.toRoute<HomeDetailRoute>()
        ItemDetailScreen(itemId = route.itemId)
    }
}
