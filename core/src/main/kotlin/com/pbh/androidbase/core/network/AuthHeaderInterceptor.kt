package com.pbh.androidbase.core.network

import okhttp3.Interceptor
import okhttp3.Response

/** Adds a bearer `Authorization` header when [tokenProvider] returns a non-blank token. */
class AuthHeaderInterceptor(
    private val tokenProvider: () -> String?,
) : Interceptor {
    /** Intercepts the request and conditionally attaches the current auth token. */
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
