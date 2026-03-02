package com.example.easygame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = Emerald,
    onPrimary = White,
    primaryContainer = MotherboardGreen,
    secondary = Cosmos,
    onSecondary = White,
    tertiary = FreshGold,
    background = CoolGreen,
    surface = White,
    outlineVariant = WhiteMarble,
    onSurface = Hydrocarbon,
    surfaceVariant = TitaniumWhite,
    onSurfaceVariant = SharkGray,
    outline = SilverSteel,
    error = BlazeRed,
    errorContainer = BabyBottom,
    onErrorContainer = LightPinkRose
)

@Composable
fun GameTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
