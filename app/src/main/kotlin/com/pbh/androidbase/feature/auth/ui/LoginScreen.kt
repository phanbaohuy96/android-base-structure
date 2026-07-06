package com.pbh.androidbase.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pbh.androidbase.R
import com.pbh.androidbase.core.designsystem.AppTheme
import com.pbh.androidbase.core.designsystem.components.AppButton
import com.pbh.androidbase.core.designsystem.components.AppTextField
import com.pbh.androidbase.core.ui.BaseScreen

/** Login form screen that renders auth state and delegates sign-in events to [LoginViewModel]. */
@Composable
fun LoginScreen(
    onLoginComplete: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val defaultEmail = stringResource(R.string.login_default_email)
    val defaultPassword = stringResource(R.string.login_default_password)
    var email by remember(defaultEmail) { mutableStateOf(defaultEmail) }
    var password by remember(defaultPassword) { mutableStateOf(defaultPassword) }

    BaseScreen(
        viewModel = viewModel,
        onEffect = { effect ->
            when (effect) {
                LoginEffect.NavigateHome -> onLoginComplete()
                is LoginEffect.ShowMessage -> Unit // shown by BaseScreen
            }
        },
    ) { state, padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.spacing.screen),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = AppTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.login_subtitle),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(AppTheme.spacing.extraLarge))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.login_email_label),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(Modifier.height(AppTheme.spacing.medium))
            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.login_password_label),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(Modifier.height(AppTheme.spacing.large))
            AppButton(
                text = stringResource(R.string.login_sign_in),
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                loading = state == LoginUiState.Loading,
            )
            if (state is LoginUiState.Error) {
                Spacer(Modifier.height(AppTheme.spacing.medium))
                Text(
                    text = state.message.asString(),
                    color = AppTheme.colors.error,
                )
            }
        }
    }
}
