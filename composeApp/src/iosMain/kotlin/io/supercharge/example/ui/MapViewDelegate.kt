package io.supercharge.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import io.supercharge.example.ui.component.CommonMapAnnotation
import io.supercharge.example.ui.component.CommonMapOverlay
import kotlinx.cinterop.ObjCSignatureOverride
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.darwin.NSObject

@Composable
fun rememberMKMapViewDelegate(onMapVisibleRegionChanged: (MKMapView) -> Unit = {}): MKMapViewDelegateProtocol {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val currentOnMapVisibleRegionChanged by rememberUpdatedState(onMapVisibleRegionChanged)

    return remember(density, layoutDirection, currentOnMapVisibleRegionChanged) {
        object : NSObject(), MKMapViewDelegateProtocol {

            override fun mapViewDidChangeVisibleRegion(mapView: MKMapView) = currentOnMapVisibleRegionChanged(mapView)

            @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            @ObjCSignatureOverride
            override fun mapView(mapView: MKMapView, viewForAnnotation: MKAnnotationProtocol): MKAnnotationView? {
                val annotation = viewForAnnotation as? CommonMapAnnotation ?: return null

                var annotationView = mapView.dequeueReusableAnnotationViewWithIdentifier(annotation.identifier)

                if (annotationView == null) {
                    annotationView = annotation.createView()
                } else {
                    annotationView.annotation = viewForAnnotation
                }

                annotation.bindView(annotationView)

                return annotationView
            }

            @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            @ObjCSignatureOverride
            override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
                val overlay = rendererForOverlay as? CommonMapOverlay ?: return MKOverlayRenderer(rendererForOverlay)

                return overlay.createRenderer()
            }
        }
    }
}
