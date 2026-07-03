package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() = authRepository.logout()
}
