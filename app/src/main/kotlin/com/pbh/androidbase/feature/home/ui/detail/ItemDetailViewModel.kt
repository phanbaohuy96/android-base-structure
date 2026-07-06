package com.pbh.androidbase.feature.home.ui.detail

import com.pbh.androidbase.core.ui.BaseViewModel
import com.pbh.androidbase.core.ui.toUiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.usecase.GetItemDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** ViewModel that loads one item for the detail screen. */
@HiltViewModel
class ItemDetailViewModel
    @Inject
    constructor(
        private val getItemDetailUseCase: GetItemDetailUseCase,
    ) : BaseViewModel<ItemDetailUiState, Nothing>(ItemDetailUiState.Loading) {
        /** Loads the item matching [itemId] and maps failures to UI text. */
        fun load(itemId: String) {
            launch {
                val result = getItemDetailUseCase(itemId)
                setState {
                    when (result) {
                        is AppResult.Success -> ItemDetailUiState.Content(result.data)
                        is AppResult.Failure -> ItemDetailUiState.Error(result.error.toUiText())
                    }
                }
            }
        }
    }
