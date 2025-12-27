package com.cthinhdzs1tg.meomaybeooptimizer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Build a dark color scheme using our defined palette.
private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = AccentColor,
    onPrimary = Color.White,
    secondary = AccentColor,
    onSecondary = Color.White,
    tertiary = AccentColor,
    onTertiary = Color.White,
    background = DarkGradientStart,
    onBackground = TextPrimary,
    surface = DarkCard,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = AccentColor.copy(alpha = 0.5f),
)

@Composable
fun MeoMayBeOptimizerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}