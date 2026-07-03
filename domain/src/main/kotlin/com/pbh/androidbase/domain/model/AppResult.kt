package com.pbh.androidbase.domain.model

sealed interface AppResult<out T> {
    data class Success<T>(
        val data: T,
    ) : AppResult<T>

    data class Failure(
        val error: DomainError,
    ) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> =
    when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Failure -> this
    }
