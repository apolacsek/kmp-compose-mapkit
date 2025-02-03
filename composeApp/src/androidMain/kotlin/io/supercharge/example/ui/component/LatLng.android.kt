package io.supercharge.example.ui.component

import io.supercharge.example.model.Coordinate
import com.google.android.gms.maps.model.LatLng as GoogleLatLng

actual fun Coordinate.toLatLng(): LatLng = AndroidLatLng(GoogleLatLng(latitude, longitude))

class AndroidLatLng(val data: GoogleLatLng) : LatLng {

    override val latitude: Double = data.latitude

    override val longitude: Double = data.longitude

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is AndroidLatLng) return false

        return data == other.data
    }

    override fun hashCode(): Int = data.hashCode()
}

val LatLng.data: GoogleLatLng
    get() = (this as AndroidLatLng).data
