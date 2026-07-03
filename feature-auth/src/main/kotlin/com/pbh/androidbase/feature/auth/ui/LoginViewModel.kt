package com.pbh.androidbase.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.model.ValidationReason
import com.pbh.androidbase.domain.usecase.LoginUseCase
import com.pbh.androidbase.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        private val effects = Channel<LoginEffect>(Channel.BUFFERED)
        val effectFlow = effects.receiveAsFlow()

        fun login(
            email: String,
            password: String,
        ) {
            if (_uiState.value == LoginUiState.Loading) return
            viewModelScope.launch {
                _uiState.value = LoginUiState.Loading
                when (val result = loginUseCase(email, password)) {
                    is AppResult.Success -> {
                        _uiState.value = LoginUiState.Success
                        effects.send(LoginEffect.NavigateHome)
                    }
                    is AppResult.Failure -> {
                        val message = result.error.toMessage()
                        _uiState.value = LoginUiState.Error(message)
                        effects.send(LoginEffect.ShowMessage(message))
                    }
                }
            }
        }

        private fun DomainError.toMessage(): UiText =
            when (this) {
                DomainError.NotFound -> UiText.Resource(R.string.login_error_account_not_found)
                DomainError.Offline -> UiText.Resource(R.string.login_error_offline)
                DomainError.Unauthorized -> UiText.Resource(R.string.login_error_invalid_credentials)
                is DomainError.Remote -> UiText.Resource(R.string.login_error_generic)
                DomainError.Unknown -> UiText.Resource(R.string.login_error_generic)
                is DomainError.Validation ->
                    when (reason) {
                        ValidationReason.InvalidEmail -> UiText.Resource(R.string.login_error_invalid_email)
                        ValidationReason.PasswordTooShort -> UiText.Resource(R.string.login_error_password_too_short)
                    }
            }
    }
