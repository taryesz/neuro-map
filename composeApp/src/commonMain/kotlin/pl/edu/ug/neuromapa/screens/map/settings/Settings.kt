package pl.edu.ug.neuromapa.screens.map.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.spatialk.geojson.Position

val widePadding = 30.dp
val mediumPadding = 15.dp

val wideSpacing = 30.dp
val mediumSpacing = 15.dp

val userProfileIconSize = 40.dp

// MapLibre
val iconSize = DpSize(30.dp, 30.dp)
val iconHaloWidthValue = const(10.dp)
val iconHaloColorValue = const(Color.White)
val iconHaloBlurValue = const(1.dp)

@Composable
fun cameraSettings(): CameraState {

    val camera = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(latitude = 54.444, longitude = 18.560),
            zoom = 9.0
        )
    )
    return camera
}

