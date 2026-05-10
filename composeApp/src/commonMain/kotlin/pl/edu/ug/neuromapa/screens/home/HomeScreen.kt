package pl.edu.ug.neuromapa.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import neuromapa.composeapp.generated.resources.home_screen_about_project_section_content
import neuromapa.composeapp.generated.resources.home_screen_about_project_section_title
import neuromapa.composeapp.generated.resources.home_screen_bullet_point_icon
import neuromapa.composeapp.generated.resources.home_screen_header_motto
import neuromapa.composeapp.generated.resources.home_screen_header_need_quiet_button_name
import neuromapa.composeapp.generated.resources.home_screen_header_title
import neuromapa.composeapp.generated.resources.home_screen_key_operations_section_content
import neuromapa.composeapp.generated.resources.home_screen_key_operations_section_title
import neuromapa.composeapp.generated.resources.logo_faru_gummed_pg_ug_blue
import neuromapa.composeapp.generated.resources.logo_gdansk
import neuromapa.composeapp.generated.resources.logo_investgda_fullcolor
import neuromapa.composeapp.generated.resources.logo_neuromap_dark
import neuromapa.composeapp.generated.resources.home_screen_project_goals_section_content
import neuromapa.composeapp.generated.resources.home_screen_project_goals_section_title
import neuromapa.composeapp.generated.resources.home_screen_team_members_section_title
import neuromapa.composeapp.generated.resources.logo_faru_gumed_pg_ug_alttext
import neuromapa.composeapp.generated.resources.logo_gdansk_alttext
import neuromapa.composeapp.generated.resources.logo_investgda_alttext
import neuromapa.composeapp.generated.resources.logo_neuromap_alttext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.data.PlaceDataState
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.helpers.calculateDistance
import pl.edu.ug.neuromapa.platform.Location
import pl.edu.ug.neuromapa.platform.LocationRequestResult
import pl.edu.ug.neuromapa.platform.rememberLocationManager
import pl.edu.ug.neuromapa.platform.rememberMapNavigator
import pl.edu.ug.neuromapa.screens.home.components.HomeHeader
import pl.edu.ug.neuromapa.screens.home.data.affiliationsData
import pl.edu.ug.neuromapa.screens.home.data.teamMembersData
import pl.edu.ug.neuromapa.screens.home.settings.bulletPointHorizontalSpacing
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
import pl.edu.ug.neuromapa.ui.settings.globalComponentLogoImageHeight

@Composable
fun HomeScreen(
    userFirstName: String,
    userProfileImage: DrawableResource,
    profilePhotoUrl: String? = null,
    onProfileClick: () -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {

    // Create a ViewModel which immediately starts fetching the places data
    val placeViewModel = viewModel { PlaceViewModel() }

    // Whenever the data finished being fetched, collect its current state
    // (available, not available or permission error)
    val placeDataState by placeViewModel.dataState.collectAsState()

    // This is a Navigator which allows creating routes between locations
    val mapNavigator = rememberMapNavigator()

    // This function looks for all the quiet places on the map and starts navigation to the closest one
    // This function is called by the big light green button in the Header of HomeScreen
    fun findClosestQuietPlaceAndNavigate(currentLocation: Location) {

        val placesState = placeDataState

        // If places were loaded successfully -> we can access the places
        if (placesState is PlaceDataState.Success) {

            val allPlaces = placesState.mapPoints
            val quietPlaces = allPlaces.filter { it.sensoryFeatures.contains("ciche") }

            if (quietPlaces.isEmpty()) {
                println("Nie znaleziono cichych miejsc.")   // TODO: add UI response to this case
                return
            }

            // Find the nearest place by calculating the all distances using map coordinates
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
                println("Nie udało się znaleźć najbliższego miejsca.")  // TODO: add UI response to this case
            }

        } else {
            println("Miejsca nie są załadowane. Spróbuj ponownie.") // TODO: add UI response to this case
        }
    }

    // This checks if the places data is available and starts corresponding procedure whenever the big green button
    // is clicked
    val locationManager = rememberLocationManager { result ->
        when (result) {

            // If the data is available ->
            // start looking for the closest quiet place from the user location
            is LocationRequestResult.Success -> {
                findClosestQuietPlaceAndNavigate(result.location)
            }

            is LocationRequestResult.PermissionDenied -> {
                println("Brak pozwolenia na dostęp do lokalizacji.")    // TODO: add UI response to this case
            }
            is LocationRequestResult.Failure -> {
                println("Nie udało się uzyskać lokalizacji.")   // TODO: add UI response to this case
            }

        }
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
                .verticalScroll(scrollState)  // Make the screen scrollable
        ) {

            // Header (turquoise panel at the very top) - CUSTOM VERSION
            HomeHeader(
                title = stringResource(                                 // "Witaj, $userFirstName!"
                    Res.string.home_screen_header_title,
                    userFirstName
                ),
                motto = stringResource(Res.string.home_screen_header_motto),
                buttonText = stringResource(Res.string.home_screen_header_need_quiet_button_name),
                userProfileImage = userProfileImage,
                profilePhotoUrl = profilePhotoUrl,
                showProfile = true,
                onProfileClick = onProfileClick,
                onButtonClick = { locationManager.requestLocation() }
            )

            // Body (main content)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(widePadding),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {

                // "About the project" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Section title
                    Text(
                        text = stringResource(Res.string.home_screen_about_project_section_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // Section main content
                    Text(
                        modifier = Modifier.padding(top = mediumPadding),
                        text = stringResource(Res.string.home_screen_about_project_section_content),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().bodySmall,
                    )

                }

                // "Project goals" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Section title
                    Text(
                        text = stringResource(Res.string.home_screen_project_goals_section_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    val rawGoals = stringResource(Res.string.home_screen_project_goals_section_content)

                    // Extract separate goals from the text (each "\n" symbolizes the end of a goal)
                    val goalsList = remember(rawGoals) {
                        rawGoals.split("\n").filter { it.isNotBlank() }
                    }

                    // This is a list of goals
                    Column {
                        goalsList.forEach { goalText ->

                            // This is a specific goal (one bullet point) which consists of a separate text
                            // section containing the bullet point icon AND another text section with the goal itself
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = mediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = stringResource(Res.string.home_screen_bullet_point_icon),
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

                // "Key operations" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Section title
                    Text(
                        text = stringResource(Res.string.home_screen_key_operations_section_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    val rawOperations = stringResource(Res.string.home_screen_key_operations_section_content)

                    // Extract separate operations from the text (each "\n" symbolizes the end of an operation)
                    val goalsList = remember(rawOperations) {
                        rawOperations.split("\n").filter { it.isNotBlank() }
                    }

                    // This is a list of operations
                    Column {
                        goalsList.forEach { goalText ->

                            // This is a specific operation (one bullet point) which consists of a separate text
                            // section containing the bullet point icon AND another text section with the operation
                            // itself
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = mediumPadding),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = stringResource(Res.string.home_screen_bullet_point_icon),
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

                // "Team members" section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Section title
                    Text(
                        text = stringResource(Res.string.home_screen_team_members_section_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = getAppTypography().titleMedium
                    )

                    // This will show tiny numbers near each bullet point referring to an affiliation where
                    // this specific team member works at
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

                        // This is a specific team member (one bullet point) which consists of a separate text
                        // section containing the bullet point icon AND another text section with the team member
                        // themselves
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = mediumPadding),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = stringResource(Res.string.home_screen_bullet_point_icon),
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

                    // This will show numbers beside each affiliation which will allow referring to it
                    // from a specific team member bullet point
                    affiliationsData.forEach { (id, textRes) ->

                        // This is a specific affiliation (one bullet point) which consists of a separate text
                        // section containing the bullet point icon AND another text section with the affiliation
                        // itself
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

                // Partnering organizations logos
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(logoSpacing)
                ) {

                    val logos = listOf(
                        Res.drawable.logo_neuromap_dark to stringResource(Res.string.logo_neuromap_alttext),
                        Res.drawable.logo_investgda_fullcolor to stringResource(Res.string.logo_investgda_alttext),
                        Res.drawable.logo_gdansk to stringResource(Res.string.logo_gdansk_alttext),
                        Res.drawable.logo_faru_gummed_pg_ug_blue to stringResource(Res.string.logo_faru_gumed_pg_ug_alttext),
                    )

                    // Show each logo at the bottom of the screen
                    logos.forEach { (logo, contentDescription) ->
                        Image(
                            painter = painterResource(logo),
                            contentDescription = contentDescription,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(globalComponentLogoImageHeight)
                                .fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}
