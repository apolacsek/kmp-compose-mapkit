package io.supercharge.example.ui.component

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class CommonMapAnnotation(
    val identifier: String,
    private val position: LatLng,
    private val viewCreator: (MKAnnotationProtocol) -> MKAnnotationView,
    private val viewBinder: (MKAnnotationView) -> Unit
): NSObject(), MKAnnotationProtocol {

    override fun coordinate(): CValue<CLLocationCoordinate2D> = position.data

    fun createView(): MKAnnotationView = viewCreator(this)

    fun bindView(view: MKAnnotationView) = viewBinder(view)
}
