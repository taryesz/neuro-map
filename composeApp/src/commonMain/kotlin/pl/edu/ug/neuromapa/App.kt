package pl.edu.ug.neuromapa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.user_pfp_example
import pl.edu.ug.neuromapa.enums.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.components.NavigationBar
import pl.edu.ug.neuromapa.screens.add.AddScreen
import pl.edu.ug.neuromapa.screens.favorites.FavoritesScreen
import pl.edu.ug.neuromapa.screens.home.HomeScreen
import pl.edu.ug.neuromapa.screens.map.MapScreen
import pl.edu.ug.neuromapa.screens.place.PlaceScreen
import pl.edu.ug.neuromapa.screens.survey.SurveyScreen
import pl.edu.ug.neuromapa.ui.NeuroMapaTheme
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceDataState
import pl.edu.ug.neuromapa.data.PlaceViewModel

@Composable
@Preview
fun App() {
    NeuroMapaTheme {

        val placeViewModel = viewModel { PlaceViewModel() }
        val dataState by placeViewModel.dataState.collectAsState()

        var currentScreen by remember { mutableStateOf(Screen.Home) }
        var selectedMapPoint by remember { mutableStateOf<MapPoint?>(null) }

        val mapPoints: List<MapPoint> = remember(dataState) {
            (dataState as? PlaceDataState.Success)?.mapPoints ?: emptyList()
        }

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

            Box(modifier = Modifier.fillMaxSize()) {

                when (dataState) {

                    is PlaceDataState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is PlaceDataState.Error -> {
                        val message = (dataState as PlaceDataState.Error).message
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Error: $message. Check the network connection and try again",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    is PlaceDataState.Success -> {

                        when (currentScreen) {

                            Screen.Map -> MapScreen(
                                userProfileImage = Res.drawable.user_pfp_example,
                                mapPoints = mapPoints,
                                bottomPadding = paddingValues.calculateBottomPadding(),
                                placeViewModel = placeViewModel,
                                onPlaceClick = { clickedId ->

                                    // Find a point by id
                                    val point = mapPoints.find { it.id.toLong() == clickedId }

                                    if (point != null) {
                                        selectedMapPoint = point
                                        currentScreen = Screen.Place
                                    }

                                }
                            )

                            else -> {

                                Box(modifier = Modifier.padding(paddingValues)) {

                                    when (currentScreen) {

                                        Screen.Home -> HomeScreen(
                                            userFirstName = "User",
                                            userProfileImage = Res.drawable.user_pfp_example,
                                        )

                                        Screen.Add -> AddScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,
                                        )

                                        Screen.Favorites -> FavoritesScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,
                                            onPlaceClick = { place ->
                                                // TODO: clicking on a saved place shows its details
                                                println("Kliknięto w ulubione: ${place.name}")
                                            }
                                        )

                                        Screen.Survey -> SurveyScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,
                                        )

                                        Screen.Place -> {
                                            if (selectedMapPoint != null) {
                                                PlaceScreen(mapPoint = selectedMapPoint!!)
                                            }
                                        }

                                        else -> {}

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
