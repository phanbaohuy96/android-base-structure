package com.pbh.androidbase.core.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Complete theme configuration injected into [AndroidBaseTheme]. */
@Immutable
data class AppThemeConfig(
    val colorScheme: ColorScheme,
    val typography: Typography,
    val shapes: Shapes,
    val spacing: AppSpacing,
    val decoration: AppDecoration,
)

/** Shared spacing scale for screens and reusable components. */
@Immutable
data class AppSpacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
    val screen: Dp = 24.dp,
)

/** Shared non-color decoration tokens used by app components. */
@Immutable
data class AppDecoration(
    val minTouchTarget: Dp = 48.dp,
    val loadingIndicatorSize: Dp = 18.dp,
    val loadingIndicatorStrokeWidth: Dp = 2.dp,
    val cardElevation: Dp = 1.dp,
    val focusedBorderWidth: Dp = 2.dp,
)

private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
private val LocalAppDecoration = staticCompositionLocalOf { AppDecoration() }
private val LocalAppThemeConfig =
    staticCompositionLocalOf {
        AppThemeDefaults.light()
    }

/** Composition-local accessors for the active app theme tokens. */
object AppTheme {
    /** Current Material color scheme. */
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /** Current Material typography scale. */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /** Current Material shapes. */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /** Current app spacing scale. */
    val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSpacing.current

    /** Current app decoration tokens. */
    val decoration: AppDecoration
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDecoration.current

    /** Full theme configuration currently provided to the tree. */
    val config: AppThemeConfig
        @Composable
        @ReadOnlyComposable
        get() = LocalAppThemeConfig.current
}

/** Default light and dark theme configurations for the starter app. */
object AppThemeDefaults {
    /** Returns the default light theme configuration. */
    fun light(): AppThemeConfig =
        AppThemeConfig(
            colorScheme =
                lightColorScheme(
                    primary = Color(0xFF1D5B79),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFD2ECF7),
                    onPrimaryContainer = Color(0xFF062E40),
                    secondary = Color(0xFF7A4E2D),
                    onSecondary = Color.White,
                    secondaryContainer = Color(0xFFFFDBC5),
                    tertiary = Color(0xFF2D6A4F),
                    background = Color(0xFFF8F9FB),
                    onBackground = Color(0xFF171C20),
                    surface = Color.White,
                    onSurface = Color(0xFF171C20),
                    surfaceVariant = Color(0xFFE1E6EA),
                    onSurfaceVariant = Color(0xFF41484D),
                    error = Color(0xFFBA1A1A),
                ),
            typography = appTypography(),
            shapes = Shapes(),
            spacing = AppSpacing(),
            decoration = AppDecoration(),
        )

    /** Returns the default dark theme configuration. */
    fun dark(): AppThemeConfig =
        AppThemeConfig(
            colorScheme =
                darkColorScheme(
                    primary = Color(0xFF8CCCE8),
                    onPrimary = Color(0xFF003548),
                    primaryContainer = Color(0xFF004D67),
                    onPrimaryContainer = Color(0xFFC2E8FF),
                    secondary = Color(0xFFE0B28A),
                    onSecondary = Color(0xFF452A14),
                    secondaryContainer = Color(0xFF604027),
                    tertiary = Color(0xFF95D5B2),
                    background = Color(0xFF111417),
                    onBackground = Color(0xFFE1E3E6),
                    surface = Color(0xFF1A1E22),
                    onSurface = Color(0xFFE1E3E6),
                    surfaceVariant = Color(0xFF41484D),
                    onSurfaceVariant = Color(0xFFC1C7CD),
                    error = Color(0xFFFFB4AB),
                ),
            typography = appTypography(),
            shapes = Shapes(),
            spacing = AppSpacing(),
            decoration = AppDecoration(),
        )
}

private fun appTypography(): Typography =
    Typography(
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
    )

/** Provides [config] to Compose and applies the matching Material theme. */
@Composable
fun AndroidBaseTheme(
    config: AppThemeConfig = AppThemeDefaults.light(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppThemeConfig provides config,
        LocalAppSpacing provides config.spacing,
        LocalAppDecoration provides config.decoration,
    ) {
        MaterialTheme(
            colorScheme = config.colorScheme,
            typography = config.typography,
            shapes = config.shapes,
            content = content,
        )
    }
}
