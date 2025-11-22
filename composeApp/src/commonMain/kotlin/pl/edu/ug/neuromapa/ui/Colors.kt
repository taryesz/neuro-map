package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Beige main color
val Background = Color(0xFFF2F3E7)
val onBackground = Color(0xFF123F44)

// Deep green accent color
val Primary = Color(0xFF123F44)
val onPrimary = Color(0xFFF2F3E7)

// Selection color of an item in the navigation bar
val Surface = Color(0xFFD0D3A8)
val onSurface = Color(0xFF123F44)

// Light green button
val SurfaceVariant = Color(0xFF34C759)

val LightColors = lightColorScheme(
    background = Background,
    onBackground = onBackground,
    primary = Primary,
    onPrimary = onPrimary,
    surface = Surface,
    onSurface = onSurface,
    surfaceVariant = SurfaceVariant,
)

val ProfileIcon = Color(0xFF595959)
