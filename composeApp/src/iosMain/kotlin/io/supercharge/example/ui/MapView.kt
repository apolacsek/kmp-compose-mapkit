package io.supercharge.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import platform.MapKit.MKMapView
import platform.MapKit.overlays
import platform.MapKit.removeOverlays

@Composable
fun rememberMkMapView(): MKMapView {
    val mapView = remember { MKMapView() }

    DisposableEffect(mapView) {
        onDispose {
            mapView.setDelegate(null)
            mapView.removeAnnotations(mapView.annotations)
            mapView.removeOverlays(mapView.overlays)
        }
    }

    return mapView
}

val LocalMapView = staticCompositionLocalOf<MKMapView> {
    error("No LocalMapView provided")
}
