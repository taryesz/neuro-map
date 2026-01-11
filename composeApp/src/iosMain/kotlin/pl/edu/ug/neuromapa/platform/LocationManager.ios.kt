package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class IOSLocationManager(
    private val onResult: (LocationRequestResult) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol, LocationManager {

    private val locationManager = CLLocationManager().apply {
        delegate = this@IOSLocationManager
    }

    override fun requestLocation() {
        when (locationManager.authorizationStatus()) {
            kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
            kCLAuthorizationStatusAuthorizedWhenInUse -> locationManager.requestLocation()
            else -> onResult(LocationRequestResult.PermissionDenied)
        }
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        if (manager.authorizationStatus() == kCLAuthorizationStatusAuthorizedWhenInUse) {
            manager.requestLocation()
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val locations = didUpdateLocations as List<CLLocation>
        locations.firstOrNull()?.let {
            val coordinate = it.coordinate
            onResult(LocationRequestResult.Success(Location(coordinate.useContents { latitude }, coordinate.useContents { longitude })))
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: platform.Foundation.NSError) {
        onResult(LocationRequestResult.Failure)
    }
}

@Composable
actual fun rememberLocationManager(
    onResult: (LocationRequestResult) -> Unit
): LocationManager {
    return remember { IOSLocationManager(onResult) }
}