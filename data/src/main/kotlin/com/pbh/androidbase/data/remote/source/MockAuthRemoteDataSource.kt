package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.dto.SessionDto
import kotlinx.coroutines.delay

class MockAuthRemoteDataSource : AuthRemoteDataSource {
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
