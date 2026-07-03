package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.map
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionUseCaseTest {
    @Test
    fun `observe session delegates to repository stream`() =
        runTest {
            val repository = FakeSessionRepository()

            val result = ObserveSessionUseCase(repository)().toList()

            assertEquals(listOf(testSession), result)
        }

    @Test
    fun `logout delegates to repository`() =
        runTest {
            val repository = FakeSessionRepository()

            LogoutUseCase(repository)()

            assertEquals(1, repository.logoutCalls)
        }

    @Test
    fun `app result map transforms success and preserves failure`() {
        val success = AppResult.Success(2).map { it * 3 }
        val failure = AppResult.Failure(com.pbh.androidbase.domain.model.DomainError.Unknown).map { "unused" }

        assertEquals(AppResult.Success(6), success)
        assertEquals(AppResult.Failure(com.pbh.androidbase.domain.model.DomainError.Unknown), failure)
    }
}

private val testSession =
    UserSession(
        userId = "test-user",
        email = "demo@example.com",
        token = "test-token",
    )

private class FakeSessionRepository : AuthRepository {
    var logoutCalls = 0

    override fun observeSession(): Flow<UserSession?> = flowOf(testSession)

    override suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession> = AppResult.Success(testSession)

    override suspend fun logout() {
        logoutCalls += 1
    }
}
