package io.supercharge.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.collections.immutable.ImmutableList
import com.google.maps.android.compose.Marker as ComposeMarker

@Composable
actual fun CommonMap(
    startLocation: Marker,
    stopLocation: Marker,
    route: ImmutableList<Coordinate>,
    modifier: Modifier
) {
    val coordinates = route.map { LatLng(it.latitude, it.longitude) }

    val boundsPadding = with(LocalDensity.current) { 16.dp.roundToPx() }

    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = false,
            myLocationButtonEnabled = false,
            zoomControlsEnabled = false,
            scrollGesturesEnabled = true,
            tiltGesturesEnabled = true,
            zoomGesturesEnabled = true
        )
    ) {
        Polyline(
            points = coordinates,
            color = Color.Blue,
            width = with(LocalDensity.current) { 3.dp.toPx() }
        )

        ComposeMarker(
            state = rememberMarkerState(
                key = "start",
                position = LatLng(startLocation.coordinate.latitude, startLocation.coordinate.longitude)
            )
        )

        ComposeMarker(
            state = rememberMarkerState(
                key = "stop",
                position = LatLng(stopLocation.coordinate.latitude, stopLocation.coordinate.longitude)
            )
        )
    }

    LaunchedEffect(coordinates) {
        val bounds = LatLngBounds.builder()
            .apply {
                coordinates.forEach { include(it) }
            }
            .build()

        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, boundsPadding))
    }
}
