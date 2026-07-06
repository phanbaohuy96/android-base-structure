package com.pbh.androidbase.domain.repository

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import kotlinx.coroutines.flow.Flow

/** Domain contract for session observation and authentication actions. */
interface AuthRepository {
    /** Emits the current persisted session, or null when the user is logged out. */
    fun observeSession(): Flow<UserSession?>

    /** Attempts to authenticate [email] and [password], returning a typed result. */
    suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession>

    /** Clears the persisted session. */
    suspend fun logout()
}
