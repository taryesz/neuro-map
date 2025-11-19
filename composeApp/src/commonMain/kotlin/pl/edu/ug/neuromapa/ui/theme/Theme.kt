package pl.edu.ug.neuromapa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun NeuroMapaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )

}