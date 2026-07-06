package com.pbh.androidbase.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

/** Collects one-off [events] in composition and forwards each value to [onEvent]. */
@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    onEvent: suspend (T) -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event -> onEvent(event) }
    }
}
