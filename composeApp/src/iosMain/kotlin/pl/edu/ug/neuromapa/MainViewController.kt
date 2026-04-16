package pl.edu.ug.neuromapa

import androidx.compose.ui.window.ComposeUIViewController
import pl.edu.ug.neuromapa.data.auth.OAuthLauncher
import pl.edu.ug.neuromapa.data.auth.SessionStorage
import pl.edu.ug.neuromapa.data.auth.StoredSession
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication

fun MainViewController() = ComposeUIViewController {

    val defaults = NSUserDefaults.standardUserDefaults

    SessionStorage.save = { token, email, userId ->
        defaults.setObject(token, forKey = "token")
        defaults.setObject(email, forKey = "email")
        defaults.setObject(userId, forKey = "userId")
    }

    SessionStorage.load = {
        val token = defaults.stringForKey("token")
        val email = defaults.stringForKey("email")
        val userId = defaults.stringForKey("userId")

        if (token != null && email != null && userId != null) {
            StoredSession(token, email, userId)
        } else {
            null
        }
    }

    SessionStorage.clear = {
        defaults.removeObjectForKey("token")
        defaults.removeObjectForKey("email")
        defaults.removeObjectForKey("userId")
    }

    OAuthLauncher.launch = { provider ->
        val urlString = "https://mvcxlvcjcvcrjftbcvxp.supabase.co/auth/v1/authorize?provider=$provider&redirect_to=neuromapa://auth/callback"
        val url = NSURL(string = urlString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any>(),
                completionHandler = null
            )
        }
    }

    App()
}