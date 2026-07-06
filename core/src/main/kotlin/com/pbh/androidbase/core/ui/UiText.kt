package com.pbh.androidbase.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** User-facing text that can be dynamic or resolved from Android string resources. */
sealed interface UiText {
    /** Runtime string that does not need resource lookup. */
    data class Dynamic(
        val value: String,
    ) : UiText

    /** String resource reference resolved by Composables or Android [Context]. */
    data class Resource(
        @param:StringRes val id: Int,
    ) : UiText

    /** Resolves this text inside composition. */
    @Composable
    fun asString(): String =
        when (this) {
            is Dynamic -> value
            is Resource -> stringResource(id)
        }

    /** Resolves this text from a non-composable Android [context]. */
    fun asString(context: Context): String =
        when (this) {
            is Dynamic -> value
            is Resource -> context.getString(id)
        }
}
