package io.supercharge.example

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
expect fun CommonMap(
    startLocation: Marker,
    stopLocation: Marker,
    route: ImmutableList<Coordinate>,
    modifier: Modifier = Modifier
)
