package com.pbh.androidbase.domain.entity

data class UserSession(
    val userId: String,
    val email: String,
    val token: String,
)
