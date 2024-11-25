package io.supercharge.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun App() {
    val polyline = stringResource(Res.string.polyline)

    val route = remember(polyline) { decode(polyline).toImmutableList() }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CommonMap(
                startLocation = Marker(
                    title = "Start",
                    coordinate = route.first()
                ),
                stopLocation = Marker(
                    title = "Stop",
                    coordinate = route.last()
                ),
                route = route,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
