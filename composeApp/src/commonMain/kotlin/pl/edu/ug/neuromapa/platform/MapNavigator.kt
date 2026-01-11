package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

interface MapNavigator {
    fun navigateTo(latitude: Double, longitude: Double, name: String)
}

@Composable
expect fun rememberMapNavigator(): MapNavigator