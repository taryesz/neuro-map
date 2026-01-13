package pl.edu.ug.neuromapa.screens.map

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
// Pamiętaj o imporcie NativeMap!
import pl.edu.ug.neuromapa.components.NativeMap
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.data.PlaceViewModel
import pl.edu.ug.neuromapa.screens.add.components.FormButton
import pl.edu.ug.neuromapa.screens.add.components.FormSelection
import pl.edu.ug.neuromapa.screens.map.components.MapHeader
import pl.edu.ug.neuromapa.screens.map.settings.mediumSpacing
import pl.edu.ug.neuromapa.screens.map.settings.widePadding
import pl.edu.ug.neuromapa.ui.Background
import pl.edu.ug.neuromapa.data.getCategories
import pl.edu.ug.neuromapa.data.getExcellenceMarks
import pl.edu.ug.neuromapa.data.getSensoryProperties

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

@Composable
fun MapScreen(
    userProfileImage: DrawableResource,
    mapPoints: List<MapPoint>,
    onPlaceClick: (Long) -> Unit,
    bottomPadding: Dp = 0.dp,
    placeViewModel: PlaceViewModel
) {

    // States of the search_bar_filter button and the filters themselves
    var isFilterVisible by remember { mutableStateOf(false) }

    val searchQuery = placeViewModel.searchQuery.value
    val selectedCategories = placeViewModel.selectedCategories.value
    val selectedProperties = placeViewModel.selectedProperties.value
    val selectedExcellences = placeViewModel.selectedExcellences.value

    // --- LOGIKA FILTROWANIA (BEZ ZMIAN) ---
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
            val selectedPropsSlug = selectedProperties.map { raw ->
                val slug = raw.toSlug()
                exceptions[slug] ?: slug
            }

            val selectedCatsSlug = selectedCategories.map { it.toSlug() }
            val pointCat = point.category.lowercase()
            val pointProps = point.sensoryFeatures.map { it.lowercase() }

            val catMatch = selectedCategories.isEmpty() || selectedCatsSlug.contains(pointCat)
            val propMatch = selectedProperties.isEmpty() || pointProps.containsAll(selectedPropsSlug)

            val excMatch = selectedExcellences.isEmpty() || run {
                val wantsMedal = selectedExcellences.any { it.contains("medal", true) }
                val wantsHeart = selectedExcellences.any { it.contains("serduszko", true) }
                val medalOk = if (wantsMedal) point.hasMedal else true
                val heartOk = if (wantsHeart) point.hasHeart else true
                medalOk && heartOk
            }

            val searchMatch = if (searchQuery.isBlank()) {
                true
            } else {
                point.name.contains(searchQuery, ignoreCase = true)
            }

            catMatch && propMatch && excMatch && searchMatch
        }
    }

    LaunchedEffect(filteredPoints) {
        println("=== Native Map Points: ${filteredPoints.size} ===")
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Base Header
        Header(
            title = if (isFilterVisible) "Filtruj" else "NeuroMapa",
            userProfileImage = userProfileImage,
            showProfile = true,
            roundBottomCorners = isFilterVisible,
            onProfileClick = { println("Profile clicked") },
        )

        if (isFilterVisible) {
            // --- UI FILTRÓW (BEZ ZMIAN) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .verticalScroll(rememberScrollState())
                    .padding(widePadding)
                    .padding(bottom = bottomPadding),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing)
            ) {
                FormSelection(
                    title = "Kategorie",
                    items = getCategories(),
                    selectedItems = selectedCategories,
                    onSelectionChange = { placeViewModel.selectedCategories.value = it }
                )
                FormSelection(
                    title = "Cechy sensoryczne",
                    items = getSensoryProperties(),
                    selectedItems = selectedProperties,
                    onSelectionChange = { placeViewModel.selectedProperties.value = it }
                )
                FormSelection(
                    title = "Wyróżnienia",
                    items = getExcellenceMarks(),
                    selectedItems = selectedExcellences,
                    onSelectionChange = { placeViewModel.selectedExcellences.value = it }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    FormButton(
                        text = "Wyczyść",
                        modifier = Modifier.weight(1f),
                        isPrimary = false,
                        onClick = {
                            placeViewModel.selectedCategories.value = emptySet()
                            placeViewModel.selectedProperties.value = emptySet()
                            placeViewModel.selectedExcellences.value = emptySet()
                        }
                    )
                    FormButton(
                        text = "Zastosuj",
                        modifier = Modifier.weight(1f),
                        isPrimary = true,
                        onClick = { isFilterVisible = false }
                    )
                }
            }
        } else {
            // --- MAPA NATYWNA ---
            Box(modifier = Modifier.fillMaxSize()) {

                // TU JEST KLUCZOWA ZMIANA:
                NativeMap(
                    points = filteredPoints,
                    modifier = Modifier.fillMaxSize(),
                    onPointClick = onPlaceClick
                )

                // Header Extension (Search Bar) - na wierzchu mapy
                MapHeader(
                    searchBarPlaceHolder = "Wyszukaj miejsce...",
                    onFilterClick = { isFilterVisible = true },
                    searchText = searchQuery,
                    onSearchTextChange = { newText ->
                        placeViewModel.searchQuery.value = newText
                    }
                )
            }
        }
    }
}