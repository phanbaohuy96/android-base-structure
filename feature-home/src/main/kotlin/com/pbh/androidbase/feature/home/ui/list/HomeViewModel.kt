package com.pbh.androidbase.feature.home.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pbh.androidbase.core.ui.UiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.usecase.GetItemsUseCase
import com.pbh.androidbase.domain.usecase.LogoutUseCase
import com.pbh.androidbase.domain.usecase.RefreshItemsUseCase
import com.pbh.androidbase.feature.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        getItemsUseCase: GetItemsUseCase,
        private val refreshItemsUseCase: RefreshItemsUseCase,
        private val logoutUseCase: LogoutUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<HomeListUiState>(HomeListUiState.Loading)
        val uiState: StateFlow<HomeListUiState> = _uiState.asStateFlow()

        private val effects = Channel<HomeListEffect>(Channel.BUFFERED)
        val effectFlow = effects.receiveAsFlow()

        init {
            viewModelScope.launch {
                getItemsUseCase().collect { items ->
                    _uiState.value = HomeListUiState.Content(items = items)
                }
            }
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                val current = _uiState.value
                if (current is HomeListUiState.Content) {
                    _uiState.value = current.copy(refreshing = true)
                }
                when (val result = refreshItemsUseCase()) {
                    is AppResult.Success -> Unit
                    is AppResult.Failure -> effects.send(HomeListEffect.ShowMessage(result.error.toMessage()))
                }
                val after = _uiState.value
                if (after is HomeListUiState.Content) {
                    _uiState.value = after.copy(refreshing = false)
                }
            }
        }

        fun logout() {
            viewModelScope.launch {
                logoutUseCase()
                effects.send(HomeListEffect.LoggedOut)
            }
        }

        private fun DomainError.toMessage(): UiText =
            when (this) {
                DomainError.NotFound -> UiText.Resource(R.string.home_error_item_not_found)
                DomainError.Offline -> UiText.Resource(R.string.home_error_offline_cache)
                DomainError.Unauthorized -> UiText.Resource(R.string.home_error_session_expired)
                is DomainError.Remote -> UiText.Resource(R.string.home_error_generic)
                DomainError.Unknown -> UiText.Resource(R.string.home_error_generic)
                is DomainError.Validation -> UiText.Resource(R.string.home_error_generic)
            }
    }
