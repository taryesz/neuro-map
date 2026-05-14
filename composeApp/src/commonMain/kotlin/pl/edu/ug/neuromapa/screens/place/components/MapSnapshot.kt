package pl.edu.ug.neuromapa.screens.place.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.components.NativeMap
import pl.edu.ug.neuromapa.data.MapPoint

@Composable
fun MapSnapshot(
    latitude: Double,
    longitude: Double,
    category: String,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true
) {

    // Unlike a normal map, this map only shows ONE place
    val singlePoint = MapPoint(
        id = -1,
        name = "",
        category = category,
        latitude = latitude,
        longitude = longitude,
        sensoryFeatures = emptyList(),
        hasMedal = false,
        hasHeart = false,
        description = "",
        address = "",
        photoUrl = "",
        website = "",
        facebook = "",
        instagram = ""
    )

    NativeMap(
        points = listOf(singlePoint),
        modifier = modifier,
        isInteractive = isInteractive,
        onPointClick = {}   // Do nothing because we are already in the place screen
    )

}