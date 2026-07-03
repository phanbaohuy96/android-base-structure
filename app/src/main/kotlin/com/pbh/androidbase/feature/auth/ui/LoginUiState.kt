package com.pbh.androidbase.feature.auth.ui

import com.pbh.androidbase.core.ui.MessageEffect
import com.pbh.androidbase.core.ui.UiEffect
import com.pbh.androidbase.core.ui.UiText

sealed interface LoginUiState {
    data object Idle : LoginUiState

    data object Loading : LoginUiState

    data object Success : LoginUiState

    data class Error(
        val message: UiText,
    ) : LoginUiState
}

sealed interface LoginEffect : UiEffect {
    data object NavigateHome : LoginEffect

    data class ShowMessage(
        override val message: UiText,
    ) : LoginEffect,
        MessageEffect
}
