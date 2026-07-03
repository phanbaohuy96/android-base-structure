package com.pbh.androidbase.core.ui

import com.pbh.androidbase.core.R
import com.pbh.androidbase.domain.model.DomainError
import com.pbh.androidbase.domain.model.ValidationReason

/**
 * Central error-to-text policy — the single place that turns a [DomainError] into a localized
 * [UiText]. This is the Android analog of the Flutter template's `state_base.error_handler.dart`:
 * one exhaustive `when` instead of a per-feature copy. Features may still map a specific error to
 * a bespoke message where the UX warrants it, but this is the default every screen falls back to.
 */
fun DomainError.toUiText(): UiText =
    when (this) {
        DomainError.Unauthorized -> UiText.Resource(R.string.core_error_unauthorized)
        DomainError.Offline -> UiText.Resource(R.string.core_error_offline)
        DomainError.NotFound -> UiText.Resource(R.string.core_error_not_found)
        is DomainError.Validation ->
            when (reason) {
                ValidationReason.InvalidEmail -> UiText.Resource(R.string.core_error_invalid_email)
                ValidationReason.PasswordTooShort -> UiText.Resource(R.string.core_error_password_too_short)
            }
        is DomainError.Remote -> UiText.Resource(R.string.core_error_generic)
        DomainError.Unknown -> UiText.Resource(R.string.core_error_generic)
    }
