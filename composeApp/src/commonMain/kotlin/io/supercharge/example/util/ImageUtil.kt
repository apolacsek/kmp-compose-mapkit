package io.supercharge.example.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

fun Painter.toImageBitmap(
    density: Density,
    layoutDirection: LayoutDirection,
    size: Size = intrinsicSize
): ImageBitmap {
    val image = ImageBitmap(
        width = size.width.toInt(),
        height = size.height.toInt(),
        config = ImageBitmapConfig.Argb8888
    )

    val canvas = Canvas(image)

    // create a new draw scope
    CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
        // draw the painter on the canvas
        draw(size = this.size)
    }

    return image
}
