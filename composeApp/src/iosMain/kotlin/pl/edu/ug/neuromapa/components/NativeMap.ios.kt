package pl.edu.ug.neuromapa.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.*
import pl.edu.ug.neuromapa.data.MapPoint
import platform.UIKit.*
import platform.darwin.NSObject
import platform.Foundation.NSString
import platform.Foundation.create
import kotlin.math.max

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.resize(width: Double, height: Double): UIImage? {
    val targetSize = CGSizeMake(width, height)
    val rect = CGRectMake(0.0, 0.0, width, height)
    UIGraphicsBeginImageContextWithOptions(targetSize, false, 0.0)
    this.drawInRect(rect)
    val newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return newImage
}

@OptIn(ExperimentalForeignApi::class)
private fun drawClusterImage(count: Int): UIImage? {
    val diameter = 40.0
    val size = CGSizeMake(diameter, diameter)
    val rect = CGRectMake(0.0, 0.0, diameter, diameter)

    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    val context = UIGraphicsGetCurrentContext()

    val red = 0.28
    val green = 0.61
    val blue = 0.63
    val alpha = 0.85

    CGContextSetRGBFillColor(context, red, green, blue, alpha)
    CGContextFillEllipseInRect(context, rect)

    CGContextSetRGBStrokeColor(context, 1.0, 1.0, 1.0, 1.0)
    CGContextSetLineWidth(context, 2.0)
    CGContextStrokeEllipseInRect(context, CGRectInset(rect, 1.0, 1.0))

    val text = NSString.create(string = count.toString())
    val paragraphStyle = NSMutableParagraphStyle()
    paragraphStyle.setAlignment(NSTextAlignmentCenter)

    val attributes = mapOf<Any?, Any?>(
        NSForegroundColorAttributeName to UIColor.whiteColor,
        NSFontAttributeName to UIFont.boldSystemFontOfSize(16.0),
        NSParagraphStyleAttributeName to paragraphStyle
    )

    val textHeight = 16.0
    val yOffset = (diameter - textHeight) / 2 - 2
    val textRect = CGRectMake(0.0, yOffset, diameter, textHeight + 5)

    text.drawInRect(textRect, withAttributes = attributes)

    val image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return image
}

private class MapDelegate(
    val onPointClick: (Long) -> Unit,
    val points: List<MapPoint>
) : NSObject(), MKMapViewDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun mapView(mapView: MKMapView, viewForAnnotation: MKAnnotationProtocol): MKAnnotationView? {
        if (viewForAnnotation is MKUserLocation) return null

        if (viewForAnnotation is MKPointAnnotation) {
            val category = (viewForAnnotation.subtitle ?: "default").trim().lowercase()
            val reuseId = "pin_$category"

            var annotationView = mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId)

            if (annotationView == null) {
                annotationView = MKAnnotationView(viewForAnnotation, reuseId)
                annotationView.canShowCallout = false
                annotationView.clusteringIdentifier = "neuro_cluster"
            }

            annotationView.annotation = viewForAnnotation

            val imageName = when (category) {
                "relaks" -> "category_dark_relax"
                "dzieci" -> "category_dark_children"
                "jedzenie" -> "category_dark_food"
                "kultura" -> "category_dark_culture"
                "usługi" -> "category_dark_services"
                "wsparcie" -> "category_dark_support"
                "praca" -> "category_dark_work"
                else -> "category_dark_relax"
            }

            val originalImage = UIImage.imageNamed(imageName)
            if (originalImage != null) {
                annotationView.image = originalImage.resize(40.0, 40.0)
            }

            annotationView.displayPriority = MKFeatureDisplayPriorityRequired

            return annotationView
        }

        if (viewForAnnotation is MKClusterAnnotation) {
            val reuseId = "cluster"

            var clusterView = mapView.dequeueReusableAnnotationViewWithIdentifier(reuseId)

            if (clusterView == null) {
                clusterView = MKAnnotationView(viewForAnnotation, reuseId)
                clusterView.canShowCallout = false
            }

            clusterView.annotation = viewForAnnotation

            val count = viewForAnnotation.memberAnnotations.size.toInt()
            clusterView.image = drawClusterImage(count)
            clusterView.displayPriority = MKFeatureDisplayPriorityRequired

            return clusterView
        }

        return null
    }

    @Suppress("CONFLICTING_OVERLOADS")
    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val annotation = didSelectAnnotationView.annotation

        if (annotation is MKPointAnnotation) {
            val point = points.find { it.name == annotation.title }
            point?.let { onPointClick(it.id.toLong()) }
        }

        mapView.deselectAnnotation(annotation, animated = false)
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeMap(
    points: List<MapPoint>,
    modifier: Modifier,
    onPointClick: (Long) -> Unit
) {
    val mkMapView = remember { MKMapView() }
    val delegate = remember(points) { MapDelegate(onPointClick, points) }

    UIKitView(
        factory = {
            mkMapView.apply {
                showsUserLocation = true
                this.delegate = delegate
                pointOfInterestFilter = MKPointOfInterestFilter.filterExcludingAllCategories()
                mapType = MKMapTypeMutedStandard
                showsCompass = false
                showsScale = false
                showsTraffic = false
                showsBuildings = false
                showsPointsOfInterest = false
            }
        },
        modifier = modifier,
        update = { view ->
            view.delegate = delegate
            view.removeAnnotations(view.annotations)

            points.forEach { point ->
                val annotation = MKPointAnnotation()
                annotation.setCoordinate(CLLocationCoordinate2DMake(point.latitude, point.longitude))
                annotation.setTitle(point.name)
                annotation.setSubtitle(point.category)
                view.addAnnotation(annotation)
            }

            if (points.isNotEmpty()) {
                val latitudes = points.map { it.latitude }
                val longitudes = points.map { it.longitude }
                val minLat = latitudes.minOrNull() ?: 0.0
                val maxLat = latitudes.maxOrNull() ?: 0.0
                val minLng = longitudes.minOrNull() ?: 0.0
                val maxLng = longitudes.maxOrNull() ?: 0.0

                val centerLat = (minLat + maxLat) / 2
                val centerLng = (minLng + maxLng) / 2

                val spanLat = max((maxLat - minLat) * 1.4, 0.02)
                val spanLng = max((maxLng - minLng) * 1.4, 0.02)

                val region = MKCoordinateRegionMake(
                    CLLocationCoordinate2DMake(centerLat, centerLng),
                    MKCoordinateSpanMake(spanLat, spanLng)
                )
                view.setRegion(region, animated = true)
            }
        }
    )
}
