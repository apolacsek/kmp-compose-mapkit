package io.supercharge.example

import androidx.compose.runtime.Stable

@Stable
data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

@Stable
data class Marker(
    val title: String,
    val coordinate: Coordinate
)
