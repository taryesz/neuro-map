package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IOSMapNavigator : MapNavigator {
    override fun navigateTo(latitude: Double, longitude: Double, name: String) {
        val urlString = "http://maps.apple.com/?daddr=$latitude,$longitude&q=$name"
        val url = NSURL(string = urlString)
        
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}

@Composable
actual fun rememberMapNavigator(): MapNavigator {
    return remember { IOSMapNavigator() }
}