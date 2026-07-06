package com.pbh.androidbase.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pbh.androidbase.core.network.NetworkErrorMapper
import com.pbh.androidbase.domain.model.DomainError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base for every feature ViewModel. Centralizes the unidirectional-data-flow plumbing that was
 * previously hand-rolled in each ViewModel: a single [state] `StateFlow` and a one-off [effects]
 * channel, plus a [launch] helper that normalizes thrown errors to [DomainError].
 *
 * Subclasses drive the UI with [setState] and [sendEffect]; they never touch the backing flows.
 *
 * @param S immutable UI state, typically a sealed interface exhaustively describing the screen.
 * @param E one-off UI effects; implement [UiEffect]. Use [Nothing] for screens with no effects.
 */
abstract class BaseViewModel<S, E : UiEffect>(
    initialState: S,
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    /** Snapshot of the current [state], for read-modify-write logic inside a coroutine. */
    protected val currentState: S get() = _state.value

    /** Atomically transforms the current state. */
    protected fun setState(reduce: S.() -> S) = _state.update(reduce)

    /** Enqueues a one-off [effect] for the screen to consume exactly once. */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effects.send(effect) }
    }

    /**
     * Runs [block] on [viewModelScope]. Rethrows [CancellationException] so structured
     * concurrency keeps working; any other throwable is normalized via [NetworkErrorMapper] and
     * handed to [onError]. The default fails loud so unexpected bugs are not silently swallowed;
     * callers that expect thrown infrastructure failures should pass an explicit handler.
     */
    @Suppress("TooGenericExceptionCaught")
    protected fun launch(
        onError: (DomainError) -> Unit = { error("Unhandled ViewModel failure: $it") },
        block: suspend CoroutineScope.() -> Unit,
    ): Job =
        viewModelScope.launch {
            try {
                block()
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (throwable: Throwable) {
                onError(NetworkErrorMapper.map(throwable))
            }
        }
}
