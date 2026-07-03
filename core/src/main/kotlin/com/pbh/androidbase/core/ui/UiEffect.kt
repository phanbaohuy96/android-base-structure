package com.pbh.androidbase.core.ui

/**
 * Marker for one-off UI effects a [BaseViewModel] emits and a [BaseScreen] observes
 * (navigation, snackbars, dialogs). Effects are consumed once and never replayed.
 *
 * Feature effects declare their own sealed hierarchy on top of this marker, e.g.
 *
 * ```kotlin
 * sealed interface LoginEffect : UiEffect {
 *     data object NavigateHome : LoginEffect
 *     data class ShowMessage(override val message: UiText) : LoginEffect, MessageEffect
 * }
 * ```
 */
interface UiEffect

/**
 * A [UiEffect] that carries a user-facing [message]. [BaseScreen] automatically shows any
 * [MessageEffect] in the snackbar, so features get message display for free — they only need
 * to handle their navigation-style effects in `onEffect`.
 */
interface MessageEffect : UiEffect {
    val message: UiText
}
