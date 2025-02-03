package io.supercharge.example.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.supercharge.example.ui.component.CommonMapComposable
import io.supercharge.example.ui.component.MapState
import io.supercharge.example.ui.component.rememberMapState

@Composable
expect fun CommonMap(
    modifier: Modifier = Modifier,
    mapState: MapState = rememberMapState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable @CommonMapComposable () -> Unit = {}
)
