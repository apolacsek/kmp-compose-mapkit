package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset

@Composable
@CommonMapComposable
expect fun MarkerComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset = Offset(0.5f, 1.0f),
    content: @Composable () -> Unit
)

@Composable
@CommonMapComposable
expect fun MarkerInfoWindowComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset = Offset(0.5f, 1.0f),
    infoContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
)
