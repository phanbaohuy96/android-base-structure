package com.pbh.androidbase.feature.home.ui.list

import com.pbh.androidbase.core.ui.MessageEffect
import com.pbh.androidbase.core.ui.UiEffect
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item

/** Exhaustive UI states for the home list. */
sealed interface HomeListUiState {
    /** Initial state before cached items emit. */
    data object Loading : HomeListUiState

    /** Visible item list plus a refresh-in-progress flag. */
    data class Content(
        val items: List<Item>,
        val refreshing: Boolean = false,
    ) : HomeListUiState

    /** List-level failure with a user-facing [message]. */
    data class Error(
        val message: UiText,
    ) : HomeListUiState
}

/** One-off effects emitted by the home list. */
sealed interface HomeListEffect : UiEffect {
    /** Show [message] through the shared snackbar path. */
    data class ShowMessage(
        override val message: UiText,
    ) : HomeListEffect,
        MessageEffect

    /** Navigate back to login after the session is cleared. */
    data object LoggedOut : HomeListEffect
}
