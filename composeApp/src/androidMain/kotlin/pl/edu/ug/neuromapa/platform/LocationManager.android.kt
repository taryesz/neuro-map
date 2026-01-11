package pl.edu.ug.neuromapa.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
private fun requestActualLocation(context: Context, onResult: (LocationRequestResult) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                onResult(LocationRequestResult.Success(Location(location.latitude, location.longitude)))
            } else {
                onResult(LocationRequestResult.Failure)
            }
        }
        .addOnFailureListener { 
            onResult(LocationRequestResult.Failure)
        }
}

@Composable
actual fun rememberLocationManager(
    onResult: (LocationRequestResult) -> Unit
): LocationManager {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (isGranted) {
            requestActualLocation(context, onResult)
        } else {
            onResult(LocationRequestResult.PermissionDenied)
        }
    }

    return remember(context, launcher, onResult) {
        object : LocationManager {
            override fun requestLocation() {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    requestActualLocation(context, onResult)
                } else {
                    launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            }
        }
    }
}