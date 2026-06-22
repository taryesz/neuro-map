package pl.edu.ug.neuromapa.platform

import androidx.compose.runtime.Composable

@Composable
expect fun SystemConfirmDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
)