package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.dto.SessionDto

interface AuthRemoteDataSource {
    suspend fun login(
        email: String,
        password: String,
    ): SessionDto
}
