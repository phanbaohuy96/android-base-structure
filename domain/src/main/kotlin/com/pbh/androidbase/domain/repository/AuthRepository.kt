package com.pbh.androidbase.domain.repository

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<UserSession?>

    suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession>

    suspend fun logout()
}
