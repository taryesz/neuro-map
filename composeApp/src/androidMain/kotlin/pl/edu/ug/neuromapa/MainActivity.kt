package pl.edu.ug.neuromapa

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.data.SessionStorage
import pl.edu.ug.neuromapa.data.StoredSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("neuromapa_auth", Context.MODE_PRIVATE)

        SessionStorage.save = { token, email, userId, displayName ->
            prefs.edit()
                .putString("token", token)
                .putString("email", email)
                .putString("userId", userId)
                .putString("displayName", displayName)
                .apply()
        }
        SessionStorage.load = {
            val token = prefs.getString("token", null)
            val email = prefs.getString("email", null)
            val userId = prefs.getString("userId", null)
            val displayName = prefs.getString("displayName", "") ?: ""
            if (token != null && email != null && userId != null)
                StoredSession(token, email, userId, displayName)
            else null
        }
        SessionStorage.clear = {
            prefs.edit().clear().apply()
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
