package io.supercharge.example.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import io.supercharge.example.ui.LocalMapView
import io.supercharge.example.util.toUIImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGPointMake
import platform.MapKit.MKAnnotationView
import platform.UIKit.UIImage
import platform.UIKit.UIImageView

@CommonMapComposable
@Composable
actual fun MarkerComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset,
    content: @Composable () -> Unit
) {
    MarkerInfoWindowComposable(
        keys = keys,
        position = position,
        anchor = anchor,
        infoContent = null,
        content = content
    )
}

@CommonMapComposable
@Composable
actual fun MarkerInfoWindowComposable(
    vararg keys: Any,
    position: LatLng,
    anchor: Offset,
    infoContent: @Composable (() -> Unit)?,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val mapView = LocalMapView.current

    var annotation by remember { mutableStateOf<CommonMapAnnotation?>(null) }

    val contentGraphicsLayer = rememberGraphicsLayer()
    val currentContent by rememberUpdatedState(content)

    val infoContentGraphicsLayer = rememberGraphicsLayer()
    val currentInfoContent by rememberUpdatedState(infoContent)

    Box(
        modifier = Modifier.drawWithContent {
            contentGraphicsLayer.record { this@drawWithContent.drawContent() }
        }
    ) {
        currentContent()
    }

    if (currentInfoContent != null) {
        Box(
            modifier = Modifier.drawWithContent {
                infoContentGraphicsLayer.record { this@drawWithContent.drawContent() }
            }
        ) {
            currentInfoContent?.invoke()
        }
    }

    LaunchedEffect(*keys, currentContent, currentInfoContent) {
        val uiImage = withContext(Dispatchers.Main) {
            contentGraphicsLayer.toImageBitmap().toUIImage(density.density)
        }

        val infoUIImage = withContext(Dispatchers.Main) {
            if (infoContent != null) {
                infoContentGraphicsLayer.toImageBitmap().toUIImage(density.density)
            } else {
                null
            }
        }

        annotation?.let { mapView.removeAnnotation(it) }

        val reuseIdentifier = keys.joinToString()

        annotation = CommonMapAnnotation(
            identifier = reuseIdentifier,
            position = position,
            viewCreator = { MKAnnotationView(it, reuseIdentifier) },
            viewBinder = { convertView -> viewBinder(convertView, uiImage, infoUIImage, anchor) }
        ).also { mapView.addAnnotation(it) }
    }

    DisposableEffect(Unit) {
        onDispose {
            annotation?.let { mapView.removeAnnotation(it) }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun viewBinder(convertView: MKAnnotationView, uiImage: UIImage, infoUIImage: UIImage?, anchor: Offset) {
    convertView.image = uiImage
    convertView.centerOffset = CGPointMake(anchor.x.toDouble(), anchor.y.toDouble())

    if (infoUIImage != null) {
        convertView.canShowCallout = true
        convertView.detailCalloutAccessoryView = UIImageView().apply {
            image = infoUIImage
        }
    } else {
        convertView.canShowCallout = false
        convertView.detailCalloutAccessoryView = null
    }
}
