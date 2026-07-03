package com.pbh.androidbase.feature.home.ui.list

import com.pbh.androidbase.core.ui.BaseViewModel
import com.pbh.androidbase.core.ui.toUiText
import com.pbh.androidbase.domain.model.AppResult
import com.pbh.androidbase.domain.usecase.GetItemsUseCase
import com.pbh.androidbase.domain.usecase.LogoutUseCase
import com.pbh.androidbase.domain.usecase.RefreshItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        getItemsUseCase: GetItemsUseCase,
        private val refreshItemsUseCase: RefreshItemsUseCase,
        private val logoutUseCase: LogoutUseCase,
    ) : BaseViewModel<HomeListUiState, HomeListEffect>(HomeListUiState.Loading) {
        init {
            launch {
                getItemsUseCase().collect { items ->
                    setState { HomeListUiState.Content(items = items) }
                }
            }
            refresh()
        }

        fun refresh() {
            launch {
                setState { if (this is HomeListUiState.Content) copy(refreshing = true) else this }
                when (val result = refreshItemsUseCase()) {
                    is AppResult.Success -> Unit
                    is AppResult.Failure -> sendEffect(HomeListEffect.ShowMessage(result.error.toUiText()))
                }
                setState { if (this is HomeListUiState.Content) copy(refreshing = false) else this }
            }
        }

        fun logout() {
            launch {
                logoutUseCase()
                sendEffect(HomeListEffect.LoggedOut)
            }
        }
    }
