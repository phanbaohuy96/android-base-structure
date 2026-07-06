package com.pbh.androidbase.core.network

import com.pbh.androidbase.domain.model.DomainError
import retrofit2.HttpException
import java.io.IOException

/** Maps infrastructure exceptions into stable domain error categories. */
object NetworkErrorMapper {
    private const val HTTP_UNAUTHORIZED = 401
    private const val HTTP_FORBIDDEN = 403
    private const val HTTP_NOT_FOUND = 404

    /** Converts [throwable] into a [DomainError] suitable for domain/UI boundaries. */
    fun map(throwable: Throwable): DomainError =
        when (throwable) {
            is HttpException ->
                when (throwable.code()) {
                    HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> DomainError.Unauthorized
                    HTTP_NOT_FOUND -> DomainError.NotFound
                    else -> DomainError.Remote(throwable.code())
                }
            is IOException -> DomainError.Offline
            else -> DomainError.Unknown
        }
}
