// AppTheme.kt
package com.example.travelapp

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette
object AppColors {
    val Primary = Color(0xFF2196F3)
    val PrimaryVariant = Color(0xFF1976D2)
    val Secondary = Color(0xFF03DAC6)
    val SecondaryVariant = Color(0xFF018786)
    val Background = Color(0xFFF5F5F5)
    val Surface = Color(0xFFFFFFFF)
    val Error = Color(0xFFB00020)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFF000000)
    val OnBackground = Color(0xFF000000)
    val OnSurface = Color(0xFF000000)
    val OnError = Color(0xFFFFFFFF)

    // Custom App Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Info = Color(0xFF2196F3)
    val SurfaceVariant = Color(0xFFF8F9FA)
    val Outline = Color(0xFFE0E0E0)
    val TextSecondary = Color(0xFF666666)
    val TextHint = Color(0xFF999999)

    // Button Colors
    val ButtonPrimary = Color(0xFF2196F3)
    val ButtonSecondary = Color(0xFF6C757D)
    val ButtonSuccess = Color(0xFF28A745)
    val ButtonWarning = Color(0xFFFFC107)
    val ButtonDanger = Color(0xFFDC3545)
    val ButtonInfo = Color(0xFF17A2B8)
}

// Padding Constants
object AppPadding {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
    val screenHorizontal = 20.dp
    val screenVertical = 16.dp
    val cardPadding = 16.dp
    val buttonPadding = 12.dp
}

// Corner Radius
object AppCornerRadius {
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val extraLarge = 24.dp
    val button = 12.dp
    val card = 16.dp
    val dialog = 20.dp
}

// Elevation
object AppElevation {
    val none = 0.dp
    val small = 2.dp
    val medium = 4.dp
    val large = 8.dp
    val extraLarge = 16.dp
    val card = 6.dp
    val button = 4.dp
    val dialog = 24.dp
}

// Font Sizes
object AppFontSize {
    val extraSmall = 10.sp
    val small = 12.sp
    val medium = 14.sp
    val large = 16.sp
    val extraLarge = 18.sp
    val title = 20.sp
    val heading = 24.sp
    val display = 32.sp
    val buttonText = 14.sp
    val bodyText = 14.sp
    val captionText = 12.sp
}

// Shapes
object AppShapes {
    val small = RoundedCornerShape(AppCornerRadius.small)
    val medium = RoundedCornerShape(AppCornerRadius.medium)
    val large = RoundedCornerShape(AppCornerRadius.large)
    val extraLarge = RoundedCornerShape(AppCornerRadius.extraLarge)
    val button = RoundedCornerShape(AppCornerRadius.button)
    val card = RoundedCornerShape(AppCornerRadius.card)
    val dialog = RoundedCornerShape(AppCornerRadius.dialog)
}

// Typography
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontSize = AppFontSize.display,
        fontWeight = FontWeight.Bold,
        color = AppColors.OnBackground
    ),
    headlineLarge = TextStyle(
        fontSize = AppFontSize.heading,
        fontWeight = FontWeight.Bold,
        color = AppColors.OnBackground
    ),
    titleLarge = TextStyle(
        fontSize = AppFontSize.title,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.OnBackground
    ),
    titleMedium = TextStyle(
        fontSize = AppFontSize.extraLarge,
        fontWeight = FontWeight.Medium,
        color = AppColors.OnBackground
    ),
    bodyLarge = TextStyle(
        fontSize = AppFontSize.large,
        fontWeight = FontWeight.Normal,
        color = AppColors.OnBackground
    ),
    bodyMedium = TextStyle(
        fontSize = AppFontSize.bodyText,
        fontWeight = FontWeight.Normal,
        color = AppColors.OnBackground
    ),
    bodySmall = TextStyle(
        fontSize = AppFontSize.small,
        fontWeight = FontWeight.Normal,
        color = AppColors.TextSecondary
    ),
    labelLarge = TextStyle(
        fontSize = AppFontSize.buttonText,
        fontWeight = FontWeight.Medium,
        color = AppColors.OnPrimary
    ),
    labelMedium = TextStyle(
        fontSize = AppFontSize.medium,
        fontWeight = FontWeight.Medium,
        color = AppColors.TextSecondary
    ),
    labelSmall = TextStyle(
        fontSize = AppFontSize.captionText,
        fontWeight = FontWeight.Normal,
        color = AppColors.TextHint
    )
)

// Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.PrimaryVariant,
    onPrimaryContainer = AppColors.OnPrimary,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    secondaryContainer = AppColors.SecondaryVariant,
    onSecondaryContainer = AppColors.OnSecondary,
    tertiary = AppColors.Info,
    onTertiary = AppColors.OnPrimary,
    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.Error.copy(alpha = 0.1f),
    onErrorContainer = AppColors.Error,
    background = AppColors.Background,
    onBackground = AppColors.OnBackground,
    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.OnSurface,
    outline = AppColors.Outline,
    outlineVariant = AppColors.Outline.copy(alpha = 0.5f)
)

// Main Theme Composable
@Composable
fun TripPlannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}

// Dimension Constants
object AppDimensions {
    val buttonHeight = 48.dp
    val textFieldHeight = 56.dp
    val cardMinHeight = 80.dp
    val iconSize = 24.dp
    val iconSizeSmall = 20.dp
    val iconSizeLarge = 32.dp
    val appBarHeight = 64.dp
    val bottomBarHeight = 80.dp
    val dialogMaxWidth = 400.dp
    val dialogMaxHeight = 600.dp
}