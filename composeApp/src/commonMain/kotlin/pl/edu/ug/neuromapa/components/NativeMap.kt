package pl.edu.ug.neuromapa.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.data.MapPoint

@Composable
expect fun NativeMap(
    points: List<MapPoint>,
    modifier: Modifier = Modifier,
    isInteractive: Boolean,
    onPointClick: (Long) -> Unit
)
