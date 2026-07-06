package com.pbh.androidbase.data.remote.source

import com.pbh.androidbase.data.remote.api.AuthApi
import com.pbh.androidbase.data.remote.dto.LoginRequestDto
import com.pbh.androidbase.data.remote.dto.SessionDto

/** Auth source backed by Retrofit and the configured remote API. */
class RealAuthRemoteDataSource(
    private val authApi: AuthApi,
) : AuthRemoteDataSource {
    /** Calls the login endpoint and unwraps the response data. */
    override suspend fun login(
        email: String,
        password: String,
    ): SessionDto = authApi.login(LoginRequestDto(email = email, password = password)).data
}
