package com.pbh.androidbase.feature.auth.ui

import com.pbh.androidbase.core.ui.MessageEffect
import com.pbh.androidbase.core.ui.UiEffect
import com.pbh.androidbase.core.ui.UiText

/** Exhaustive UI states for the login screen. */
sealed interface LoginUiState {
    /** Initial editable form state. */
    data object Idle : LoginUiState

    /** Login request is in progress. */
    data object Loading : LoginUiState

    /** Login succeeded and navigation will be emitted. */
    data object Success : LoginUiState

    /** Login failed with a user-facing [message]. */
    data class Error(
        val message: UiText,
    ) : LoginUiState
}

/** One-off login actions consumed by [LoginScreen]. */
sealed interface LoginEffect : UiEffect {
    /** Navigate away from login after a successful session is saved. */
    data object NavigateHome : LoginEffect

    /** Show [message] through the shared snackbar path. */
    data class ShowMessage(
        override val message: UiText,
    ) : LoginEffect,
        MessageEffect
}
