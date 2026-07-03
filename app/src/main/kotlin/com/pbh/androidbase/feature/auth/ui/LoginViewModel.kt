package com.pbh.androidbase.feature.auth.ui

import com.pbh.androidbase.core.ui.BaseViewModel
import com.pbh.androidbase.core.ui.toUiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
    ) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState.Idle) {
        fun login(
            email: String,
            password: String,
        ) {
            if (currentState == LoginUiState.Loading) return
            launch {
                setState { LoginUiState.Loading }
                when (val result = loginUseCase(email, password)) {
                    is AppResult.Success -> {
                        setState { LoginUiState.Success }
                        sendEffect(LoginEffect.NavigateHome)
                    }
                    is AppResult.Failure -> {
                        val message = result.error.toUiText()
                        setState { LoginUiState.Error(message) }
                        sendEffect(LoginEffect.ShowMessage(message))
                    }
                }
            }
        }
    }
