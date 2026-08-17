package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GamingColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = BackgroundDark,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = TextPrimary,
    secondary = NeonCrimson,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceElevatedDark,
    onSecondaryContainer = CyberCyan,
    tertiary = GamerAmber,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderCyan
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GamingColorScheme,
        typography = Typography,
        content = content
    )
}
