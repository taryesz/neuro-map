package pl.edu.ug.neuromapa.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import neuromapa.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.data.MapPoint

// Wrapper do klastrowania (bez zmian)
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

    // 1. Stan początkowy kamery
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(54.35, 18.65), 10f)
    }

    // 2. Stan uprawnień
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 3. Launcher do wywołania systemowego okienka z pytaniem
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // 4. Próba uzyskania zgody przy starcie mapy (tylko raz)
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val clusterItems = remember(points) {
        points.map { MapPointClusterItem(it) }
    }

    val mapLibreStyleJson = """
    [
  {
    "elementType": "geometry",
    "stylers": [
      { "color": "#f5f5f0" }
    ]
  },
  {
    "elementType": "labels.text.fill",
    "stylers": [
      { "color": "#6e6e6e" }
    ]
  },
  {
    "elementType": "labels.text.stroke",
    "stylers": [
      { "color": "#ffffff" },
      { "weight": 4 }
    ]
  },
  {
    "featureType": "administrative",
    "elementType": "geometry.stroke",
    "stylers": [
      { "color": "#dcdcdc" },
      { "weight": 1 }
    ]
  },
  {
    "featureType": "landscape.man_made",
    "elementType": "geometry.fill",
    "stylers": [
      { "color": "#ececec" }
    ]
  },
  {
    "featureType": "landscape.natural",
    "elementType": "geometry.fill",
    "stylers": [
      { "color": "#dceccb" }
    ]
  },
  {
    "featureType": "poi",
    "stylers": [
      { "visibility": "off" }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry.fill",
    "stylers": [
      { "visibility": "on" },
      { "color": "#dceccb" }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": [
      { "color": "#ffffff" }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": [
      { "color": "#e6e6e6" },
      { "weight": 1 }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      { "color": "#8a8a8a" }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.fill",
    "stylers": [
      { "color": "#ffffff" }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      { "color": "#e0e0e0" }
    ]
  },
  {
    "featureType": "transit",
    "stylers": [
      { "visibility": "off" }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry.fill",
    "stylers": [
      { "color": "#d8eff5" }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      { "color": "#9db4bd" }
    ]
  }
]
    """.trimIndent()

    LaunchedEffect(points) {
        if (points.isNotEmpty()) {
            try {
                if (points.size == 1) {
                    val singlePoint = points.first()
                    cameraPositionState.move(
                        update = CameraUpdateFactory.newLatLngZoom(
                            LatLng(singlePoint.latitude, singlePoint.longitude),
                            15f
                        )
                    )
                } else {
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
                // ignore
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions(mapLibreStyleJson),
            isMyLocationEnabled = hasLocationPermission // <-- Używamy bezpiecznej flagi
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = hasLocationPermission // Przycisk widoczny tylko gdy jest zgoda
        )
    ) {
        Clustering(
            items = clusterItems,
            clusterItemContent = { item ->
                val iconRes = getCategoryIcon(item.mapPoint.category)
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = item.mapPoint.name,
                    modifier = Modifier.size(40.dp)
                )
            },
            onClusterItemClick = { item ->
                onPointClick(item.mapPoint.id.toLong())
                true
            },
            clusterContent = { cluster ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0x99489CA1),
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