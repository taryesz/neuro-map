package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

// Deklarujemy, że taka klasa "będzie istniała" na każdej platformie
expect class MapNavigator {
    fun navigateTo(latitude: Double, longitude: Double, name: String)
}

@Composable
expect fun rememberMapNavigator(): MapNavigator