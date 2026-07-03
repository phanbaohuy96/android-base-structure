package com.pbh.androidbase.data.remote.api

import com.pbh.androidbase.core.network.ApiResponse
import com.pbh.androidbase.data.remote.dto.LoginRequestDto
import com.pbh.androidbase.data.remote.dto.SessionDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto,
    ): ApiResponse<SessionDto>
}
