package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.*

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
    LaunchedEffect(title, message) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        val confirmStyle = if (isDestructive) UIAlertActionStyleDestructive else UIAlertActionStyleDefault
        alert.addAction(
            UIAlertAction.actionWithTitle(confirmButtonText, style = confirmStyle) {
                onConfirm()
            }
        )

        alert.addAction(
            UIAlertAction.actionWithTitle(cancelButtonText, style = UIAlertActionStyleCancel) {
                onDismiss()
            }
        )

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            alert, animated = true, completion = null
        )
    }
}