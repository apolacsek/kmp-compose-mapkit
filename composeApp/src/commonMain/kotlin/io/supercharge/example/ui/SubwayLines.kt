package io.supercharge.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.supercharge.example.model.SubwayLine
import io.supercharge.example.ui.component.LatLng
import io.supercharge.example.ui.component.MarkerComposable
import io.supercharge.example.ui.component.Polyline
import io.supercharge.example.ui.component.toLatLng
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SubwayLines() {
    SubwayLine.entries.forEach { line ->
        SubwayLineItem(line = line)
    }
}

@Composable
private fun SubwayLineItem(line: SubwayLine) {
    val points = remember(line) { line.polyline.map { it.toLatLng() }.toImmutableList() }

    Polyline(
        points = points,
        color = line.color,
        width = 4.dp
    )

    SubwayLineEndStation(
        position = points.first(),
        color = line.color
    )

    SubwayLineEndStation(
        position = points.last(),
        color = line.color
    )
}

@Composable
private fun SubwayLineEndStation(
    position: LatLng,
    color: Color
) {
    MarkerComposable(
        position = position,
        anchor = Offset(0.5f, 0.5f)
    ) {
        Box(
            modifier = Modifier.size(8.dp)
                .background(color, shape = CircleShape)
                .padding(2.dp)
                .background(Color.White, shape = CircleShape)
        )
    }
}
