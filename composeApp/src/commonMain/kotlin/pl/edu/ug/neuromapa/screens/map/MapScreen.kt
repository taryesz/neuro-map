package pl.edu.ug.neuromapa.screens.map

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.components.NativeMap
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.components.form.FormSelection
import pl.edu.ug.neuromapa.screens.map.components.MapHeader
import pl.edu.ug.neuromapa.screens.map.settings.mediumSpacing
import pl.edu.ug.neuromapa.screens.map.settings.widePadding
import pl.edu.ug.neuromapa.ui.Background
import pl.edu.ug.neuromapa.data.getCategories
import pl.edu.ug.neuromapa.data.getExcellenceMarks
import pl.edu.ug.neuromapa.data.getSensoryProperties
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalFocusManager
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_form_erase_button
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_form_field_place_categories
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_form_field_place_excellences
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_form_field_place_sensory_properties
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_form_submit_button
import neuromapa.composeapp.generated.resources.map_screen_filter_overlay_header_title
import neuromapa.composeapp.generated.resources.map_screen_header_searchbar_placeholder
import neuromapa.composeapp.generated.resources.map_screen_header_title
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.ui.animations.bounceClick

@Composable
fun MapScreen(
    userProfileImage: DrawableResource,
    mapPoints: List<MapPoint>,
    onPlaceClick: (Long) -> Unit,
    bottomPadding: Dp = 0.dp,
    placeViewModel: PlaceViewModel,
    onProfileClick: () -> Unit,
    filterScrollState: ScrollState = rememberScrollState()
) {

    val focusManager = LocalFocusManager.current

    // States of the search_bar_filter button and the filters themselves
    var isFilterVisible by remember { mutableStateOf(false) }

    // Current values of selected filters stored in ViewModel
    val searchQuery = placeViewModel.searchQuery.value                  // ... current text input in the search bar
    val selectedCategories = placeViewModel.selectedCategories.value    // ... currently selected categories
    val selectedProperties = placeViewModel.selectedProperties.value    // ... currently selected properties
    val selectedExcellences = placeViewModel.selectedExcellences.value  // ... currently selected excellences

    // This replaces "normal" text to "technical" one
    // e.g. "Brak intensywnych zapachów" -> "brak_intensywnych_zapachow"
    // It is necessary to compare the data fetched from API with what the user inputs
    fun String.toSlug(): String {
        val polishChars = mapOf(
            'ą' to 'a', 'ć' to 'c', 'ę' to 'e', 'ł' to 'l', 'ń' to 'n',
            'ó' to 'o', 'ś' to 's', 'ź' to 'z', 'ż' to 'z'
        )
        return this.lowercase()
            .map { polishChars[it] ?: it }
            .joinToString("")
            .replace(" ", "_")
            .filter { it.isLetterOrDigit() || it == '_' }
    }

    // This is a list of filtered places (it auto-updates whenever anything in those 5 arguments changes)
    val filteredPoints = remember(
        mapPoints,
        selectedCategories,
        selectedProperties,
        selectedExcellences,
        searchQuery
    ) {

        val exceptions = mapOf(
            "cisza" to "ciche",
            "brak_intensywnych_zapachow" to "brak_zapachow",
            "jasna_informacja" to "dostepnosc_informacyjna"
        )

        mapPoints.filter { point ->

            // Create a "technical" version of the selected properties (to be able to compare)
            val selectedPropsSlug = selectedProperties.map { raw ->
                val slug = raw.toSlug()
                exceptions[slug] ?: slug
            }

            val selectedCatsSlug = selectedCategories.map { it.toSlug() }
            val pointCat = point.category.lowercase()
            val pointProps = point.sensoryFeatures.map { it.lowercase() }

            // Check if the current place falls under the filters or input text in search bar -> true if yes
            val catMatch = selectedCategories.isEmpty() || selectedCatsSlug.contains(pointCat)
            val propMatch = selectedProperties.isEmpty() || pointProps.containsAll(selectedPropsSlug)
            val excMatch = selectedExcellences.isEmpty() || run {
                val wantsMedal = selectedExcellences.any { it.contains("medal", true) }
                val wantsHeart = selectedExcellences.any { it.contains("serduszko", true) }
                val medalOk = if (wantsMedal) point.hasMedal else true
                val heartOk = if (wantsHeart) point.hasHeart else true
                medalOk && heartOk  // Check if both conditions are met
            }
            val searchMatch = if (searchQuery.isBlank()) {
                true
            } else {
                point.name.contains(searchQuery, ignoreCase = true)
            }

            catMatch && propMatch && excMatch && searchMatch    // Check if all 4 conditions are met. If yes ->
                                                                // use this place in the list of the results
        }

    }

    // Wrapper...
    Column(modifier = Modifier.fillMaxSize()) {

        // Base Header
        Header(

            // The title changes depending on if the filter panel is turned on or not
            title =
                if (isFilterVisible) stringResource(Res.string.map_screen_filter_overlay_header_title)
                else stringResource(Res.string.map_screen_header_title),
            userProfileImage = userProfileImage,
            showProfile = true,
            roundBottomCorners = isFilterVisible,
            onProfileClick = onProfileClick,
        )

        // Show the map filter overlay (if turned on)
        if (isFilterVisible) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                    }
                    .verticalScroll(filterScrollState)
                    .padding(widePadding)
                    .padding(bottom = bottomPadding),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing)
            ) {

                // Let the user CHOOSE category or categories of the places to be shown
                FormSelection(
                    title = stringResource(Res.string.map_screen_filter_overlay_form_field_place_categories),
                    items = getCategories(),
                    selectedItems = selectedCategories,
                    onSelectionChange = { placeViewModel.selectedCategories.value = it }
                )

                // Let the user CHOOSE sensory property or properties of the places to be shown
                FormSelection(
                    title = stringResource(Res.string.map_screen_filter_overlay_form_field_place_sensory_properties),
                    items = getSensoryProperties(),
                    selectedItems = selectedProperties,
                    onSelectionChange = { placeViewModel.selectedProperties.value = it }
                )

                // Let the user CHOOSE excellence or excellences of the places to be shown
                FormSelection(
                    title = stringResource(Res.string.map_screen_filter_overlay_form_field_place_excellences),
                    items = getExcellenceMarks(),
                    selectedItems = selectedExcellences,
                    onSelectionChange = { placeViewModel.selectedExcellences.value = it }
                )

                // Form button wrapper at the very bottom of the screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    // Wrapper for ERASE BUTTON (needed for the animation to work)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                focusManager.clearFocus()
                                placeViewModel.selectedCategories.value = emptySet()
                                placeViewModel.selectedProperties.value = emptySet()
                                placeViewModel.selectedExcellences.value = emptySet()
                            }
                    ) {
                        FormButton(
                            text = stringResource(Res.string.map_screen_filter_overlay_form_erase_button),
                            isPrimary = false,
                        )
                    }

                    // Wrapper for APPLY BUTTON (needed for the animation to work)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick {
                                focusManager.clearFocus()
                                isFilterVisible = false     // Hides the filter overlay
                            }
                    ) {
                        FormButton(
                            text = stringResource(Res.string.map_screen_filter_overlay_form_submit_button),
                            isPrimary = true,
                        )
                    }

                }
            }
        }
        // Show the map if the filters are off
        else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                    }
            ) {

                // THE NEURO MAP
                NativeMap(
                    points = filteredPoints,
                    modifier = Modifier.fillMaxSize(),
                    onPointClick = onPlaceClick
                )

                // Custom Header that has a search bar
                MapHeader(
                    searchBarPlaceHolder = stringResource(Res.string.map_screen_header_searchbar_placeholder),
                    onFilterClick = {
                        focusManager.clearFocus()
                        isFilterVisible = true  // Shows the filter overlay if a button clicked
                    },
                    searchText = searchQuery,
                    onSearchTextChange = { newText ->
                        placeViewModel.searchQuery.value = newText
                    }
                )

            }
        }
    }
}