package io.supercharge.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.supercharge.example.Res
import io.supercharge.example.map_marker_check
import io.supercharge.example.map_marker_home
import io.supercharge.example.ui.component.LatLng
import io.supercharge.example.ui.component.MarkerInfoWindowComposable
import io.supercharge.example.ui.component.Polyline
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource

@Composable
fun Trip(route: ImmutableList<LatLng>) {
    Polyline(
        points = route,
        color = Color.Blue,
        width = 3.dp
    )

    MarkerInfoWindowComposable(
        position = route.first(),
        anchor = Offset(0.5f, 1f),
        infoContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Start", style = MaterialTheme.typography.headlineMedium)

                Text("Trip start marker", style = MaterialTheme.typography.bodyMedium)
            }
        }
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(Res.drawable.map_marker_home),
            contentDescription = "Start"
        )
    }

    MarkerInfoWindowComposable(
        position = route.last(),
        anchor = Offset(0.5f, 1f),
        infoContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Stop", style = MaterialTheme.typography.headlineMedium)

                Text("Trip stop marker", style = MaterialTheme.typography.bodyMedium)
            }
        }
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(Res.drawable.map_marker_check),
            contentDescription = "Stop"
        )
    }
}
