package com.pbh.androidbase.data.remote.api

import com.pbh.androidbase.core.network.ApiResponse
import com.pbh.androidbase.data.remote.dto.LoginRequestDto
import com.pbh.androidbase.data.remote.dto.SessionDto
import retrofit2.http.Body
import retrofit2.http.POST

/** Retrofit API for authentication endpoints. */
interface AuthApi {
    /** Authenticates with the remote service and returns the response envelope. */
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto,
    ): ApiResponse<SessionDto>
}
