package com.pbh.androidbase.feature.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pbh.androidbase.core.designsystem.AndroidBaseTheme
import com.pbh.androidbase.domain.entity.UserSession
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.repository.AuthRepository
import com.pbh.androidbase.domain.usecase.LoginUseCase
import com.pbh.androidbase.feature.auth.ui.LoginScreen
import com.pbh.androidbase.feature.auth.ui.LoginViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * JVM Compose test for [LoginScreen] backed by Robolectric — CI-friendly, no emulator. The screen
 * is driven with an explicit [LoginViewModel] (real, over a fake repository) so no Hilt graph is
 * needed. Verifies the base-tier [com.pbh.androidbase.core.ui.BaseScreen] wiring renders the form.
 *
 * Lives in `src/testDebug` rather than `src/test` on purpose: `createComposeRule` launches
 * `androidx.activity.ComponentActivity`, which is declared by the `ui-test-manifest` artifact, and
 * that artifact is `debugImplementation` so its manifest entry merges into debug variants only.
 * In `src/test` the test also runs under `testProdReleaseUnitTest`, where the activity is absent
 * and Robolectric fails with "Unable to resolve activity for Intent".
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LoginScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersTitleAndSignInAction() {
        composeRule.setContent {
            AndroidBaseTheme {
                LoginScreen(
                    onLoginComplete = {},
                    viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository())),
                )
            }
        }

        composeRule.onNodeWithText("Sign in").assertIsDisplayed()
    }
}

private class FakeAuthRepository : AuthRepository {
    override fun observeSession(): Flow<UserSession?> = flowOf(null)

    override suspend fun login(
        email: String,
        password: String,
    ): AppResult<UserSession> = AppResult.Success(UserSession(userId = "u", email = email, token = "t"))

    override suspend fun logout() = Unit
}
