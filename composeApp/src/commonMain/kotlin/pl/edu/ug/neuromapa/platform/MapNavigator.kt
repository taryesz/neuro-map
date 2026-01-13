package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
// Upewnij się, że ten import wskazuje na miejsce, gdzie zdefiniowałeś expect fun openNavigation
import pl.edu.ug.neuromapa.components.openNavigation

class MapNavigator {
    fun navigateTo(latitude: Double, longitude: Double, name: String) {
        // Ta funkcja jest 'expect', więc Android i iOS mają swoje implementacje
        // Nie musimy robić osobnych klas MapNavigator
        openNavigation(latitude, longitude)
    }
}

@Composable
fun rememberMapNavigator(): MapNavigator {
    return remember { MapNavigator() }
}