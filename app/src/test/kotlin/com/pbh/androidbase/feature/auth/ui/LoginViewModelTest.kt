package com.pbh.androidbase.feature.auth.ui

import app.cash.turbine.test
import com.pbh.androidbase.core.R
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.repository.AuthRepository
import com.pbh.androidbase.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful login emits success state and navigation effect`() =
        runTest(dispatcher) {
            val viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository()))

            viewModel.effects.test {
                viewModel.login("demo@example.com", "password")
                advanceUntilIdle()

                assertEquals(LoginUiState.Success, viewModel.state.value)
                assertIs<LoginEffect.NavigateHome>(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invalid email emits localized validation error`() =
        runTest(dispatcher) {
            val viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository()))

            viewModel.effects.test {
                viewModel.login("invalid", "password")
                advanceUntilIdle()

                val expected = UiText.Resource(R.string.core_error_invalid_email)
                assertEquals(LoginUiState.Error(expected), viewModel.state.value)
                assertEquals(LoginEffect.ShowMessage(expected), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `repository failure emits localized remote error`() =
        runTest(dispatcher) {
            val viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository(AppResult.Failure(DomainError.Remote(500)))))

            viewModel.effects.test {
                viewModel.login("demo@example.com", "password")
                advanceUntilIdle()

                val expected = UiText.Resource(R.string.core_error_generic)
                assertEquals(LoginUiState.Error(expected), viewModel.state.value)
                assertEquals(LoginEffect.ShowMessage(expected), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}

private class FakeAuthRepository(
    private val loginResult: AppResult<UserSession> =
        AppResult.Success(
            UserSession(
                userId = "test-user",
                email = "demo@example.com",
                token = "test-token",
            ),
        ),
) : AuthRepository {
    override fun observeSession(): Flow<UserSession?> = flowOf(null)

    override suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession> = loginResult

    override suspend fun logout() = Unit
}
