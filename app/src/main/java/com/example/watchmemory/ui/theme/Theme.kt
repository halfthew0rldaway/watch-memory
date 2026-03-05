package com.example.watchmemory.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BrutalColors(
    val border: Color,
    val cardBg: Color,
    val accent: Color,
    val shadow: Color,
    val surface: Color,
    val borderWidth: Dp = 3.dp,
    val shadowOffset: Dp = 5.dp
)

val LocalBrutalColors = staticCompositionLocalOf {
    BrutalColors(
        border = BrutalBlack,
        cardBg = BrutalWhite,
        accent = BrutalYellow,
        shadow = BrutalBlack,
        surface = BrutalBackground
    )
}

private val LightColorScheme = lightColorScheme(
    primary = BrutalBlack,
    onPrimary = BrutalWhite,
    secondary = BrutalYellow,
    onSecondary = BrutalBlack,
    background = BrutalBackground,
    onBackground = BrutalBlack,
    surface = BrutalWhite,
    onSurface = BrutalBlack,
    onSurfaceVariant = Color(0xFF555555),
    error = Color(0xFFE53935),
    surfaceVariant = Color(0xFFEEEEEE)
)

private val LightBrutalColors = BrutalColors(
    border = BrutalBlack,
    cardBg = BrutalWhite,
    accent = BrutalYellow,
    shadow = BrutalBlack,
    surface = BrutalBackground
)

@Composable
fun WatchMemoryTheme(
    darkTheme: Boolean = false, // Forced Light Mode
    content: @Composable () -> Unit
) {
    // We ignore darkTheme parameter as requested by user to remove "ass" dark mode
    val colorScheme = LightColorScheme
    val brutalColors = LightBrutalColors

    CompositionLocalProvider(LocalBrutalColors provides brutalColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = WatchMemoryTypography,
            content = content
        )
    }
}
