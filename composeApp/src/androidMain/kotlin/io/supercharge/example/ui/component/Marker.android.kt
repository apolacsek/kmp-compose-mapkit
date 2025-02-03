package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.MarkerComposable as GoogleMarkerComposable
import com.google.maps.android.compose.MarkerInfoWindowComposable as GoogleMarkerInfoWindowComposable

@CommonMapComposable
@Composable
actual fun MarkerComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset,
    content: @Composable () -> Unit
) {
    GoogleMarkerComposable(
        keys = keys,
        state = rememberMarkerState(position = position.data),
        anchor = anchor,
        content = content
    )
}

@CommonMapComposable
@Composable
actual fun MarkerInfoWindowComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset,
    infoContent: @Composable (() -> Unit)?,
    content: @Composable () -> Unit
) {
    GoogleMarkerInfoWindowComposable(
        keys = keys,
        state = rememberMarkerState(position = position.data),
        anchor = anchor,
        infoContent = infoContent?.let { { infoContent() } },
        content = content
    )
}
