package pl.edu.ug.neuromapa.screens.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import pl.edu.ug.neuromapa.screens.favorites.settings.deleteColor
import pl.edu.ug.neuromapa.screens.favorites.settings.deleteNavigateIconSize
import pl.edu.ug.neuromapa.screens.favorites.settings.navigateColor
import pl.edu.ug.neuromapa.screens.favorites.settings.scrimColor
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@Composable
fun DismissBackground(
    offset: Float,
    onConfirmDelete: () -> Unit,
    onConfirmNavigate: () -> Unit
) {
    val target = when {
        offset > 0 -> "START"
        offset < 0 -> "END"
        else -> "NONE"
    }

    if (target == "NONE") return

    val alignment = if (target == "START") Alignment.CenterStart else Alignment.CenterEnd

    val color = when (target) {
        "END" -> deleteColor
        "START" -> navigateColor
        else -> scrimColor
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = color,
                shape = RoundedCornerShape(globalComponentCornerRadius)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = alignment
    ) {
        if (target == "END") {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(deleteNavigateIconSize)
                    .clickable { onConfirmDelete() }
            )
        } else if (target == "START") {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(deleteNavigateIconSize)
                    .clickable { onConfirmNavigate() }
            )
        }
    }
}
