package com.pbh.androidbase.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pbh.androidbase.core.designsystem.components.AppScaffold

/**
 * Screen scaffold that pairs with [BaseViewModel] — the Android analog of the Flutter template's
 * `StateBase`. It collects [BaseViewModel.state] with lifecycle awareness, routes
 * [BaseViewModel.effects] (auto-showing any [MessageEffect] in the snackbar), and hosts the
 * content inside [AppScaffold]. Screens supply only their layout via [content]; the collect /
 * observe / snackbar prologue that every screen used to repeat now lives here once.
 *
 * @param title optional top-bar title; when `null` no [androidx.compose.material3.TopAppBar] shows.
 * @param onEffect feature-specific effect handling (e.g. navigation). [MessageEffect]s are already
 *   shown in the snackbar before this runs, so `onEffect` can ignore them.
 * @param content receives the current UI state and the scaffold [PaddingValues].
 */
@Composable
fun <S, E : UiEffect> BaseScreen(
    viewModel: BaseViewModel<S, E>,
    modifier: Modifier = Modifier,
    title: String? = null,
    actions: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEffect: suspend (E) -> Unit = {},
    content: @Composable (state: S, padding: PaddingValues) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(viewModel.effects) { effect ->
        if (effect is MessageEffect) {
            snackbarHostState.showSnackbar(effect.message.asString(context))
        }
        onEffect(effect)
    }
    AppScaffold(
        modifier = modifier,
        title = title,
        actions = actions,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        content(state, padding)
    }
}
