package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class MapNavigator {
    actual fun navigateTo(latitude: Double, longitude: Double, name: String) {
        val urlString = "http://maps.apple.com/?daddr=$latitude,$longitude"
        val url = NSURL(string = urlString)

        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
        }
    }
}

@Composable
actual fun rememberMapNavigator(): MapNavigator {
    return remember { MapNavigator() }
}