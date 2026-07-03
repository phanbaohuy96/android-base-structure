package com.pbh.androidbase.feature.home.ui.list

import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item

sealed interface HomeListUiState {
    data object Loading : HomeListUiState

    data class Content(
        val items: List<Item>,
        val refreshing: Boolean = false,
    ) : HomeListUiState

    data class Error(
        val message: UiText,
    ) : HomeListUiState
}

sealed interface HomeListEffect {
    data class ShowMessage(
        val message: UiText,
    ) : HomeListEffect

    data object LoggedOut : HomeListEffect
}
