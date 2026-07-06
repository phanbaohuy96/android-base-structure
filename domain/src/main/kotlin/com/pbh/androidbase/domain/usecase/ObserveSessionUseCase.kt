package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/** Observes whether the app should start authenticated or unauthenticated. */
class ObserveSessionUseCase(
    private val authRepository: AuthRepository,
) {
    /** Emits the current session, or null when no session is available. */
    operator fun invoke(): Flow<UserSession?> = authRepository.observeSession()
}
