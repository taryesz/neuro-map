package pl.edu.ug.neuromapa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.user_icon_example
import pl.edu.ug.neuromapa.enums.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.components.NavigationBar
import pl.edu.ug.neuromapa.screens.add.AddScreen
import pl.edu.ug.neuromapa.screens.favorites.FavoritesScreen
import pl.edu.ug.neuromapa.screens.home.HomeScreen
import pl.edu.ug.neuromapa.screens.map.MapScreen
import pl.edu.ug.neuromapa.screens.place.PlaceScreen
import pl.edu.ug.neuromapa.screens.place.data.mockPlaceKotkaCafe
import pl.edu.ug.neuromapa.screens.survey.SurveyScreen
import pl.edu.ug.neuromapa.ui.NeuroMapaTheme

@Composable
@Preview
fun App() {
    NeuroMapaTheme {

        var currentScreen by remember { mutableStateOf(Screen.Home) }
        var selectedPlace by remember { mutableStateOf(mockPlaceKotkaCafe) }

        Scaffold(
            bottomBar = {
                NavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        currentScreen = newScreen
                    }
                )
            },
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->

            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentScreen) {

                    Screen.Home -> HomeScreen(
                        userFirstName = "Ryszard",
                        userProfileImage = Res.drawable.user_icon_example,
                    )
                    Screen.Add -> AddScreen(
                        userProfileImage = Res.drawable.user_icon_example,
                    )
                    Screen.Map -> MapScreen(
                        userProfileImage = Res.drawable.user_icon_example,
                    )
                    Screen.Favorites -> FavoritesScreen(
                        userProfileImage = Res.drawable.user_icon_example,
                        onPlaceClick = { place ->
                            selectedPlace = place
                            currentScreen = Screen.Place
                        }
                    )
                    Screen.Survey -> SurveyScreen(
                        userProfileImage = Res.drawable.user_icon_example,
                    )
                    Screen.Place -> PlaceScreen(
                        place = selectedPlace
                    )

                }
            }
        }
    }
}