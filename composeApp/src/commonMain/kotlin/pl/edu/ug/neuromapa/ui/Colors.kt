package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val LightColors = lightColorScheme(
    background = Color(0xFFF2F3E7),             // Beige (for main UI components)
    onBackground = Color(0xFF123F44),           // Turquoise (for text)

    primary = Color(0xFF123F44),                // Turquoise (for accent UI components)
    onPrimary = Color(0xFFF2F3E7),              // Beige (for text)

    surface = Color(0xFFD0D3A8),                // Light Green (for tiny accent UI components)
    surfaceVariant = Color(0xFF34C759),         // Vibrant Green (for navigation buttons)
    surfaceBright = Color(0xFF595959),          // Light Gray (for secondary buttons)
    surfaceTint = Color(0xFFb2251e)             // Vibrant Red (for Logout button)
)

val DarkColors = darkColorScheme(
    background = Color(0xFF121212),             // Dark Gray (for main UI components)
    onBackground = Color(0xFFF2F3E7),           // Beige (for text)

    primary = Color(0xFF123F44),                // Turquoise (for accent UI components)
    onPrimary = Color(0xFFF2F3E7),              // Beige (for text)

    surface = Color(0xFFD0D3A8),                // Light Green (for tiny accent UI components)
    surfaceVariant = Color(0xFF34C759),         // Vibrant Green (for navigation buttons)
    surfaceBright = Color(0xFF595959),          // Light Gray (for secondary buttons)
    surfaceTint = Color(0xFFb2251e)             // Vibrant Red (for Logout button)
)