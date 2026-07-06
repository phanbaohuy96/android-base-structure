package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.dto.SessionDto

/** Data-source boundary for swappable mock and real authentication implementations. */
interface AuthRemoteDataSource {
    /** Returns a session DTO or throws when authentication fails. */
    suspend fun login(
        email: String,
        password: String,
    ): SessionDto
}
