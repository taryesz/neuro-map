package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val Background = Color(0xFFF2F3E7)
val onBackground = Color(0xFF123F44)

val Primary = Color(0xFF123F44)
val onPrimary = Color(0xFFF2F3E7)

val Surface = Color(0xFFD0D3A8)
val onSurface = Color(0xFF123F44)

val SurfaceVariant = Color(0xFF34C759)
val SurfaceDim = Color(0xFFB3B3B3)

val ProfileIcon = Color(0xFF595959)

val DarkBg = Color(0xFF121212)
val DarkCard = Color(0xFF1E1E1E)
val DarkPrimary = Color(0xFF6DA3A9)

val Logout = Color(0xFFb2251e)

val LightColors = lightColorScheme(
    background = Background,
    onBackground = onBackground,
    primary = Primary,
    onPrimary = onPrimary,
    surface = Background,
    onSurface = onBackground,
    surfaceVariant = SurfaceVariant,
    surfaceDim = SurfaceDim,
    primaryContainer = Primary,
    onPrimaryContainer = onPrimary
)

val DarkColors = darkColorScheme(
    background = DarkBg,
    onBackground = Background,
    primary = DarkPrimary,
    onPrimary = DarkBg,
    surface = DarkCard,
    onSurface = Background,
    surfaceVariant = SurfaceVariant,
    surfaceDim = Color(0xFF333333),
    primaryContainer = Primary,
    onPrimaryContainer = onPrimary
)