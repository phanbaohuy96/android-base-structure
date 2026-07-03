package com.pbh.androidbase.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class SessionDto(
    val userId: String,
    val email: String,
    val token: String,
)
