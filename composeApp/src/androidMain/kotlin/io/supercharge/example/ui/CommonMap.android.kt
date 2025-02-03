package io.supercharge.example.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import io.supercharge.example.ui.component.CommonMapComposable
import io.supercharge.example.ui.component.MapState
import io.supercharge.example.ui.component.setCameraState

@Composable
actual fun CommonMap(
    modifier: Modifier,
    mapState: MapState,
    contentPadding: PaddingValues,
    content: @Composable @CommonMapComposable () -> Unit
) {
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
        ),
        contentPadding = contentPadding,
        content = content
    )

    DisposableEffect(mapState, cameraPositionState) {
        mapState.setCameraState(cameraPositionState)

        onDispose { mapState.setCameraState(null) }
    }
}
