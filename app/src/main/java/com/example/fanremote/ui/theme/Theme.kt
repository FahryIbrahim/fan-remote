package com.example.fanremote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE4E4E7),
    secondary = Color(0xFF71717A),
    tertiary = Color(0xFF3B82F6),
    background = Color(0xFF0A0A0A),
    surface = Color(0xFF1A1A1A)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A1A1A),
    secondary = Color(0xFF71717A),
    tertiary = Color(0xFF3B82F6),
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun FanRemoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}