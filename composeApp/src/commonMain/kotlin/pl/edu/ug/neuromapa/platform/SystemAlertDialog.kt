package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

@Composable
expect fun SystemAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
)