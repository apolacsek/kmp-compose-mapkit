package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import io.supercharge.example.ZOOM_LEVEL_THRESHOLD
import kotlinx.collections.immutable.ImmutableList

@Stable
interface MapState {

    val zoomLevel: Float

    val isCloseZoomLevel: Boolean
        get() = zoomLevel >= ZOOM_LEVEL_THRESHOLD

    fun jumpToBudapest()

    fun moveToCoordinates(
        coordinates: ImmutableList<LatLng>,
        padding: Int
    )

    fun zoomIn()

    fun zoomOut()
}

@Composable
expect fun rememberMapState(): MapState
