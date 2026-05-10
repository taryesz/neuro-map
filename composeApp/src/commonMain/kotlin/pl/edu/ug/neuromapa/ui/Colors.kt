package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
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

// Secondary button
val SurfaceDim = Color(0xFFb3b3b3)


val DarkBackground = Color(0xFF0D1F21)
val DarkOnBackground = Color(0xFFF2F3E7)
val DarkPrimary = Color(0xFF6DA3A9)
val DarkSurface = Color(0xFF1E3A3D)

val LightColors = lightColorScheme(
    background = Background,
    onBackground = onBackground,
    primary = Primary,
    onPrimary = onPrimary,
    surface = Surface,
    onSurface = onSurface,
    surfaceVariant = SurfaceVariant,
    surfaceDim = SurfaceDim,
)

val DarkColors = darkColorScheme(
    background = DarkBackground,
    onBackground = DarkOnBackground,
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    surfaceVariant = Color(0xFF28A745)
)


val ProfileIcon = Color(0xFF595959)
