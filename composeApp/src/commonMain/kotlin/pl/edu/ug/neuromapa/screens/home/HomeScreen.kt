package pl.edu.ug.neuromapa.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.about_project_description
import neuromapa.composeapp.generated.resources.about_project_title
import neuromapa.composeapp.generated.resources.key_operations_description
import neuromapa.composeapp.generated.resources.key_operations_title
import neuromapa.composeapp.generated.resources.logo_faru_gummed_pg_ug_blue
import neuromapa.composeapp.generated.resources.logo_gdansk
import neuromapa.composeapp.generated.resources.logo_investgda_fullcolor
import neuromapa.composeapp.generated.resources.logo_neuromap_dark
import neuromapa.composeapp.generated.resources.project_goals_description
import neuromapa.composeapp.generated.resources.project_goals_title
import neuromapa.composeapp.generated.resources.team_members_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceDataState
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.helpers.calculateDistance
import pl.edu.ug.neuromapa.platform.Location
import pl.edu.ug.neuromapa.platform.LocationRequestResult
import pl.edu.ug.neuromapa.platform.rememberLocationManager
import pl.edu.ug.neuromapa.platform.rememberMapNavigator
import pl.edu.ug.neuromapa.screens.home.components.HomeHeader
import pl.edu.ug.neuromapa.screens.home.models.affiliationsData
import pl.edu.ug.neuromapa.screens.home.models.teamMembersData
import pl.edu.ug.neuromapa.screens.home.settings.bulletPointHorizontalSpacing
import pl.edu.ug.neuromapa.screens.home.settings.logoHeight
import pl.edu.ug.neuromapa.screens.home.settings.logoSpacing
import pl.edu.ug.neuromapa.screens.home.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.home.settings.narrowPadding
import pl.edu.ug.neuromapa.screens.home.settings.superTextFontSize
import pl.edu.ug.neuromapa.screens.home.settings.superTextLineHeight
import pl.edu.ug.neuromapa.screens.home.settings.superTextNumberWidth
import pl.edu.ug.neuromapa.screens.home.settings.superTextOpacity
import pl.edu.ug.neuromapa.screens.home.settings.widePadding
import pl.edu.ug.neuromapa.screens.home.settings.wideSpacing
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun HomeScreen(
    userFirstName: String,
    userProfileImage: DrawableResource,
) {
    val placeViewModel: PlaceViewModel = viewModel()
    val placeDataState by placeViewModel.dataState.collectAsState()
    val mapNavigator = rememberMapNavigator()

    fun findAndNavigate(currentLocation: Location) {
        val placesState = placeDataState
        if (placesState is PlaceDataState.Success) {
            val allPlaces = placesState.mapPoints
            val quietPlaces = allPlaces.filter { it.sensoryFeatures.contains("ciche") }

            if (quietPlaces.isEmpty()) {
                println("Nie znaleziono cichych miejsc.")
                return
            }

            val nearestPlace = quietPlaces.minByOrNull {
                calculateDistance(currentLocation.latitude, currentLocation.longitude, it.latitude, it.longitude)
            }

            if (nearestPlace != null) {
                mapNavigator.navigateTo(
                    latitude = nearestPlace.latitude,
                    longitude = nearestPlace.longitude,
                    name = nearestPlace.name
                )
            } else {
                println("Nie udało się znaleźć najbliższego miejsca.")
            }
        } else {
            println("Miejsca nie są jeszcze załadowane. Spróbuj ponownie.")
        }
    }

    val locationManager = rememberLocationManager { result ->
        when (result) {
            is LocationRequestResult.Success -> {
                findAndNavigate(result.location)
            }
            is LocationRequestResult.PermissionDenied -> {
                println("Brak pozwolenia na dostęp do lokalizacji.")
            }
            is LocationRequestResult.Failure -> {
                println("Nie udało się uzyskać lokalizacji.")
            }
        }
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
            HomeHeader(
                title = "Witaj, $userFirstName!",
                motto = "Znajdź miejsca przyjazne Twoim potrzebom.",
                buttonText = "Potrzebuję spokoju",
                userProfileImage = userProfileImage,
                showProfile = true,
                onProfileClick = { println("Profile clicked") },
                onButtonClick = { locationManager.requestLocation() }
            )

            // Решта вашого коду без змін
            Column(
                modifier = Modifier.fillMaxSize().padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.about_project_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    Text(
                        modifier = Modifier.padding(top = mediumPadding),
                        text = stringResource(Res.string.about_project_description),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.project_goals_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    val rawGoals = stringResource(Res.string.project_goals_description)
                    val goalsList = remember(rawGoals) {
                        rawGoals.split("\n").filter { it.isNotBlank() }
                    }
                    Column {
                        goalsList.forEach { goalText ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = mediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = goalText.trim(),
                                    style = getAppTypography().bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.key_operations_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    val rawGoals = stringResource(Res.string.key_operations_description)
                    val goalsList = remember(rawGoals) {
                        rawGoals.split("\n").filter { it.isNotBlank() }
                    }
                    Column {
                        goalsList.forEach { goalText ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = mediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    style = getAppTypography().bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = goalText.trim(),
                                    style = getAppTypography().bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.team_members_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )
                    teamMembersData.forEach { member ->
                        val nameText = stringResource(member.nameRes)
                        val styledText = buildAnnotatedString {
                            append("$nameText ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = superTextFontSize
                                )
                            ) {
                                val refsString = member.affiliationIds.joinToString(",")
                                append("($refsString)")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = mediumPadding),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = "•",
                                style = getAppTypography().bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(end = bulletPointHorizontalSpacing)
                            )
                            Text(
                                text = styledText,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(wideSpacing))
                    affiliationsData.forEach { (id, textRes) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = narrowPadding),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = superTextFontSize
                                    )) {
                                        append("$id ")
                                    }
                                },
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = superTextOpacity),
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.width(superTextNumberWidth)
                            )
                            Text(
                                text = stringResource(textRes),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = superTextOpacity),
                                fontSize = superTextFontSize,
                                lineHeight = superTextLineHeight,
                                style = getAppTypography().bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(logoSpacing)
                ) {
                    val logos = listOf(
                        Res.drawable.logo_neuromap_dark to "Logotyp NeuroMapy.",
                        Res.drawable.logo_investgda_fullcolor to "Logotyp InvestGDA.",
                        Res.drawable.logo_gdansk to "Logotyp miasta Gdańska.",
                        Res.drawable.logo_faru_gummed_pg_ug_blue to "Logotyp FarU, GumMed, PG oraz UG.",
                    )
                    logos.forEach { (logo, contentDescription) ->
                        Image(
                            painter = painterResource(logo),
                            contentDescription = contentDescription,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(logoHeight)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}