package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.repository.AuthRepository

/** Clears the current user session. */
class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    /** Delegates logout to the auth repository. */
    suspend operator fun invoke() = authRepository.logout()
}
