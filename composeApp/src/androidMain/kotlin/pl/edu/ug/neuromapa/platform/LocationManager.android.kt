package pl.edu.ug.neuromapa.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

actual class LocationManager(
    private val onPermissionRequest: () -> Unit,
    private val onFetchLocation: () -> Unit
) {
    actual fun requestLocation() {
        onPermissionRequest()
    }

    fun fetch() {
        onFetchLocation()
    }
}

@Composable
actual fun rememberLocationManager(onResult: (LocationRequestResult) -> Unit): LocationManager {
    val context = LocalContext.current

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val fetchLocation = {
        try {
            @SuppressLint("MissingPermission")
            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    onResult(LocationRequestResult.Success(Location(location.latitude, location.longitude)))
                } else {
                    onResult(LocationRequestResult.Failure)
                }
            }
            task.addOnFailureListener {
                onResult(LocationRequestResult.Failure)
            }
        } catch (e: Exception) {
            onResult(LocationRequestResult.Failure)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fine || coarse) {
            fetchLocation()
        } else {
            onResult(LocationRequestResult.PermissionDenied)
        }
    }

    return remember {
        LocationManager(
            onPermissionRequest = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {
                    fetchLocation()
                } else {
                    launcher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            },
            onFetchLocation = { fetchLocation() }
        )
    }
}