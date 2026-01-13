package pl.edu.ug.neuromapa.components // <-- Ważne: ten pakiet musi być taki sam jak w android/ios

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.data.MapPoint

@Composable
expect fun NativeMap(
    points: List<MapPoint>,
    modifier: Modifier = Modifier,
    onPointClick: (Long) -> Unit
)

expect fun openNavigation(lat: Double, lng: Double)