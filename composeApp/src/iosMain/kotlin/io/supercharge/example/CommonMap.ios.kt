@file:OptIn(ExperimentalForeignApi::class)

package io.supercharge.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.Arena
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.interpretPointed
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.collections.immutable.ImmutableList
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKOverlayLevelAboveRoads
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.MapKit.addOverlay
import platform.MapKit.overlays
import platform.MapKit.removeOverlays
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.systemBlueColor
import platform.darwin.NSObject

@Composable
actual fun CommonMap(
    startLocation: Marker,
    stopLocation: Marker,
    route: ImmutableList<Coordinate>,
    modifier: Modifier
) {
    // density conversion is not needed on iOS, the OS will use it as Points
    val boundsPadding = 16.dp.value.toDouble()

    val delegate = rememberMKMapViewDelegate()

    val startMarkerAnnotation = rememberMarkerAnnotation(startLocation)
    val stopMarkerAnnotation = rememberMarkerAnnotation(stopLocation)

    val routeOverlay = rememberPolyline(route)

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                setZoomEnabled(true)
                setScrollEnabled(true)
                setRotateEnabled(true)
                setPitchEnabled(true)

                setShowsCompass(false)

                // set delegate to handle annotations and overlays
                setDelegate(delegate)

                // customize map view further
            }
        },
        update = { mapView ->
            // clear previous annotations and overlays
            mapView.removeAnnotations(mapView.annotations)
            mapView.removeOverlays(mapView.overlays)

            // add markers
            mapView.addAnnotation(startMarkerAnnotation)
            mapView.addAnnotation(stopMarkerAnnotation)

            // add route overlay
            mapView.addOverlay(routeOverlay, MKOverlayLevelAboveRoads)

            // set visible map rect with padding to show our route
            val edgePadding = UIEdgeInsetsMake(
                top = boundsPadding,
                left = boundsPadding,
                bottom = boundsPadding,
                right = boundsPadding
            )

            mapView.setVisibleMapRect(routeOverlay.boundingMapRect, edgePadding = edgePadding, animated = true)
        }
    )
}

@Composable
fun rememberMKMapViewDelegate(): MKMapViewDelegateProtocol {
    return remember {
        object : NSObject(), MKMapViewDelegateProtocol {

            @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            @ObjCSignatureOverride
            override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
                val polyline = rendererForOverlay as? MKPolyline ?: return MKOverlayRenderer(rendererForOverlay)

                val renderer = MKPolylineRenderer(overlay = polyline).apply {
                    setStrokeColor(UIColor.systemBlueColor)
                    setLineWidth(3.0)
                }

                return renderer
            }
        }
    }
}

@Composable
fun rememberMarkerAnnotation(marker: Marker): MKAnnotationProtocol {
    return remember(marker) {
        val clLocation = CLLocationCoordinate2DMake(
            marker.coordinate.latitude,
            marker.coordinate.longitude
        )

        MKPointAnnotation().apply {
            setTitle(marker.title)
            setCoordinate(clLocation)
        }
    }
}

@Composable
fun rememberPolyline(route: ImmutableList<Coordinate>): MKPolyline {
    return remember(route) {
        memScoped {
            // allocate a C style array to hold the coordinates
            val coordinates = allocArray<CLLocationCoordinate2D>(route.size)

            route.forEachIndexed { index, coordinate ->
                // get the CValue of CLLocationCoordinate2D
                val clLocation = CLLocationCoordinate2DMake(coordinate.latitude, coordinate.longitude)

                // calculate the offset of the current coordinate in the array
                val offset = index * sizeOf<CLLocationCoordinate2D>()

                // calculate the memory address of the current coordinate in the array
                val pointer = coordinates.rawValue + offset

                // copy the coordinate to the calculated memory address
                clLocation.place(interpretPointed<CLLocationCoordinate2D>(pointer).ptr)
            }

            MKPolyline.polylineWithCoordinates(
                coords = coordinates,
                count = route.size.toULong()
            )
        }
    }
}

@Composable
fun rememberPolylineWithArena(route: ImmutableList<Coordinate>): MKPolyline {
    // create an Arena to allocate memory in the native heap
    val nativeMemory = remember { Arena() }

    val polyline = remember(route) {
        // allocate the memory
        val coordinates = nativeMemory.allocArray<CLLocationCoordinate2D>(route.size)

        route.forEachIndexed { index, coordinate ->
            val clLocation = CLLocationCoordinate2DMake(
                coordinate.latitude,
                coordinate.longitude
            )
            val offset = index * sizeOf<CLLocationCoordinate2D>()
            val pointer = coordinates.rawValue + offset
            clLocation.place(interpretPointed<CLLocationCoordinate2D>(pointer).ptr)
        }

        // create the polyline
        MKPolyline.polylineWithCoordinates(
            coords = coordinates,
            count = route.size.toULong()
        )
    }

    // clean up native memory when the composable leaves composition
    DisposableEffect(polyline) {
        onDispose {
            nativeMemory.clear()
        }
    }

    return polyline
}

@Composable
fun rememberPolylineWithNativeHeap(route: ImmutableList<Coordinate>): MKPolyline {
    val polyline = remember(route) {
        // allocate memory in the native heap
        val coordinates = nativeHeap.allocArray<CLLocationCoordinate2D>(route.size)

        route.forEachIndexed { index, coordinate ->
            val clLocation = CLLocationCoordinate2DMake(
                coordinate.latitude,
                coordinate.longitude
            )
            val offset = index * sizeOf<CLLocationCoordinate2D>()
            val pointer = coordinates.rawValue + offset
            clLocation.place(interpretPointed<CLLocationCoordinate2D>(pointer).ptr)
        }

        // create data class to hold both the polyline and allocated memory
        PolylineWithNativeMemory(
            polyline = MKPolyline.polylineWithCoordinates(
                coords = coordinates,
                count = route.size.toULong()
            ),
            nativeMemory = coordinates
        )
    }

    // clean up native memory when the composable leaves composition
    DisposableEffect(polyline) {
        onDispose {
            nativeHeap.free(polyline.nativeMemory.rawValue)
        }
    }

    return polyline.polyline
}

private data class PolylineWithNativeMemory(
    val polyline: MKPolyline,
    val nativeMemory: CPointer<CLLocationCoordinate2D>
)
