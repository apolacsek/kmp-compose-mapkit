package io.supercharge.example.ui.component

import androidx.compose.runtime.Stable
import io.supercharge.example.model.Coordinate

@Stable
interface LatLng {
    val latitude: Double
    val longitude: Double
}

expect fun Coordinate.toLatLng(): LatLng
