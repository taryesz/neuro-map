package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.*

@Composable
actual fun SystemAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    LaunchedEffect(title, message) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )
        alert.addAction(
            UIAlertAction.actionWithTitle("OK", style = UIAlertActionStyleDefault) {
                onDismiss()
            }
        )

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            alert, animated = true, completion = null
        )
    }
}