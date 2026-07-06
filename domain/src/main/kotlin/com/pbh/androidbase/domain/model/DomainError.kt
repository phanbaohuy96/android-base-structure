package com.pbh.androidbase.domain.model

/** Typed failure reasons shared by domain, data, and UI translation layers. */
sealed interface DomainError {
    /** The caller is unauthenticated or the stored session is no longer accepted. */
    data object Unauthorized : DomainError

    /** The requested operation could not reach the network. */
    data object Offline : DomainError

    /** The requested resource is absent from the backing store. */
    data object NotFound : DomainError

    /** User-provided input failed a domain validation rule. */
    data class Validation(
        val reason: ValidationReason,
    ) : DomainError

    /** Remote service returned an unsuccessful response, optionally with [code]. */
    data class Remote(
        val code: Int?,
    ) : DomainError

    /** Fallback for unexpected failures that should still be shown safely. */
    data object Unknown : DomainError
}

/** Specific validation failures produced by domain use cases. */
enum class ValidationReason {
    /** Email input is not in the minimal accepted shape. */
    InvalidEmail,

    /** Password input is shorter than the accepted minimum length. */
    PasswordTooShort,
}
