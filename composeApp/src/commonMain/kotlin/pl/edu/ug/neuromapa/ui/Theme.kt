package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun NeuroMapTheme(
    content: @Composable () -> Unit
) {

    val colorScheme = LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )

}