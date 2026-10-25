package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TerracottaRed,
    onPrimary = Color.White,
    primaryContainer = TerracottaLight,
    onPrimaryContainer = TerracottaDark,
    secondary = WineBurgundy,
    onSecondary = Color.White,
    secondaryContainer = WineLight,
    onSecondaryContainer = WineBurgundy,
    tertiary = SageGreen,
    onTertiary = Color.White,
    tertiaryContainer = SageLight,
    onTertiaryContainer = SageGreen,
    background = WarmCreamBg,
    onBackground = CharcoalDark,
    surface = WarmLinenSurface,
    onSurface = CharcoalDark,
    surfaceVariant = WarmAlmondVariant,
    onSurfaceVariant = WarmTaupe,
    outline = WarmTaupe
)

@Composable
fun Only4AlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme, // Warm, cozy theme for arts & culture
        typography = Typography,
        content = content
    )
}
