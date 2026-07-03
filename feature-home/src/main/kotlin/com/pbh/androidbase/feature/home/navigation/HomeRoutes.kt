package com.pbh.androidbase.feature.home.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeListRoute

@Serializable
data class HomeDetailRoute(
    val itemId: String,
)
