package com.pbh.androidbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pbh.androidbase.domain.usecase.ObserveSessionUseCase
import com.pbh.androidbase.navigation.StartRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * App-level ViewModel that resolves the start destination from the persisted session — the auth
 * guard. While [startRoute] is `null` the UI shows a splash; once the session flow emits, the app
 * routes to [StartRoute.Home] (signed in) or [StartRoute.Login]. This is the real consumer of
 * [ObserveSessionUseCase].
 */
@HiltViewModel
class AppViewModel
    @Inject
    constructor(
        observeSession: ObserveSessionUseCase,
    ) : ViewModel() {
        /** Null until the session guard resolves, then the top-level destination to show. */
        val startRoute: StateFlow<StartRoute?> =
            observeSession()
                .map { session -> if (session != null) StartRoute.Home else StartRoute.Login }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                    initialValue = null,
                )

        private companion object {
            const val STOP_TIMEOUT_MILLIS = 5_000L
        }
    }
