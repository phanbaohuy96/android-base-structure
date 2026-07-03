package com.pbh.androidbase.domain.model

sealed interface DomainError {
    data object Unauthorized : DomainError

    data object Offline : DomainError

    data object NotFound : DomainError

    data class Validation(
        val reason: ValidationReason,
    ) : DomainError

    data class Remote(
        val code: Int?,
    ) : DomainError

    data object Unknown : DomainError
}

enum class ValidationReason {
    InvalidEmail,
    PasswordTooShort,
}
