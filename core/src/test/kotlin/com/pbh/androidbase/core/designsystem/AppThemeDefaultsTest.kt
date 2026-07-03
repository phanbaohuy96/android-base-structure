package com.pbh.androidbase.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals

class AppThemeDefaultsTest {
    @Test
    fun `light theme exposes configurable color typography spacing and decoration tokens`() {
        val config = AppThemeDefaults.light()

        assertEquals(Color(0xFF1D5B79), config.colorScheme.primary)
        assertEquals(28.sp, config.typography.headlineMedium.fontSize)
        assertEquals(24.dp, config.spacing.screen)
        assertEquals(18.dp, config.decoration.loadingIndicatorSize)
        assertEquals(2.dp, config.decoration.loadingIndicatorStrokeWidth)
    }

    @Test
    fun `dark theme exposes dark color tokens with same structural configuration`() {
        val config = AppThemeDefaults.dark()

        assertEquals(Color(0xFF8CCCE8), config.colorScheme.primary)
        assertEquals(28.sp, config.typography.headlineMedium.fontSize)
        assertEquals(24.dp, config.spacing.screen)
        assertEquals(1.dp, config.decoration.cardElevation)
    }
}
