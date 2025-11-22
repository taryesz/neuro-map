package pl.edu.ug.neuromapa.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.screens.map.components.MapHeader
import pl.edu.ug.neuromapa.ui.MainPadding
import pl.edu.ug.neuromapa.ui.MainSpacing

@Composable
fun MapScreen() {
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
                title = "NeuroMapa",
                showProfile = true,
                roundBottomCorners = false,
                onProfileClick = { println("Profile clicked") },
            )

            // Header Extension (Search Bar)
            MapHeader(
                searchBarPlaceHolder = "Wyszukaj miejsce...",
            )

            // Body (Content)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()        // TODO: Remove this line when the map is being implemented
                    .weight(1f)     // TODO: Remove this line when the map is being implemented
                    .background(Color.Gray) // TODO: Remove this line when the map is being implemented
                    .padding(MainPadding),  // TODO: Remove this line when the map is being implemented
                verticalArrangement = Arrangement.spacedBy(MainSpacing)
            ) {
                // TODO: The map goes here | BARTEK
            }

        }

    }

}