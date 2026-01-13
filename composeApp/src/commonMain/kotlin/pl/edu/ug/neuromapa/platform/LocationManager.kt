package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

// 1. Model danych
data class Location(val latitude: Double, val longitude: Double)

// 2. Wynik żądania lokalizacji
sealed class LocationRequestResult {
    data class Success(val location: Location) : LocationRequestResult()
    data object PermissionDenied : LocationRequestResult()
    data object Failure : LocationRequestResult()
}

// 3. Interfejs menedżera
expect class LocationManager {
    fun requestLocation()
}

// 4. Funkcja Composable do tworzenia menedżera
@Composable
expect fun rememberLocationManager(onResult: (LocationRequestResult) -> Unit): LocationManager