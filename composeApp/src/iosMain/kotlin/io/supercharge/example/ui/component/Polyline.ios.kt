package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.supercharge.example.ui.LocalMapView
import io.supercharge.example.util.toUIColor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.interpretPointed
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.collections.immutable.ImmutableList
import platform.CoreGraphics.CGLineCap
import platform.CoreLocation.CLLocationCoordinate2D
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.MapKit.addOverlay
import platform.MapKit.removeOverlay

@Composable
@CommonMapComposable
actual fun Polyline(
    points: ImmutableList<LatLng>,
    color: Color,
    width: Dp
) {
    val mapView = LocalMapView.current
    val overlay = rememberPolylineOverlay(points, color, width)

    DisposableEffect(mapView, points, color, width) {
        mapView.addOverlay(overlay)

        onDispose {
            mapView.removeOverlay(overlay)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun rememberPolylineOverlay(
    points: ImmutableList<LatLng>,
    color: Color,
    width: Dp
): MKOverlayProtocol {
    val polyline = remember(points) {
        val clLocations = points.map { it.data }

        memScoped {
            // allocate a C style array to hold the coordinates
            val coordinates = allocArray<CLLocationCoordinate2D>(clLocations.size)

            clLocations.forEachIndexed { index, clLocation ->
                // calculate the offset of the current coordinate in the array
                val offset = index * sizeOf<CLLocationCoordinate2D>()

                // calculate the memory address of the current coordinate in the array
                val pointer = coordinates.rawValue + offset

                // copy the coordinate to the calculated memory address
                clLocation.place(interpretPointed<CLLocationCoordinate2D>(pointer).ptr)
            }

            MKPolyline.polylineWithCoordinates(coordinates, clLocations.size.toULong())
        }
    }

    return remember(points, color, width) {
        CommonMapOverlay(
            delegate = polyline,
            rendererCreator = {
                MKPolylineRenderer(overlay = polyline).apply {
                    setStrokeColor(color.toUIColor())
                    setLineWidth(width.value.toDouble())
                    setLineCap(CGLineCap.kCGLineCapRound)
                }
            }
        )
    }
}
