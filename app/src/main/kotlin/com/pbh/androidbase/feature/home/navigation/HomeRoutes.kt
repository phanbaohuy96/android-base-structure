package com.pbh.androidbase.feature.home.navigation

import kotlinx.serialization.Serializable

/** Type-safe navigation route for the home list. */
@Serializable
data object HomeListRoute

/** Type-safe navigation route for one item detail destination. */
@Serializable
data class HomeDetailRoute(
    val itemId: String,
)
