package pl.edu.ug.neuromapa.screens.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.project_favorites_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.favorites.components.FavoriteCard
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyMediumSpacing
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.favorites.data.CafePlace
import pl.edu.ug.neuromapa.screens.favorites.data.CafePlace1
import pl.edu.ug.neuromapa.screens.favorites.data.CafePlace2
import pl.edu.ug.neuromapa.screens.favorites.data.mockPlaceKotkaCafe
import pl.edu.ug.neuromapa.screens.place.models.Place
import pl.edu.ug.neuromapa.ui.getAppTypography
import androidx.compose.runtime.key
import pl.edu.ug.neuromapa.enums.Screen

@Composable
fun FavoritesScreen(
    userProfileImage: DrawableResource,
    onNavigateToScreen: (Screen) -> Unit,
    place: Place = mockPlaceKotkaCafe,
    place1: Place = CafePlace,
    place2: Place = CafePlace1,
    place3: Place = CafePlace2,

) {
    val favoritePlaces = remember {
        mutableStateListOf(place, place1,place2, place3)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Base Header
            Header(
                title = "Ulubione",
                userProfileImage = userProfileImage,
                showProfile = true,
                roundBottomCorners = true,
                onProfileClick = { println("Profile clicked") },
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(bodyWidePadding),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {
                val rawFavDescription = stringResource(Res.string.project_favorites_description)
                val favDescriptionParagraphs = remember(rawFavDescription) {
                    rawFavDescription.split("\n").filter { it.isNotBlank() }
                }
                    favDescriptionParagraphs.forEachIndexed { index, paragraphText ->
                        Text(
                            text = paragraphText.trim(),
                            style = getAppTypography().bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(bodyMediumSpacing)
            )
            {
                favoritePlaces.forEach { currentPlace ->
                    key(currentPlace.name) {
                        AnimatedVisibility(
                            visible = favoritePlaces.contains(currentPlace),
                            exit = shrinkHorizontally(tween(500)) + fadeOut(tween(500))
                        ) {
                            FavoriteCard(
                                place = currentPlace,
                                onRemove = { placeToRemove ->
                                    favoritePlaces.remove(placeToRemove)
                                    println("Removed: ${placeToRemove.name}")
                                },
                                onNavigate = { placeToNavigate ->
                                    println("Navigate to: ${placeToNavigate.name}")
                                },
                                onClick = { placeClicked ->
                                    println("Card clicked: ${placeClicked.name}")
                                    onNavigateToScreen(Screen.Place)
                                }
                            )
                        }
                    }
                }
            }
        }
        }
}