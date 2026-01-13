package pl.edu.ug.neuromapa.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.*
import com.google.maps.android.compose.clustering.Clustering
import pl.edu.ug.neuromapa.data.MapPoint
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.*

// Wrapper potrzebny do klastrowania Google Maps
private class MapPointClusterItem(
    val mapPoint: MapPoint
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(mapPoint.latitude, mapPoint.longitude)
    override fun getTitle(): String = mapPoint.name
    override fun getSnippet(): String = mapPoint.category
    override fun getZIndex(): Float? = null
}

private fun getCategoryIcon(category: String): DrawableResource {
    return when (category.lowercase()) {
        "relaks" -> Res.drawable.category_dark_relax
        "dzieci" -> Res.drawable.category_dark_children
        "jedzenie" -> Res.drawable.category_dark_food
        "kultura" -> Res.drawable.category_dark_culture
        "usługi" -> Res.drawable.category_dark_services
        "wsparcie" -> Res.drawable.category_dark_support
        "praca" -> Res.drawable.category_dark_work
        else -> Res.drawable.category_dark_relax
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
actual fun NativeMap(
    points: List<MapPoint>,
    modifier: Modifier,
    onPointClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(54.35, 18.65), 10f)
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    // Konwersja listy MapPoint na listę ClusterItem
    val clusterItems = remember(points) {
        points.map { MapPointClusterItem(it) }
    }

    val mapLibreStyleJson = """
    [
      { "elementType": "geometry", "stylers": [ { "color": "#f2f2f0" } ] },
      { "elementType": "labels.text.fill", "stylers": [ { "color": "#525252" } ] },
      { "elementType": "labels.text.stroke", "stylers": [ { "color": "#ffffff" }, { "weight": 3 } ] },
      { "featureType": "administrative", "elementType": "geometry.stroke", "stylers": [ { "color": "#c9c9c9" }, { "weight": 1.2 } ] },
      { "featureType": "poi", "stylers": [ { "visibility": "off" } ] },
      { "featureType": "transit", "stylers": [ { "visibility": "off" } ] },
      { "featureType": "road", "elementType": "geometry.fill", "stylers": [ { "color": "#ffffff" } ] },
      { "featureType": "road", "elementType": "geometry.stroke", "stylers": [ { "color": "#d6d6d6" } ] },
      { "featureType": "road.highway", "elementType": "geometry.fill", "stylers": [ { "color": "#ffffff" } ] },
      { "featureType": "road.highway", "elementType": "geometry.stroke", "stylers": [ { "color": "#c5c5c5" } ] },
      { "featureType": "water", "elementType": "geometry.fill", "stylers": [ { "color": "#b8d9e3" } ] },
      { "featureType": "water", "elementType": "labels.text.fill", "stylers": [ { "color": "#7a7a7a" } ] }
    ]
    """.trimIndent()

    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            try {
                if (points.size == 1) {
                    // ZMIANA: move zamiast animate usuwa animację przybliżania
                    val singlePoint = points.first()
                    cameraPositionState.move(
                        update = CameraUpdateFactory.newLatLngZoom(
                            LatLng(singlePoint.latitude, singlePoint.longitude),
                            15f
                        )
                    )
                } else {
                    // ZMIANA: Tutaj również move, aby główna mapa ładowała się natychmiast
                    val builder = LatLngBounds.Builder()
                    points.forEach { point ->
                        builder.include(LatLng(point.latitude, point.longitude))
                    }
                    val bounds = builder.build()
                    cameraPositionState.move(
                        update = CameraUpdateFactory.newLatLngBounds(bounds, 250)
                    )
                }
            } catch (e: Exception) {
                // Mapa może nie być gotowa
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions(mapLibreStyleJson),
            isMyLocationEnabled = hasLocationPermission
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,   // ← PLUS / MINUS
        )
    ) {
        // --- UŻYCIE CLUSTERING ---
        Clustering(
            items = clusterItems,

            // 1. Wygląd pojedynczego pinu (Marker)
            clusterItemContent = { item ->
                val iconRes = getCategoryIcon(item.mapPoint.category)
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = item.mapPoint.name,
                    modifier = Modifier.size(40.dp)
                )
            },

            // 2. Obsługa kliknięcia w pojedynczy pin
            onClusterItemClick = { item ->
                onPointClick(item.mapPoint.id.toLong())
                true
            },

            // 3. Wygląd KLASTRA (Kółko z liczbą)
            clusterContent = { cluster ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0x99489CA1), // Półprzezroczysty morski/teal (zgodny ze screenem)
                            shape = CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cluster.size.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        )
    }
}

actual fun openNavigation(lat: Double, lng: Double) { }

fun launchGoogleMaps(context: Context, lat: Double, lng: Double) {
    val uri = Uri.parse("google.navigation:q=$lat,$lng")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}