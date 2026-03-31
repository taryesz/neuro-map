package pl.edu.ug.neuromapa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.user_pfp_example
import pl.edu.ug.neuromapa.enums.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.edu.ug.neuromapa.components.navigation_bar.NavigationBar
import pl.edu.ug.neuromapa.screens.add.AddScreen
import pl.edu.ug.neuromapa.screens.favorites.FavoritesScreen
import pl.edu.ug.neuromapa.screens.home.HomeScreen
import pl.edu.ug.neuromapa.screens.map.MapScreen
import pl.edu.ug.neuromapa.screens.place.PlaceScreen
import pl.edu.ug.neuromapa.ui.NeuroMapTheme
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceDataState
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.screens.login.LoginScreen

@Composable
@Preview
fun App() {

    NeuroMapTheme {

        val placeViewModel = viewModel { PlaceViewModel() }
        val dataState by placeViewModel.dataState.collectAsState()

        var currentScreen by remember { mutableStateOf(Screen.Home) }
        var selectedMapPoint by remember { mutableStateOf<MapPoint?>(null) }

        val mapPoints: List<MapPoint> = remember(dataState) {
            (dataState as? PlaceDataState.Success)?.mapPoints ?: emptyList()
        }

        // This will allow us to know where the user left the screen and
        // when coming back to that screen, go to the exact place where they left
        val homeScrollState = rememberScrollState()
        val addScrollState = rememberScrollState()
        val mapFilterScrollState = rememberScrollState()
        val favoritesListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            bottomBar = {
                NavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { newScreen ->
                        if (currentScreen == newScreen) {
                            coroutineScope.launch {
                                when (newScreen) {
                                    Screen.Home -> homeScrollState.animateScrollTo(0)
                                    Screen.Add -> addScrollState.animateScrollTo(0)
                                    Screen.Map -> mapFilterScrollState.animateScrollTo(0)
                                    Screen.Favorites -> favoritesListState.animateScrollToItem(0)
                                    else -> {}
                                }
                            }
                        } else {
                            currentScreen = newScreen
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets(0.dp)
        )
        { paddingValues ->

            Box(modifier = Modifier.fillMaxSize()) {

                when (dataState) {

                    // Show a loading screen when the place data is being fetched from the NeuroMapa website
                    is PlaceDataState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    // Show an error in case one happends
                    is PlaceDataState.Error -> {
                        val message = (dataState as PlaceDataState.Error).message
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Error: $message. Check the network connection and try again",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    // Show the application in case the data is fetched completely and successfully
                    is PlaceDataState.Success -> {

                        when (currentScreen) {

                            Screen.Map -> MapScreen(
                                userProfileImage = Res.drawable.user_pfp_example,   // TODO: change accordingly
                                mapPoints = mapPoints,
                                bottomPadding = paddingValues.calculateBottomPadding(),
                                placeViewModel = placeViewModel,
                                onPlaceClick = { clickedId ->

                                    // Find a point by id
                                    val point = mapPoints.find { it.id.toLong() == clickedId }

                                    // Go to the place details (Place Screen)
                                    if (point != null) {
                                        selectedMapPoint = point
                                        currentScreen = Screen.Place
                                    }

                                },
                                onProfileClick = { currentScreen = Screen.Profile },
                                filterScrollState = mapFilterScrollState
                            )

                            else -> {

                                Box(modifier = Modifier.padding(paddingValues)) {

                                    when (currentScreen) {

                                        Screen.Home -> HomeScreen(
                                            userFirstName = "User", // TODO: change accordingly
                                            userProfileImage = Res.drawable.user_pfp_example,// TODO: change accordingly
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            scrollState = homeScrollState
                                        )

                                        Screen.Add -> AddScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,// TODO: change accordingly
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            scrollState = addScrollState
                                        )

                                        Screen.Favorites -> FavoritesScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,// TODO: change accordingly
                                            onPlaceClick = { place ->
                                                // TODO: clicking on a saved place shows its details
                                                println("Kliknięto w ulubione: ${place.name}")
                                            },
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            listState = favoritesListState
                                        )

                                        Screen.Place -> {
                                            if (selectedMapPoint != null) {
                                                PlaceScreen(mapPoint = selectedMapPoint!!)
                                            }
                                        }

                                        Screen.Profile -> LoginScreen()

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
