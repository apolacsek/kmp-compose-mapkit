package io.supercharge.example.ui.component

import io.supercharge.example.model.Coordinate
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake

@OptIn(ExperimentalForeignApi::class)
actual fun Coordinate.toLatLng(): LatLng = AppleLatLng(CLLocationCoordinate2DMake(latitude, longitude))

@OptIn(ExperimentalForeignApi::class)
internal class AppleLatLng(val data: CValue<CLLocationCoordinate2D>) : LatLng {

    override val latitude: Double = data.useContents { latitude }

    override val longitude: Double = data.useContents { longitude }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppleLatLng) return false

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}

@OptIn(ExperimentalForeignApi::class)
internal val LatLng.data: CValue<CLLocationCoordinate2D>
    get() = (this as AppleLatLng).data
