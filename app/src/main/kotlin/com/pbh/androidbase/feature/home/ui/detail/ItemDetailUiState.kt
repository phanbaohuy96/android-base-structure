package com.pbh.androidbase.feature.home.ui.detail

import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item

/** Exhaustive UI states for the item detail screen. */
sealed interface ItemDetailUiState {
    /** Detail load is in progress. */
    data object Loading : ItemDetailUiState

    /** Detail item was found. */
    data class Content(
        val item: Item,
    ) : ItemDetailUiState

    /** Detail load failed with a user-facing [message]. */
    data class Error(
        val message: UiText,
    ) : ItemDetailUiState
}
