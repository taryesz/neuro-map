package pl.edu.ug.neuromapa.platform

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

class AndroidMapNavigator(private val context: android.content.Context) : MapNavigator {
    override fun navigateTo(latitude: Double, longitude: Double, name: String) {
        val endLat = String.format(Locale.US, "%f", latitude)
        val endLon = String.format(Locale.US, "%f", longitude)

        val uriString = "google.navigation:q=$endLat,$endLon"
        val gmmIntentUri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        
        try {
            context.startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val browserIntent = Intent(Intent.ACTION_VIEW, 
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$endLat,$endLon"))
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browserIntent)
        }
    }
}

@Composable
actual fun rememberMapNavigator(): MapNavigator {
    val context = LocalContext.current
    return remember(context) { AndroidMapNavigator(context) }
}