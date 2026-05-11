package pl.edu.ug.neuromapa.screens.favorites.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.favorites.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.favorites.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.favorites.settings.placeCardDeletionStateColor
import pl.edu.ug.neuromapa.screens.favorites.settings.placeCardNavigationStateColor
import pl.edu.ug.neuromapa.screens.favorites.settings.widePadding
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onPrimary
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePlaceCard(
    title: String,
    background: String,
    onClick: () -> Unit = {},
    onDelete: () -> Unit,
    onNavigate: () -> Unit
) {

    // This allows to track if the card is being swiped to left (which deleted the card)
    // or to the right (which launcher navigation to the place)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {

                // Swipe left -> delete
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true    // Deletes and removes the item both from a list and visually
                }

                // Swipe right -> navigation
                SwipeToDismissBoxValue.StartToEnd -> {
                    onNavigate()
                    false   // Launches the navigation, but snaps the card back to its place
                }

                else -> false

            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(globalComponentCornerRadius))
            .bounceClick(
                onClick = onClick,
                hapticType = HapticFeedbackType.LongPress
            )
    )
    {

        // This is a blueprint of a card which contains a place information in a shortened form in FavoritesScreen.kt
        // Here SwipeToDismissBox is used to inherit all the animations of the swipes
        SwipeToDismissBox(
            state = dismissState,                   // Assign the card's state (detect the swipe and save it here)
            backgroundContent = {
                DismissBackground(dismissState)     // Lower layer (what can be seen under the card when the card is moved)
            },
            content = {

                // Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .clip(RoundedCornerShape(cornerRadius))
                ) {

                    // Background Image
//                    AsyncImage(
//                        model = background,
//                        contentDescription = title,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.matchParentSize(),
//                        // placeholder = painterResource(Res.drawable.placeholder),
//                        // error = painterResource(Res.drawable.error_image)
//                    )

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {

    val direction = dismissState.dismissDirection

    // Set the color of the card background depending on the direction of swipe
    val color = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Color(placeCardNavigationStateColor)   // Green (to the right - navigation)
        SwipeToDismissBoxValue.EndToStart -> Color(placeCardDeletionStateColor)     // Red (to the left - delete)
        else -> Color.Transparent
    }

    // Set the alignment of the card background depending on the direction of swipe
    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart      // The background keeps left
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd        // The background keeps right
        else -> Alignment.Center
    }

    // Set the icon seen in the card's background depending on the direction of swipe
    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Place        // Pin (to the right - navigation)
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete       // Bin (to the left - navigation)
        else -> Icons.Default.Delete
    }

    // The background that is shown when the card is being moved
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(cornerRadius))
            .background(color)          // Assign the color depending on the direction of the swipe
            .padding(horizontal = mediumPadding),
        contentAlignment = alignment    // Assign the alignment of the background of the card
    ) {

        // This shows the icons (pin or bin) whenever the card is moved
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