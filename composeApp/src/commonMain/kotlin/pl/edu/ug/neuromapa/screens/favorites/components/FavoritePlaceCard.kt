package pl.edu.ug.neuromapa.screens.favorites.components

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
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.screens.favorites.settings.cardHeight
import pl.edu.ug.neuromapa.screens.favorites.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.favorites.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.favorites.settings.placeCardDeletionStateColor
import pl.edu.ug.neuromapa.screens.favorites.settings.placeCardNavigationStateColor
import pl.edu.ug.neuromapa.screens.place.components.PlaceHeader
import pl.edu.ug.neuromapa.screens.place.helpers.getCategoryIconHelper
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritePlaceCard(
    mapPoint: MapPoint,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit = {},
    onDelete: () -> Unit,
    onNavigate: () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onNavigate()
                    false
                }
                else -> false
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(globalComponentCornerRadius))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DismissBackground(dismissState)
            },
            content = {
                PlaceHeader(
                    name = mapPoint.name,
                    categoryIcon = getCategoryIconHelper(mapPoint.category),
                    categoryIconDescription = mapPoint.category,
                    isFavorite = isFavorite,
                    onFavoriteButtonClick = onFavoriteClick,
                    latitude = mapPoint.latitude,
                    longitude = mapPoint.longitude,
                    height = cardHeight,
                    onCardClick = onClick
                )
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