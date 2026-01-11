package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

// Defines the data structure for a location point
data class Location(val latitude: Double, val longitude: Double)

// Defines the possible outcomes of a location request
sealed class LocationRequestResult {
    data class Success(val location: Location) : LocationRequestResult()
    object PermissionDenied : LocationRequestResult()
    object Failure : LocationRequestResult()
}

// Defines the interface for our location manager
interface LocationManager {
    fun requestLocation()
}

// The 'expect' declaration that the Android and iOS modules will implement
@Composable
expect fun rememberLocationManager(
    onResult: (LocationRequestResult) -> Unit
): LocationManager