package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import io.supercharge.example.DEFAULT_LATITUDE
import io.supercharge.example.DEFAULT_LONGITUDE
import io.supercharge.example.DEFAULT_ZOOM
import kotlinx.collections.immutable.ImmutableList
import com.google.android.gms.maps.model.LatLng as GoogleLatLng

class AndroidMapState: MapState {

    var cameraState: CameraPositionState? by mutableStateOf(null)

    override val zoomLevel: Float by derivedStateOf { cameraState?.position?.zoom ?: DEFAULT_ZOOM }

    override fun jumpToBudapest() {
        val cameraState = cameraState ?: return

        cameraState.move(
            CameraUpdateFactory.newLatLngZoom(
                GoogleLatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE),
                DEFAULT_ZOOM
            )
        )
    }

    override fun moveToCoordinates(coordinates: ImmutableList<LatLng>, padding: Int) {
        val cameraState = cameraState ?: return

        val latLngBounds = LatLngBounds.builder()
            .apply { coordinates.forEach { include(GoogleLatLng(it.latitude, it.longitude)) } }
            .build()

        cameraState.move(
            CameraUpdateFactory.newLatLngBounds(
                latLngBounds,
                padding
            )
        )
    }

    override fun zoomIn() {
        val cameraState = cameraState ?: return

        cameraState.move(CameraUpdateFactory.zoomIn())
    }

    override fun zoomOut() {
        val cameraState = cameraState ?: return

        cameraState.move(CameraUpdateFactory.zoomOut())
    }
}

@Composable
actual fun rememberMapState(): MapState = remember { AndroidMapState() }

fun MapState.setCameraState(cameraState: CameraPositionState?) {
    (this as AndroidMapState).cameraState = cameraState
}
