package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.model.ValidationReason
import com.pbh.androidbase.domain.repository.AuthRepository

private const val MIN_PASSWORD_LENGTH = 6

/** Validates credentials and delegates authentication to [AuthRepository]. */
class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    /** Returns a session on success or a typed validation/authentication failure. */
    suspend operator fun invoke(
        email: String,
        password: String,
    ): AppResult<UserSession> {
        if (!email.contains("@")) {
            return AppResult.Failure(DomainError.Validation(ValidationReason.InvalidEmail))
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            return AppResult.Failure(DomainError.Validation(ValidationReason.PasswordTooShort))
        }
        return authRepository.login(email.trim(), password)
    }
}
