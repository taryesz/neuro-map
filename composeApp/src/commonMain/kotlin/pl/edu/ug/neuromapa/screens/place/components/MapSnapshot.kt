package pl.edu.ug.neuromapa.screens.place.components

//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import neuromapa.composeapp.generated.resources.Res
//import org.jetbrains.compose.resources.ExperimentalResourceApi
//
//import org.maplibre.compose.camera.CameraPosition
//import org.maplibre.compose.camera.rememberCameraState
//import org.maplibre.compose.expressions.dsl.const
//import org.maplibre.compose.layers.CircleLayer
//import org.maplibre.compose.map.GestureOptions
//import org.maplibre.compose.map.MapOptions
//import org.maplibre.compose.map.MaplibreMap
//import org.maplibre.compose.map.OrnamentOptions
//import org.maplibre.compose.sources.GeoJsonData
//import org.maplibre.compose.sources.rememberGeoJsonSource
//import org.maplibre.compose.style.BaseStyle
//import org.maplibre.spatialk.geojson.Position
//
//@OptIn(ExperimentalResourceApi::class)
//@Composable
//fun MapSnapshot(
//    latitude: Double,
//    longitude: Double,
//    modifier: Modifier = Modifier,
//    zoomLevel: Double = 15.5
//) {
//    MaplibreMap(
//        modifier = modifier.fillMaxSize(),
//        baseStyle = BaseStyle.Uri(Res.getUri("files/map_1_0_color.json")),
//
//        cameraState = rememberCameraState(
//            firstPosition = CameraPosition(
//                target = Position(longitude, latitude),
//                zoom = zoomLevel
//            )
//        ),
//
//        options = MapOptions(
//            gestureOptions = GestureOptions.AllDisabled,
//            ornamentOptions = OrnamentOptions(
//                isLogoEnabled = false,
//                isAttributionEnabled = false,
//                isCompassEnabled = false,
//                isScaleBarEnabled = false
//            )
//        )
//    ) {
//
//        // Marker of the selected place
//        val pointGeoJson = """
//            {
//              "type": "Feature",
//              "geometry": {
//                "type": "Point",
//                "coordinates": [$longitude, $latitude]
//              },
//              "properties": {}
//            }
//        """.trimIndent()
//
//        CircleLayer(
//            id = "snapshot-point",
//            source = rememberGeoJsonSource(
//                data = GeoJsonData.JsonString(pointGeoJson)
//            ),
//            color = const(Color.Black),
//            radius = const(6.dp),
//            strokeWidth = const(2.dp),
//            strokeColor = const(Color.White)
//        )
//    }
//}