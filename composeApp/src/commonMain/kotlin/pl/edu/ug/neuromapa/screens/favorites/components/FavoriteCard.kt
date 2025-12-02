package pl.edu.ug.neuromapa.screens.favorites.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.favorites.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.favorites.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.favorites.settings.widePadding
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCard(
    title: String,
    background: DrawableResource,
    onClick: () -> Unit = {},
    onDelete: () -> Unit,
    onNavigate: () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {

                // Swipe left -> delete
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true    // Delete and remove the item
                }

                // Swipe right -> navigation
                SwipeToDismissBoxValue.StartToEnd -> {
                    onNavigate()
                    false   // Perform an action, leave the item as is
                }

                else -> false

            }
        }
    )

    // Swipe Box
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            DismissBackground(dismissState)     // Lower layer (what can be seen under the card)
        },
        content = {

            // Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clip(RoundedCornerShape(cornerRadius))
                    .clickable { onClick() }
            ) {

                // Background Image
                Image(
                    painter = painterResource(background),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Text panel
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxWidth(0.66f)
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(vertical = mediumPadding, horizontal = widePadding),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        style = getAppTypography().titleMedium,
                        color = onPrimary
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {

    val direction = dismissState.dismissDirection

    // Colors depending on the direction of swipe
    val color = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50) // Green (Right - Map)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFE53935) // Red (Left - Delete)
        else -> Color.Transparent
    }

    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Place    // TODO: replace the icon
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete   // TODO: replace the icon
        else -> Icons.Default.Delete
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(cornerRadius))
            .background(color)
            .padding(horizontal = mediumPadding),
        contentAlignment = alignment
    ) {
        if (direction != SwipeToDismissBoxValue.Settled) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}