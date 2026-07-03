package com.pbh.androidbase.feature.auth.ui

import com.pbh.androidbase.core.ui.UiText

sealed interface LoginUiState {
    data object Idle : LoginUiState

    data object Loading : LoginUiState

    data object Success : LoginUiState

    data class Error(
        val message: UiText,
    ) : LoginUiState
}

sealed interface LoginEffect {
    data object NavigateHome : LoginEffect

    data class ShowMessage(
        val message: UiText,
    ) : LoginEffect
}
