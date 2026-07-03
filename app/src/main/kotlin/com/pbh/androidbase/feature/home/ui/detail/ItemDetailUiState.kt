package com.pbh.androidbase.feature.home.ui.detail

import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.entity.Item

sealed interface ItemDetailUiState {
    data object Loading : ItemDetailUiState

    data class Content(
        val item: Item,
    ) : ItemDetailUiState

    data class Error(
        val message: UiText,
    ) : ItemDetailUiState
}
