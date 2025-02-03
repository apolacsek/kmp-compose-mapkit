package io.supercharge.example.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.supercharge.example.ui.component.CommonMapComposable
import io.supercharge.example.ui.component.MapState
import io.supercharge.example.ui.component.onMapUpdated
import io.supercharge.example.ui.component.setMap
import kotlinx.cinterop.ExperimentalForeignApi
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.UIKit.NSDirectionalEdgeInsetsMake

@Composable
actual fun CommonMap(
    modifier: Modifier,
    mapState: MapState,
    contentPadding: PaddingValues,
    content: @Composable @CommonMapComposable () -> Unit
) {
    val mkMapView = rememberMkMapView()
    val mkMapViewDelegate = rememberMKMapViewDelegate {
        mapState.onMapUpdated()
    }

    AppleMap(
        modifier = modifier,
        mkMapView = mkMapView,
        mkMapViewDelegate = mkMapViewDelegate,
        contentPadding = contentPadding,
        content = content
    )

    DisposableEffect(mkMapView, mapState) {
        mapState.setMap(mkMapView)

        onDispose {
            mapState.setMap(null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun AppleMap(
    modifier: Modifier = Modifier,
    mkMapView: MKMapView = rememberMkMapView(),
    mkMapViewDelegate: MKMapViewDelegateProtocol = rememberMKMapViewDelegate(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    val directionalEdgeInsets = remember(layoutDirection, contentPadding) {
        NSDirectionalEdgeInsetsMake(
            top = contentPadding.calculateTopPadding().value.toDouble(),
            leading = contentPadding.calculateStartPadding(layoutDirection).value.toDouble(),
            bottom = contentPadding.calculateBottomPadding().value.toDouble(),
            trailing = contentPadding.calculateEndPadding(layoutDirection).value.toDouble()
        )
    }

    UIKitView(
        modifier = modifier,
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        ),
        factory = {
            mkMapView.apply {
                setZoomEnabled(true)
                setScrollEnabled(true)
                setRotateEnabled(true)
                setPitchEnabled(true)

                setShowsCompass(false)

                // customize map view further
            }
        },
        update = { mapView ->
            mapView.delegate = mkMapViewDelegate
            mapView.directionalLayoutMargins = directionalEdgeInsets
        }
    )

    CompositionLocalProvider(LocalMapView provides mkMapView) {
        content()
    }
}
