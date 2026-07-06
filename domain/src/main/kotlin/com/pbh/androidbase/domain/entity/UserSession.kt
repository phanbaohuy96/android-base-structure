package com.pbh.androidbase.domain.entity

/** Authenticated user identity plus bearer token persisted by the data layer. */
data class UserSession(
    val userId: String,
    val email: String,
    val token: String,
)
