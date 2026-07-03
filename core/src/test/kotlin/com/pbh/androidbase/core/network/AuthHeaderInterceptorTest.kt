package com.pbh.androidbase.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthHeaderInterceptorTest {
    @Test
    fun `adds bearer token when token is available`() {
        val request = executeRequest(token = "session-token")

        assertEquals("Bearer session-token", request.header("Authorization"))
    }

    @Test
    fun `leaves request unchanged when token is blank`() {
        val request = executeRequest(token = "")

        assertNull(request.header("Authorization"))
    }

    private fun executeRequest(token: String?): Request {
        lateinit var capturedRequest: Request
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthHeaderInterceptor { token })
                .addInterceptor(
                    Interceptor { chain ->
                        capturedRequest = chain.request()
                        Response
                            .Builder()
                            .request(capturedRequest)
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .body("".toResponseBody())
                            .build()
                    },
                ).build()

        client.newCall(Request.Builder().url("https://example.test/").build()).execute().close()

        return capturedRequest
    }
}
