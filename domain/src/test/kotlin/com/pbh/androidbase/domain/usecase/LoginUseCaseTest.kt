package com.pbh.androidbase.domain.usecase

import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.model.ValidationReason
import com.pbh.androidbase.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginUseCaseTest {
    @Test
    fun `rejects invalid email before repository call`() =
        runTest {
            val repository = FakeAuthRepository()
            val useCase = LoginUseCase(repository)

            val result = useCase("invalid", "password")

            assertIs<AppResult.Failure>(result)
            val error = assertIs<DomainError.Validation>(result.error)
            assertEquals(ValidationReason.InvalidEmail, error.reason)
            assertEquals(0, repository.loginCalls)
        }

    @Test
    fun `rejects short password before repository call`() =
        runTest {
            val repository = FakeAuthRepository()
            val useCase = LoginUseCase(repository)

            val result = useCase("demo@example.com", "short")

            assertIs<AppResult.Failure>(result)
            val error = assertIs<DomainError.Validation>(result.error)
            assertEquals(ValidationReason.PasswordTooShort, error.reason)
            assertEquals(0, repository.loginCalls)
        }

    @Test
    fun `delegates valid credentials to repository`() =
        runTest {
            val repository = FakeAuthRepository()
            val useCase = LoginUseCase(repository)

            val result = useCase("demo@example.com", "password")

            assertIs<AppResult.Success<UserSession>>(result)
            assertEquals("demo@example.com", result.data.email)
            assertEquals(1, repository.loginCalls)
        }
}

private class FakeAuthRepository : AuthRepository {
    var loginCalls = 0

    override fun observeSession(): Flow<UserSession?> = flowOf(null)

    override suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession> {
        loginCalls += 1
        return AppResult.Success(
            UserSession(
                userId = "test-user",
                email = email,
                token = "test-token",
            ),
        )
    }

    override suspend fun logout() = Unit
}
