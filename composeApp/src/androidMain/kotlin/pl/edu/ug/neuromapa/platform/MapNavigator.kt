package pl.edu.ug.neuromapa.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

actual class MapNavigator(private val context: Context) {
    actual fun navigateTo(latitude: Double, longitude: Double, name: String) {
        val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

@Composable
actual fun rememberMapNavigator(): MapNavigator {
    val context = LocalContext.current
    return remember(context) { MapNavigator(context) }
}