package pl.edu.ug.neuromapa

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.data.auth.OAuthLauncher
import pl.edu.ug.neuromapa.data.auth.ProfilePhotoPicker
import pl.edu.ug.neuromapa.data.auth.OAuthResultHandler
import pl.edu.ug.neuromapa.data.auth.SessionStorage
import pl.edu.ug.neuromapa.data.auth.StoredSession
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("neuromapa_auth", Context.MODE_PRIVATE)

        SessionStorage.save = { token, refreshToken, email, userId, name, birthDate, photoUrl ->
            prefs.edit()
                .putString("token", token)
                .putString("refreshToken", refreshToken)
                .putString("email", email)
                .putString("userId", userId)
                .putString("name", name)
                .putString("birthDate", birthDate)
                .putString("photoUrl", photoUrl)
                .apply()
        }
        SessionStorage.load = {
            val token = prefs.getString("token", null)
            val email = prefs.getString("email", null)
            val userId = prefs.getString("userId", null)
            if (token != null && email != null && userId != null)
                StoredSession(
                    token = token,
                    refreshToken = prefs.getString("refreshToken", null),
                    email = email,
                    userId = userId,
                    name = prefs.getString("name", null),
                    birthDate = prefs.getString("birthDate", null),
                    photoUrl = prefs.getString("photoUrl", null)
                )
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

        val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) {
                ProfilePhotoPicker.onResult?.invoke(null, "Nie wybrano zdjęcia.")
            } else {
                try {
                    val compressed = compressImageFromUri(uri)
                    ProfilePhotoPicker.onResult?.invoke(compressed, null)
                } catch (_: Exception) {
                    ProfilePhotoPicker.onResult?.invoke(null, "Nie udało się przygotować zdjęcia.")
                }
            }
        }

        ProfilePhotoPicker.launch = {
            photoPickerLauncher.launch("image/*")
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
            val refreshToken = params["refresh_token"]
            OAuthResultHandler.handle?.invoke(accessToken, refreshToken, null)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

private fun MainActivity.compressImageFromUri(uri: Uri): ByteArray {
    val input = contentResolver.openInputStream(uri) ?: throw IllegalStateException("Cannot open image stream")
    val bitmap = input.use { BitmapFactory.decodeStream(it) } ?: throw IllegalStateException("Cannot decode image")

    var currentBitmap = bitmap
    var quality = 95
    var compressed = compressToJpeg(currentBitmap, quality)

    while (compressed.size > 50 * 1024 && quality > 40) {
        quality -= 5
        compressed = compressToJpeg(currentBitmap, quality)
    }

    while (compressed.size > 50 * 1024) {
        val nextWidth = (currentBitmap.width * 0.9f).toInt().coerceAtLeast(160)
        val nextHeight = (currentBitmap.height * 0.9f).toInt().coerceAtLeast(160)

        if (nextWidth == currentBitmap.width || nextHeight == currentBitmap.height) {
            break
        }

        currentBitmap = Bitmap.createScaledBitmap(currentBitmap, nextWidth, nextHeight, true)
        compressed = compressToJpeg(currentBitmap, quality)
    }

    return compressed
}

private fun compressToJpeg(bitmap: Bitmap, quality: Int): ByteArray {
    val output = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
    return output.toByteArray()
}
