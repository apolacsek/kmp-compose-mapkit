package io.supercharge.example.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import platform.UIKit.UIImage

fun Painter.toUIImage(
    density: Density,
    layoutDirection: LayoutDirection,
    size: Size = intrinsicSize,
    scale: Float = 1.0f
): UIImage = toImageBitmap(density, layoutDirection, size).toUIImage(scale)

suspend fun GraphicsLayer.toUIImage(scale: Float = 1.0f) = toImageBitmap().toUIImage(scale)
