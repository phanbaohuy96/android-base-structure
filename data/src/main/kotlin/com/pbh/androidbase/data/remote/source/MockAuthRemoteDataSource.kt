package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.dto.SessionDto
import kotlinx.coroutines.delay

/** Deterministic auth source used when `BuildConfig.USE_MOCK` is enabled. */
class MockAuthRemoteDataSource : AuthRemoteDataSource {
    /** Returns a synthetic session for any validated credentials. */
    override suspend fun login(
        email: String,
        password: String,
    ): SessionDto {
        delay(350)
        return SessionDto(
            userId = "mock-user",
            email = email,
            token = "mock-token-for-$email",
        )
    }
}
