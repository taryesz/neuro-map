package pl.edu.ug.neuromapa.screens.favorites

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
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyMediumPadding
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyMediumSpacing
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyWideSpacing
import pl.edu.ug.neuromapa.screens.place.data.CafePlace
import pl.edu.ug.neuromapa.screens.place.data.mockPlaceKotkaCafe
import pl.edu.ug.neuromapa.screens.place.models.Place
import pl.edu.ug.neuromapa.ui.getAppTypography

val favoritePlaces = mutableStateListOf<Place>()
@Composable
fun FavoritesScreen(
    userProfileImage: DrawableResource,
    place: Place = mockPlaceKotkaCafe,
    place1: Place = CafePlace,

) {
    favoritePlaces.add(place)
    favoritePlaces.add(place1)
    favoritePlaces.add(place1)
    favoritePlaces.add(place1)

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
                verticalArrangement = Arrangement.spacedBy(bodyMediumSpacing) // Простір між картками
            )
            {
                favoritePlaces.forEach { place ->
                    FavoriteCard(place = place)
                }
            }
        }
        }
}