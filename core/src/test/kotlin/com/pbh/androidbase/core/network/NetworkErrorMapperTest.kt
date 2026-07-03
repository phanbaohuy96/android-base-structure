package com.pbh.androidbase.core.network

import com.pbh.androidbase.domain.model.DomainError
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkErrorMapperTest {
    @Test
    fun `maps unauthorized and forbidden http errors to unauthorized`() {
        assertEquals(DomainError.Unauthorized, NetworkErrorMapper.map(httpException(401)))
        assertEquals(DomainError.Unauthorized, NetworkErrorMapper.map(httpException(403)))
    }

    @Test
    fun `maps not found http error to not found`() {
        assertEquals(DomainError.NotFound, NetworkErrorMapper.map(httpException(404)))
    }

    @Test
    fun `maps other http errors to remote with status code`() {
        assertEquals(DomainError.Remote(500), NetworkErrorMapper.map(httpException(500)))
    }

    @Test
    fun `maps io exceptions to offline`() {
        assertEquals(DomainError.Offline, NetworkErrorMapper.map(IOException("network unavailable")))
    }

    @Test
    fun `maps unknown throwables to unknown`() {
        assertEquals(DomainError.Unknown, NetworkErrorMapper.map(IllegalStateException("unexpected")))
    }

    private fun httpException(code: Int): HttpException = HttpException(Response.error<String>(code, "".toResponseBody()))
}
