package com.pbh.androidbase.core.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(
    private val tokenProvider: () -> String?,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request =
            if (token.isNullOrBlank()) {
                chain.request()
            } else {
                chain
                    .request()
                    .newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }
        return chain.proceed(request)
    }
}
