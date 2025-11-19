package pl.edu.ug.neuromapa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun NeuroMapaTheme(
    // Sprawdzamy, czy telefon jest w trybie ciemnym (na przyszłość)
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit // To jest "wnętrze", które owijamy (np. Twój HomeScreen)
) {
    // Tutaj decydujemy, której palety użyć.
    // Na razie masz tylko LightColors, więc używamy jej zawsze.
    // W przyszłości zrobisz: if (darkTheme) DarkColors else LightColors
    val colorScheme = LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = Typography, // Tu podepniesz czcionki w przyszłości
        content = content
    )
}