package io.supercharge.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.supercharge.example.Res
import io.supercharge.example.ic_minus
import io.supercharge.example.ic_plus
import io.supercharge.example.polyline
import io.supercharge.example.ui.component.rememberMapState
import io.supercharge.example.ui.component.toLatLng
import io.supercharge.example.util.decode
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun App() {
    val polyline = stringResource(Res.string.polyline)

    val route = remember(polyline) { decode(polyline).map { it.toLatLng() }.toImmutableList() }

    val boundsPadding = with(LocalDensity.current) { 16.dp.roundToPx() }

    val mapState = rememberMapState()

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CommonMap(
                modifier = Modifier.fillMaxSize(),
                mapState = mapState,
                contentPadding = PaddingValues(16.dp)
            ) {
                if (mapState.isCloseZoomLevel) {
                    Trip(route)
                } else {
                    SubwayLines()
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                ZoomButton(painterResource(Res.drawable.ic_plus)) { mapState.zoomIn() }

                ZoomButton(painterResource(Res.drawable.ic_minus)) { mapState.zoomOut() }
            }
        }
    }

    LaunchedEffect(mapState, route) {
        mapState.moveToCoordinates(route, boundsPadding)
    }
}

@Composable
private fun ZoomButton(
    icon: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            .padding(2.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = icon,
                modifier = Modifier.size(24.dp),
                contentDescription = null
            )
        }
    }
}
