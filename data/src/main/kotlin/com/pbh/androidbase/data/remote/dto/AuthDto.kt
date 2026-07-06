package com.pbh.androidbase.data.remote.dto

import kotlinx.serialization.Serializable

/** Request body sent to the login endpoint. */
@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

/** Session payload returned by the login endpoint. */
@Serializable
data class SessionDto(
    val userId: String,
    val email: String,
    val token: String,
)
