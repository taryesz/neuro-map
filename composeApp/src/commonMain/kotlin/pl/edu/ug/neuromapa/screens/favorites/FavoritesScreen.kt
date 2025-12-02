package pl.edu.ug.neuromapa.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.project_favorites_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.favorites.components.FavoriteCard
import pl.edu.ug.neuromapa.screens.favorites.settings.mediumSpacing
import pl.edu.ug.neuromapa.screens.favorites.settings.widePadding
import pl.edu.ug.neuromapa.screens.favorites.settings.wideSpacing
import pl.edu.ug.neuromapa.screens.place.data.mockPlaceKotkaCafe
import pl.edu.ug.neuromapa.screens.place.models.Place
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun FavoritesScreen(
    userProfileImage: DrawableResource,
    onPlaceClick: (Place) -> Unit
) {

    // TODO: remove the mocks and fill with data fetched from db
    val favoritePlaces = remember {
        List(8) { index ->
            mockPlaceKotkaCafe.copy(name = "Kotka Café #${index + 1}")
        }.toMutableStateList()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Header
            Header(
                userProfileImage = userProfileImage,
                title = "Ulubione",
                showProfile = true,
                onProfileClick = { println("Profile clicked!") },
                modifier = Modifier
            )

            // Body (Content)
            Column(
                modifier = Modifier.fillMaxSize().weight(1f).padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                // Instruction text at the top
                Text(
                    text = stringResource(Res.string.project_favorites_description),
                    style = getAppTypography().bodySmall,
                )

                // Cards wrapper
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(mediumSpacing)
                ) {
                    items(
                        items = favoritePlaces,
                        key = { it.name }
                    ) { place ->

                        FavoriteCard(
                            title = place.name,
                            background = place.photo,
                            onClick = {
                                onPlaceClick(place)
                            },
                            onDelete = {
                                favoritePlaces.remove(place)
                                println("Usunięto: ${place.name}")
                            },
                            onNavigate = {
                                println("Nawiguj do: ${place.name}")
                            }

                        )
                    }
                }

            }

        }

    }

}