package com.project.tripplanner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Indian-inspired Color Palette
object YatraColors {
    val SaffronPrimary = Color(0xFFFF7043)
    val SaffronDark = Color(0xFFD84315)
    val SaffronLight = Color(0xFFFFAB91)

    val TurquoiseSecondary = Color(0xFF26C6DA)
    val TurquoiseDark = Color(0xFF00ACC1)
    val TurquoiseLight = Color(0xFF80DEEA)

    val EarthyBrown = Color(0xFF8D6E63)
    val WarmGold = Color(0xFFFFB74D)
    val CreamWhite = Color(0xFFFFF8E1)
    val ForestGreen = Color(0xFF66BB6A)

    val Surface = Color(0xFFFFFBF7)
    val Background = Color(0xFFFFF4E0)
    val OnSurface = Color(0xFF3E2723)
    val TextSecondary = Color(0xFF8D6E63)
}

private val YatraColorScheme = lightColorScheme(
    primary = YatraColors.SaffronPrimary,
    onPrimary = Color.White,
    secondary = YatraColors.TurquoiseSecondary,
    onSecondary = Color.White,
    tertiary = YatraColors.WarmGold,
    background = YatraColors.Background,
    surface = YatraColors.Surface,
    onBackground = YatraColors.OnSurface,
    onSurface = YatraColors.OnSurface,
    surfaceVariant = YatraColors.CreamWhite
)

// Custom Typography
private val YatraTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = YatraColors.SaffronDark
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = YatraColors.SaffronDark
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = YatraColors.OnSurface
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = YatraColors.OnSurface
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = YatraColors.TextSecondary
    )
)

@Composable
fun YatraBotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = YatraColorScheme,
        typography = YatraTypography,
        content = content
    )
}
