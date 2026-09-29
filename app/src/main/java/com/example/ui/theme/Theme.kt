package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EduBluePrimaryDark,
    onPrimary = EduBlueOnPrimaryDark,
    primaryContainer = EduBlueContainerDark,
    onPrimaryContainer = EduBlueOnContainerDark,
    secondary = EduEmeraldSecondaryDark,
    onSecondary = EduEmeraldOnSecondaryDark,
    secondaryContainer = EduEmeraldContainerDark,
    onSecondaryContainer = EduEmeraldOnContainerDark,
    tertiary = EduAmberTertiaryDark,
    onTertiary = EduAmberOnTertiaryDark,
    tertiaryContainer = EduAmberContainerDark,
    onTertiaryContainer = EduAmberOnContainerDark,
    background = EduBackgroundDark,
    onBackground = EduOnBackgroundDark,
    surface = EduSurfaceDark,
    onSurface = EduOnSurfaceDark,
    surfaceVariant = EduSurfaceVariantDark,
    onSurfaceVariant = EduOnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = EduBluePrimary,
    onPrimary = EduBlueOnPrimary,
    primaryContainer = EduBlueContainerContainer(EduBlueContainer),
    onPrimaryContainer = EduBlueOnContainer,
    secondary = EduEmeraldSecondary,
    onSecondary = EduEmeraldOnSecondary,
    secondaryContainer = EduEmeraldContainer,
    onSecondaryContainer = EduEmeraldOnContainer,
    tertiary = EduAmberTertiary,
    onTertiary = EduAmberOnTertiary,
    tertiaryContainer = EduAmberContainer,
    onTertiaryContainer = EduAmberOnContainer,
    background = EduBackgroundLight,
    onBackground = EduOnBackgroundLight,
    surface = EduSurfaceLight,
    onSurface = EduOnSurfaceLight,
    surfaceVariant = EduSurfaceVariantLight,
    onSurfaceVariant = EduOnSurfaceVariantLight
)

private fun EduBlueContainerContainer(color: androidx.compose.ui.graphics.Color) = color

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to preserve our vibrant child-friendly branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
