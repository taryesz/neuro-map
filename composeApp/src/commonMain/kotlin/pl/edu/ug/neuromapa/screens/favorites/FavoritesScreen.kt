package pl.edu.ug.neuromapa.screens.favorites

import EmptyFavorites
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.favorites_screen_header_title
import neuromapa.composeapp.generated.resources.favorites_screen_instruction
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.auth.AuthViewModel
import pl.edu.ug.neuromapa.platform.MapNavigator
import pl.edu.ug.neuromapa.platform.rememberMapNavigator
import pl.edu.ug.neuromapa.screens.favorites.components.FavoritePlaceCard
import pl.edu.ug.neuromapa.screens.favorites.settings.mediumSpacing
import pl.edu.ug.neuromapa.screens.favorites.settings.widePadding
import pl.edu.ug.neuromapa.screens.favorites.settings.wideSpacing
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun FavoritesScreen(
    userProfileImage: DrawableResource,
    favoritesViewModel: FavoritesViewModel,
    authViewModel: AuthViewModel,
    mapPoints: List<MapPoint>,
    onPlaceClick: (MapPoint) -> Unit,
    onGoToMap: () -> Unit,
    onProfileClick: () -> Unit,
    profilePhotoUrl: String? = null,
    listState: LazyListState = rememberLazyListState()
) {
    val mapNavigator = rememberMapNavigator()
    val authState by authViewModel.authState.collectAsState()
    val favoriteIds by favoritesViewModel.favoritePlaceIds.collectAsState()
    val favoritePlaces = remember(favoriteIds, mapPoints) {
        mapPoints.filter { it.id in favoriteIds }
    }

    // Wrapper of the whole screen
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        // One more wrapper...
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Header (turquoise panel at the very top)
            Header(
                userProfileImage = userProfileImage,
                title = stringResource(Res.string.favorites_screen_header_title),
                profilePhotoUrl = profilePhotoUrl,
                showProfileTopRightCorner = true,
                onProfileClick = onProfileClick,
                modifier = Modifier
            )

            // Body (main content)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {


                if (favoritePlaces.isEmpty()) {
                    EmptyFavorites(
                        onGoToMap = onGoToMap
                    )
                } else {
                    // Instruction of how to use the gestures at the top (right below the turquoise header)
                    Text(
                        text = stringResource(Res.string.favorites_screen_instruction),
                        style = getAppTypography().bodySmall,
                    )

                    // Wrapper of the saved places (or as I call them: "cards")
                    // The LazyColumn allows to have a scrollable PART of the screen, not the whole screen
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(mediumSpacing)
                    ) {

                        // Define what this column will show
                        items(
                            items = favoritePlaces,
                            key = { it.id }
                        ) { place ->    // For each place create its own "card"

                            FavoritePlaceCard(
                                title = place.name,
                                background = place.photoUrl,
                                onClick = {
                                    onPlaceClick(place)     // Send the user to another screen with the place details
                                },
                                onDelete = {
                                    favoritesViewModel.toggleFavorite(
                                        place.id,
                                        authState
                                    )
                                },
                                onNavigate = {
                                    mapNavigator.navigateTo(place.latitude, place.longitude, place.name)
                                }
                            )
                        }
                    }
                }
            }

        }

    }

}
