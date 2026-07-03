package com.pbh.androidbase.feature.home.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.usecase.GetItemDetailUseCase
import com.pbh.androidbase.feature.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel
    @Inject
    constructor(
        private val getItemDetailUseCase: GetItemDetailUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
        val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

        fun load(itemId: String) {
            viewModelScope.launch {
                _uiState.value =
                    when (val result = getItemDetailUseCase(itemId)) {
                        is AppResult.Success -> ItemDetailUiState.Content(result.data)
                        is AppResult.Failure -> ItemDetailUiState.Error(UiText.Resource(R.string.home_error_item_not_found))
                    }
            }
        }
    }
