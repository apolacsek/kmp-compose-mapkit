package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.model.RoundCap
import kotlinx.collections.immutable.ImmutableList
import com.google.maps.android.compose.Polyline as GooglePolyline

@CommonMapComposable
@Composable
actual fun Polyline(
    points: ImmutableList<LatLng>,
    color: Color,
    width: Dp
) {
    val googleLatLngList = remember(points) { points.map { it.data } }

    GooglePolyline(
        points = googleLatLngList,
        color = color,
        width = with(LocalDensity.current) { width.toPx() },
        endCap = RoundCap(),
        startCap = RoundCap()
    )
}
