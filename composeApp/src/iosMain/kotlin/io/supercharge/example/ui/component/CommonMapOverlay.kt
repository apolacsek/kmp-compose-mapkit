package io.supercharge.example.ui.component

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.MapKit.MKMapRect
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class CommonMapOverlay(
    private val delegate: MKOverlayProtocol,
    private val rendererCreator: (MKOverlayProtocol) -> MKOverlayRenderer
) : NSObject(), MKOverlayProtocol {

    override fun coordinate(): CValue<CLLocationCoordinate2D> = delegate.coordinate()

    override fun boundingMapRect(): CValue<MKMapRect> = delegate.boundingMapRect()

    fun createRenderer(): MKOverlayRenderer = rendererCreator(delegate)
}
