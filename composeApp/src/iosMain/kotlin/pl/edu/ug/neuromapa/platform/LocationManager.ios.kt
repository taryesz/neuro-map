package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.darwin.NSObject
import platform.Foundation.NSError

actual class LocationManager(
    private val locationManager: CLLocationManager,
    private val delegate: NSObject
) {
    actual fun requestLocation() {
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberLocationManager(onResult: (LocationRequestResult) -> Unit): LocationManager {

    val locationManager = remember { CLLocationManager() }

    val delegate = remember {
        LocationDelegate(onResult)
    }

    DisposableEffect(Unit) {
        locationManager.delegate = delegate
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        onDispose {
            locationManager.stopUpdatingLocation()
        }
    }

    return remember { LocationManager(locationManager, delegate) }
}

@OptIn(ExperimentalForeignApi::class)
private class LocationDelegate(
    val onResult: (LocationRequestResult) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation
        location?.let {
            val lat = it.coordinate.useContents { latitude }
            val lng = it.coordinate.useContents { longitude }

            onResult(LocationRequestResult.Success(Location(lat, lng)))
            manager.stopUpdatingLocation()
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        onResult(LocationRequestResult.Failure)
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
    }
}