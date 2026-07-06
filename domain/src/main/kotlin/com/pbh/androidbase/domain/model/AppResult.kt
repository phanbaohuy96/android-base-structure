package com.pbh.androidbase.domain.model

/**
 * Result of a domain operation that can fail with a typed [DomainError].
 *
 * Use this at repository and use-case boundaries instead of throwing recoverable
 * business or infrastructure failures across layers.
 */
sealed interface AppResult<out T> {
    /** Operation succeeded and returned [data]. */
    data class Success<T>(
        val data: T,
    ) : AppResult<T>

    /** Operation failed with a typed, UI-translatable [error]. */
    data class Failure(
        val error: DomainError,
    ) : AppResult<Nothing>
}

/** Transforms successful data while preserving failures unchanged. */
inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> =
    when (this) {
        is AppResult.Success -> AppResult.Success(transform(data))
        is AppResult.Failure -> this
    }
