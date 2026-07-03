package com.pbh.androidbase.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Dynamic(
        val value: String,
    ) : UiText

    data class Resource(
        @param:StringRes val id: Int,
    ) : UiText

    @Composable
    fun asString(): String =
        when (this) {
            is Dynamic -> value
            is Resource -> stringResource(id)
        }

    fun asString(context: Context): String =
        when (this) {
            is Dynamic -> value
            is Resource -> context.getString(id)
        }
}
