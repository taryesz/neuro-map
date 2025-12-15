package pl.edu.ug.neuromapa.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import org.jetbrains.compose.resources.DrawableResource
import pl.edu.ug.neuromapa.components.Header
import pl.edu.ug.neuromapa.data.MapPoint
import pl.edu.ug.neuromapa.screens.map.components.MapHeader
import pl.edu.ug.neuromapa.screens.map.settings.widePadding
import pl.edu.ug.neuromapa.screens.map.settings.wideSpacing
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import neuromapa.composeapp.generated.resources.Res
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.style.BaseStyle
import pl.edu.ug.neuromapa.screens.map.settings.cameraSettings


@Composable
fun MapScreen(
    userProfileImage: DrawableResource,
    mapPoints: List<MapPoint>
) {

    LaunchedEffect(mapPoints) {
        println("=== TEST MAPY: START ===")
        println("Liczba pobranych punktów: ${mapPoints.size}")

        mapPoints.forEach { point ->
            println("Punkt: $point")
        }

        println("=== TEST MAPY: KONIEC ===")
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) {

        Column(
        ) {

            // Base Header
            Header(
                title = "NeuroMapa",
                userProfileImage = userProfileImage,
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(wideSpacing)
            ) {
                MaplibreMap(
                    baseStyle = BaseStyle.Uri(Res.getUri("files/map_1_0_color.json")),
                    cameraState = cameraSettings(),
                    options = MapOptions(
                        ornamentOptions = OrnamentOptions(
                            padding = PaddingValues(0.dp),
                            isLogoEnabled = true,
                            logoAlignment = Alignment.BottomStart,
                            isAttributionEnabled = true,
                            attributionAlignment = Alignment.BottomEnd,
                            isCompassEnabled = true,
                            compassAlignment = Alignment.TopEnd,
                            isScaleBarEnabled = true,
                            scaleBarAlignment = Alignment.TopStart,
                        ),
                        gestureOptions = GestureOptions.Standard
                    )
                )
            }

        }

    }

}
