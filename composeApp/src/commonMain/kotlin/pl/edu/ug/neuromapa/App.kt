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
import pl.edu.ug.neuromapa.data.auth.AuthViewModel
import pl.edu.ug.neuromapa.data.auth.AuthState
import pl.edu.ug.neuromapa.data.auth.ProfilePhotoPicker
import pl.edu.ug.neuromapa.screens.account.auth.SignInScreen
import pl.edu.ug.neuromapa.screens.account.auth.SignUpScreen
import pl.edu.ug.neuromapa.screens.account.dashboard.DashboardScreen
import pl.edu.ug.neuromapa.screens.favorites.FavoritesViewModel

@Composable
@Preview
fun App() {

    var isDarkTheme by remember { mutableStateOf(false) }
    var editableName by remember { mutableStateOf("") }
    var editableBirthDate by remember { mutableStateOf("") }
    var saveStatusMessage by remember { mutableStateOf<String?>(null) }

    var confirmedUserName by remember { mutableStateOf<String?>(null) }

    NeuroMapTheme(darkTheme = isDarkTheme) {

        val placeViewModel = viewModel { PlaceViewModel() }
        val authViewModel = viewModel { AuthViewModel() }
        val favoritesViewModel = viewModel { FavoritesViewModel() }

        LaunchedEffect(Unit) {
            authViewModel.restoreSession()
        }

        val dataState by placeViewModel.dataState.collectAsState()
        val authState by authViewModel.authState.collectAsState()

        var currentScreen by remember { mutableStateOf(Screen.Home) }
        var selectedMapPoint by remember { mutableStateOf<MapPoint?>(null) }

        val mapPoints: List<MapPoint> = remember(dataState) {
            (dataState as? PlaceDataState.Success)?.mapPoints ?: emptyList()
        }

        LaunchedEffect(authState) {
            if (authState is AuthState.SignedIn) {
                favoritesViewModel.loadFavorites(authState)

                if (currentScreen == Screen.SignIn || currentScreen == Screen.SignUp) {
                    currentScreen = Screen.Profile
                }
            } else if (authState is AuthState.SignedOut) {
                favoritesViewModel.clearFavorites()
            }
        }

        val signedInState = authState as? AuthState.SignedIn

        LaunchedEffect(signedInState) {
            if (signedInState != null) {
                if (confirmedUserName == null && !signedInState.name.isNullOrBlank()) {
                    confirmedUserName = signedInState.name
                    editableName = signedInState.name
                }
                if (editableBirthDate.isBlank() && !signedInState.birthDate.isNullOrBlank()) {
                    editableBirthDate = signedInState.birthDate
                }
            } else {
                confirmedUserName = null
                editableName = ""
                editableBirthDate = ""
            }
        }

        val displayName = when {
            !confirmedUserName.isNullOrBlank() -> confirmedUserName.orEmpty()
            signedInState != null -> signedInState.email.substringBefore("@")
            else -> "Użytkownik"
        }

        LaunchedEffect(authViewModel) {
            ProfilePhotoPicker.onResult = { imageBytes, errorMessage ->
                if (!errorMessage.isNullOrBlank()) {
                    saveStatusMessage = errorMessage
                } else if (imageBytes == null) {
                    saveStatusMessage = "Nie wybrano zdjęcia."
                } else {
                    authViewModel.uploadProfilePhoto(
                        imageBytes = imageBytes,
                        onError = { message -> saveStatusMessage = message },
                        onSuccess = { saveStatusMessage = "Zdjęcie profilowe zostało zapisane." }
                    )
                }
            }
        }

        val homeScrollState = rememberScrollState()
        val addScrollState = rememberScrollState()
        val mapFilterScrollState = rememberScrollState()
        val favoritesListState = rememberLazyListState()
        val profileScrollState = rememberScrollState()
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

                    is PlaceDataState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is PlaceDataState.Error -> {
                        val message = (dataState as PlaceDataState.Error).message
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Błąd: $message. Sprawdź połączenie z internetem i spróbuj ponownie.",
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
                                profilePhotoUrl = signedInState?.photoUrl,
                                onPlaceClick = { clickedId ->
                                    val point = mapPoints.find { it.id.toLong() == clickedId }
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
                                            userFirstName =
                                                if (displayName.isNotBlank())
                                                    ", $displayName!"
                                                else
                                                    "!",
                                            userProfileImage = Res.drawable.user_pfp_example,
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            profilePhotoUrl = signedInState?.photoUrl,
                                            scrollState = homeScrollState
                                        )

                                        Screen.Add -> AddScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            profilePhotoUrl = signedInState?.photoUrl,
                                            scrollState = addScrollState
                                        )

                                        Screen.Favorites -> FavoritesScreen(
                                            userProfileImage = Res.drawable.user_pfp_example,
                                            favoritesViewModel = favoritesViewModel,
                                            mapPoints = mapPoints,
                                            onPlaceClick = { place ->
                                                selectedMapPoint = place
                                                currentScreen = Screen.Place
                                            },
                                            onProfileClick = { currentScreen = Screen.Profile },
                                            profilePhotoUrl = signedInState?.photoUrl,
                                            onGoToMap = { currentScreen = Screen.Map },
                                            listState = favoritesListState,
                                            authViewModel = authViewModel
                                        )

                                        Screen.Place -> {
                                            if (selectedMapPoint != null) {
                                                PlaceScreen(
                                                    mapPoint = selectedMapPoint!!,
                                                    authViewModel = authViewModel,
                                                    favoritesViewModel = favoritesViewModel
                                                )
                                            }
                                        }

                                        Screen.Profile -> {
                                            when (authState) {
                                                is AuthState.Checking -> {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator()
                                                    }
                                                }
                                                is AuthState.SignedIn -> {
                                                    val signedIn = authState as AuthState.SignedIn
                                                    DashboardScreen(
                                                        userProfileImage = Res.drawable.user_pfp_example,
                                                        onSignOut = { authViewModel.signOut() },
                                                        currentName = editableName,
                                                        onNameChange = { editableName = it },
                                                        onSaveName = {
                                                            val normalizedName = editableName.trim()
                                                            authViewModel.updateName(
                                                                newName = editableName,
                                                                onError = { message ->
                                                                    saveStatusMessage = message
                                                                },
                                                                onSuccess = {
                                                                    confirmedUserName = normalizedName
                                                                    saveStatusMessage = "Imię zostało pomyślnie zapisane."
                                                                }
                                                            )
                                                        },
                                                        currentBirthDate = editableBirthDate,
                                                        onBirthDateChange = { editableBirthDate = it },
                                                        onSaveBirthDate = {
                                                            authViewModel.updateBirthDate(
                                                                newBirthDate = editableBirthDate,
                                                                onError = { message -> saveStatusMessage = message },
                                                                onSuccess = { saveStatusMessage = "Data urodzenia została zapisana." }
                                                            )
                                                        },
                                                        onPickProfilePhoto = {
                                                            ProfilePhotoPicker.launch?.invoke()
                                                                ?: run { saveStatusMessage = "Wybór zdjęcia nie jest dostępny na tej platformie." }
                                                        },
                                                        profilePhotoUrl = signedIn.photoUrl,
                                                        saveStatusMessage = saveStatusMessage,
                                                        onClearStatusMessage = { saveStatusMessage = null },
                                                        isDarkTheme = isDarkTheme,
                                                        onThemeChange = { isDarkTheme = it },
                                                        scrollState = profileScrollState
                                                    )
                                                }
                                                else -> {
                                                    SignInScreen(
                                                        authViewModel = authViewModel,
                                                        onNavigateToSignUp = { currentScreen = Screen.SignUp }
                                                    )
                                                }
                                            }
                                        }

                                        Screen.SignIn -> SignInScreen(
                                            authViewModel = authViewModel,
                                            onNavigateToSignUp = { currentScreen = Screen.SignUp }
                                        )

                                        Screen.SignUp -> SignUpScreen(
                                            authViewModel = authViewModel,
                                            onNavigateToSignIn = { currentScreen = Screen.SignIn }
                                        )

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