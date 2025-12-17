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
import androidx.compose.material.icons.Icons
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
import pl.edu.ug.neuromapa.ui.icons.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import org.maplibre.compose.expressions.dsl.Case
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.em
import kotlinx.coroutines.coroutineScope
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.maplibre.compose.expressions.ast.Expression
import org.maplibre.compose.expressions.dsl.asNumber
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.RasterLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.GeoJsonOptions
import org.maplibre.compose.sources.GeoJsonSource
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import pl.edu.ug.neuromapa.screens.map.settings.cameraSettings
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.step
import org.maplibre.compose.util.ClickResult
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.expressions.dsl.and
import org.maplibre.compose.expressions.dsl.asString
import org.maplibre.compose.expressions.dsl.case
import org.maplibre.compose.expressions.dsl.eq
import org.maplibre.compose.expressions.dsl.not
import org.maplibre.compose.expressions.value.SymbolZOrder
import org.maplibre.compose.sources.rememberRasterSource
import org.maplibre.spatialk.geojson.Point



fun mapPointsToGeoJson(mapPoints: List<MapPoint>): String {
    val features = mapPoints.joinToString(separator = ",") { point ->
        """
        {
            "type": "Feature",
            "geometry": {
                "type": "Point",
                "coordinates": [${point.longitude}, ${point.latitude}]
            },
            "properties": {
                "id": ${point.id},
                "name": "${point.name}",
                "category": "${point.category}"
            }
        }
        """
    }

    return """
        {
            "type": "FeatureCollection",
            "features": [
                $features
            ]
        }
    """
}

@Composable
fun MapScreen(
    userProfileImage: DrawableResource,
    mapPoints: List<MapPoint>
) {
    // wynik filtracji tutaj poniżej jako mapPoints
    val geoJsonString = remember(mapPoints) {
        mapPointsToGeoJson(mapPoints)
    }

    val iconSize = DpSize(30.dp, 30.dp)
    val iconHaloWidthValue =  const(10.dp)
    val iconHaloColorValue = const(Color.White)
    val iconHaloBlurValue = const(1.dp)






    LaunchedEffect(mapPoints) {
        println("=== TEST MAPY: START ===")
        println("Liczba pobranych punktów: ${mapPoints.size}")

        mapPoints.forEach { point ->
            println("Punkt: $point")
        }
//        println("GeoJSON: $geoJsonString")
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
                ) {



                    val pointsSource = rememberGeoJsonSource(
                        data = GeoJsonData.JsonString(geoJsonString),
                        options = GeoJsonOptions(
                            cluster = true,
                            clusterMaxZoom = 14,
                            clusterRadius = 50

                        )
                    )
                    // WARSTWA 1: KLASTRY (Kółka)
                    CircleLayer(
                        id = "clusters-circles",
                        source = pointsSource,
                        filter = feature.has("point_count"),
                        color = step(
                            input = feature["point_count"].asNumber(),
                            fallback = const(Color.hsv(186f, 0.74f, 0.60f)),
                            10 to const(Color.hsv(186f, 0.78f, 0.50f)),
                            30 to const(Color.hsv(186f, 0.84f, 0.40f)),

                        ),
                        blur = const(0.15f),
                        opacity = const(0.6f),
                        radius = step(
                            input = feature["point_count"].asNumber(),
                            fallback = const(20.dp), // Domyślny rozmiar
                            10 to const(30.dp),
                            30 to const(40.dp),
                            50 to const(50.dp)
                        ),
                    )

                    // WARSTWA 2: LICZNIK W KLASTRZE (Tekst)
                    SymbolLayer(
                        id = "clusters-count",
                        source = pointsSource,
                        filter = feature.has("point_count"),
                        textField = feature["point_count_abbreviated"].asString(),
                        textColor = const(Color.Black),
                        textSize = const(1.em),
                        textFont = const(listOf("Noto Sans Regular")),
                        textHaloWidth = const(1.dp),
                        textHaloColor = const(Color.White),
                        textHaloBlur = const(1.dp),
                        textAllowOverlap = const(true),
                        textIgnorePlacement = const(true),
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true)
                    )

                    // WARSTWA 3: POJEDYNCZE PUNKTY (Nie-klastry) - wszysztkie punkty poza klastrami
//                    CircleLayer(
//                        id = "unclustered-points",
//                        source = pointsSource,
//                        filter = !feature.has("point_count"),
//                        color = const(Color.Cyan),
//                        radius = const(5.dp),
//                        strokeWidth = const(2.dp),
//                        strokeColor = const(Color.White),
//                        onClick = { features ->
//                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
//                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
//                            ClickResult.Consume
//                        }
//                    )

                    SymbolLayer(
                        id = "unclustered-points-relaks",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                            feature.has("category").and(              // Posiada klucz category
                            feature["category"].asString().eq(const("relaks"))) // Jest relaksem
                        ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_relax),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-children",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("dzieci"))) // TODO SPRAWDŹ POPRAWNOŚĆ NAZWY KATEGORII
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_children),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-food",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("jedzenie"))) // Jest relaksem
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_food),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-culture",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("kultura"))) // Jest relaksem
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_culture),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-services",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("uslugi"))) // TODO SPRAWDŹ POPRAWNOŚĆ NAZWY KATEGORII
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_services),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-support",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("wsparcie")))
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_support),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )

                    SymbolLayer(
                        id = "unclustered-points-work",
                        source = pointsSource,
                        filter =
                            feature.has("point_count").not().and(      // Nie jest klastrem
                                feature.has("category").and(              // Posiada klucz category
                                    feature["category"].asString().eq(const("praca"))) // Jest relaksem
                            ),
                        iconImage = image(
                            value = painterResource(Res.drawable.category_dark_work),
                            size = iconSize
                        ),
                        iconHaloWidth = iconHaloWidthValue,
                        iconHaloColor = iconHaloColorValue,
                        iconHaloBlur = iconHaloBlurValue,
                        iconAllowOverlap = const(true),
                        iconIgnorePlacement = const(true),
                        onClick = { features ->
                            // Tu możesz dodać logikę, np. otwarcie BottomSheet
                            println("Kliknięto punkt: ${features.firstOrNull()?.properties}")
                            ClickResult.Consume
                        }
                    )





                }
            }

        }

    }

}
