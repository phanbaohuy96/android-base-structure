package com.pbh.androidbase.core.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pbh.androidbase.core.designsystem.AppTheme

/** App-level scaffold wrapper with optional top bar, actions, snackbar host, and content padding. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    actions: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (title != null) {
                TopAppBar(
                    title = { Text(title) },
                    actions = { actions() },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = AppTheme.colors.surface,
                            titleContentColor = AppTheme.colors.onSurface,
                            actionIconContentColor = AppTheme.colors.primary,
                        ),
                )
            }
        },
        snackbarHost = snackbarHost,
        content = content,
    )
}
