package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.supercharge.example.DEFAULT_LATITUDE
import io.supercharge.example.DEFAULT_LONGITUDE
import io.supercharge.example.DEFAULT_ZOOM
import io.supercharge.example.FULL_ROTATION_DEGREES
import io.supercharge.example.TILE_SIZE
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.collections.immutable.ImmutableList
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMake
import platform.MapKit.MKCoordinateSpanMake
import platform.MapKit.MKMapPointForCoordinate
import platform.MapKit.MKMapRectMake
import platform.MapKit.MKMapRectUnion
import platform.MapKit.MKMapView
import platform.UIKit.UIEdgeInsetsMake
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.round

@OptIn(ExperimentalForeignApi::class)
internal class AppleMapState : MapState {

    private var map: MKMapView? by mutableStateOf(null)

    override var zoomLevel: Float by mutableStateOf(DEFAULT_ZOOM)

    override fun jumpToBudapest() {
        val map = map ?: return

        map.moveToCoordinate(
            CLLocationCoordinate2DMake(latitude = DEFAULT_LATITUDE, longitude = DEFAULT_LONGITUDE),
            DEFAULT_ZOOM
        )
    }

    override fun moveToCoordinates(coordinates: ImmutableList<LatLng>, padding: Int) {
        val map = map ?: return

        val edgePadding = padding.toDouble()

        val edgeInsets = UIEdgeInsetsMake(
            top = edgePadding,
            left = edgePadding,
            bottom = edgePadding,
            right = edgePadding
        )

        val boundingMapRect = coordinates
            .map { MKMapPointForCoordinate(CLLocationCoordinate2DMake(it.latitude, it.longitude)) }
            .map { point -> point.useContents { MKMapRectMake(this.x, this.y, 0.0, 0.0) } }
            .reduce { acc, rect -> MKMapRectUnion(acc, rect) }

        map.setVisibleMapRect(boundingMapRect, edgePadding = edgeInsets, animated = true)
    }

    override fun zoomIn() {
        val map = map ?: return

        val currentZoom = map.getZoomLevel()

        val zoomLevel = currentZoom + 1

        map.setZoomLevel(zoomLevel)
    }

    override fun zoomOut() {
        val map = map ?: return

        val currentZoom = map.getZoomLevel()

        val zoomLevel = currentZoom - 1

        map.setZoomLevel(zoomLevel)
    }

    fun setMap(mapView: MKMapView?) {
        map = mapView
    }

    fun onMapUpdated() {
        val map = map ?: return

        zoomLevel = map.getZoomLevel()
    }
}

@Composable
actual fun rememberMapState(): MapState = remember { AppleMapState() }

fun MapState.setMap(mkMapView: MKMapView?) {
    (this as AppleMapState).setMap(mkMapView)
}

fun MapState.onMapUpdated() {
    (this as AppleMapState).onMapUpdated()
}

// https://stackoverflow.com/questions/4189621/setting-the-zoom-level-for-a-mkmapview

@OptIn(ExperimentalForeignApi::class)
private fun MKMapView.getZoomLevel(): Float {
    val width = frame.useContents { size.width }
    val region = region.useContents { span.longitudeDelta }

    return round(log2(FULL_ROTATION_DEGREES * (width / TILE_SIZE) / region)).toFloat()
}

@OptIn(ExperimentalForeignApi::class)
private fun MKMapView.setZoomLevel(zoomLevel: Float) = moveToCoordinate(centerCoordinate, zoomLevel)

@OptIn(ExperimentalForeignApi::class)
private fun MKMapView.moveToCoordinate(coordinate: CValue<CLLocationCoordinate2D>, zoomLevel: Float) {
    val width = frame.useContents { size.width }

    val span = MKCoordinateSpanMake(
        latitudeDelta = 0.0,
        longitudeDelta = FULL_ROTATION_DEGREES / 2.0.pow(zoomLevel.toDouble()) * (width / TILE_SIZE)
    )

    setRegion(MKCoordinateRegionMake(centerCoordinate = coordinate, span = span), animated = true)
}
