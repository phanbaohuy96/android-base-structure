package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveSessionUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<UserSession?> = authRepository.observeSession()
}
