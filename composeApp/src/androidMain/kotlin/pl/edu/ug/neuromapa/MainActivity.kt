package pl.edu.ug.neuromapa

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.data.auth.OAuthLauncher
import pl.edu.ug.neuromapa.data.auth.OAuthResultHandler
import pl.edu.ug.neuromapa.data.auth.SessionStorage
import pl.edu.ug.neuromapa.data.auth.StoredSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("neuromapa_auth", Context.MODE_PRIVATE)

        SessionStorage.save = { token, email, userId ->
            prefs.edit()
                .putString("token", token)
                .putString("email", email)
                .putString("userId", userId)
                .apply()
        }
        SessionStorage.load = {
            val token = prefs.getString("token", null)
            val email = prefs.getString("email", null)
            val userId = prefs.getString("userId", null)
            if (token != null && email != null && userId != null)
                StoredSession(token, email, userId)
            else null
        }
        SessionStorage.clear = {
            prefs.edit().clear().apply()
        }

        OAuthLauncher.launch = { provider ->
            val url = "https://mvcxlvcjcvcrjftbcvxp.supabase.co/auth/v1/authorize" +
                    "?provider=$provider&redirect_to=neuromapa://auth/callback"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data ?: return
        if (uri.scheme == "neuromapa" && uri.host == "auth") {
            val fragment = uri.fragment ?: return
            val params = fragment.split("&").associate {
                val parts = it.split("=", limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
            }
            val accessToken = params["access_token"] ?: return
            OAuthResultHandler.handle?.invoke(accessToken, null)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
