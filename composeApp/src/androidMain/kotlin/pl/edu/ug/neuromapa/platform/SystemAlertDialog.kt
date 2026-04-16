package pl.edu.ug.neuromapa.platform

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun SystemAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(title, message) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> onDismiss() }
            .setOnDismissListener { onDismiss() }
            .show()
    }
}