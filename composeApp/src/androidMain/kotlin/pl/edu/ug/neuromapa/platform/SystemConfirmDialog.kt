package pl.edu.ug.neuromapa.platform

import android.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun SystemConfirmDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    isDestructive: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(title, message) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(confirmButtonText) { _, _ -> onConfirm() }
            .setNegativeButton(cancelButtonText) { _, _ -> onDismiss() }
            .setOnCancelListener { onDismiss() }
            .show()
    }
}