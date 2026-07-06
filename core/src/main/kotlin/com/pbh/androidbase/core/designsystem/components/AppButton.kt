package com.pbh.androidbase.core.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pbh.androidbase.core.designsystem.AppTheme

/** Primary action button with the app minimum touch target and optional loading state. */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: @Composable RowScope.() -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = AppTheme.decoration.minTouchTarget),
        enabled = enabled && !loading,
    ) {
        leadingIcon()
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(AppTheme.decoration.loadingIndicatorSize),
                color = AppTheme.colors.onPrimary,
                strokeWidth = AppTheme.decoration.loadingIndicatorStrokeWidth,
            )
        } else {
            Text(text)
        }
    }
}

/** Text button variant using the app minimum touch target. */
@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = AppTheme.decoration.minTouchTarget),
        enabled = enabled,
    ) {
        Text(text)
    }
}
