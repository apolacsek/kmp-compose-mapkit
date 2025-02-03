package io.supercharge.example.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
@CommonMapComposable
expect fun Polyline(
    points: ImmutableList<LatLng>,
    color: Color = Color.Black,
    width: Dp = 2.dp
)
