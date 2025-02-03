package io.supercharge.example.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.util.fastForEach
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGColorSpaceRef
import platform.CoreGraphics.CGColorSpaceRelease
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageRef
import platform.CoreGraphics.CGImageRelease
import platform.CoreGraphics.kCGImageByteOrder32Little
import platform.UIKit.UIImage
import platform.UIKit.UIImageOrientation

// Implementation is based on the following articles:
// https://medium.com/@hafizmohd999/how-to-upload-file-image-into-server-in-kotlin-multiplatform-007197c8f593
// https://youtrack.jetbrains.com/issue/CMP-5903/My-UiImage-turn-into-blue-color#focus=Comments-27-10296385.0-0

@OptIn(ExperimentalForeignApi::class)
internal fun ImageBitmap.toCGImage(): CGImageRef? = withCFReleaseScope {
    if (config != ImageBitmapConfig.Argb8888) {
        throw NotImplementedError("Only ImageBitmapConfig.Argb8888 is supported")
    }

    val buffer = IntArray(width * height)

    // read the pixels from the ImageBitmap into the buffer
    readPixels(buffer)

    val colorSpace = CGColorSpaceCreateDeviceRGB()?.deferRelease()

    val bitmapInfo =
        CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst.value or kCGImageByteOrder32Little

    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u, // each channel (RGB and alpha for translucency) is stored with 8 bits of precision
        bytesPerRow = (4 * width).toULong(), // each pixel is stored on 4 bytes
        space = colorSpace,
        bitmapInfo = bitmapInfo
    )?.deferRelease()

    CGBitmapContextCreateImage(context)
}

@OptIn(ExperimentalForeignApi::class)
internal fun ImageBitmap.toUIImage(scale: Float = 1.0f): UIImage = withCFReleaseScope {
    val cgImage = toCGImage()?.deferRelease() ?: error("Failed to create CGImage")

    UIImage.imageWithCGImage(
        cgImage = cgImage,
        scale = scale.toDouble(),
        orientation = UIImageOrientation.UIImageOrientationUp
    )
}

@OptIn(ExperimentalForeignApi::class)
private sealed interface CFScopeReleasable {
    fun release()

    data class Image(val image: CGImageRef) : CFScopeReleasable {
        override fun release() = CGImageRelease(image)
    }

    data class ColorSpace(val colorSpace: CGColorSpaceRef) : CFScopeReleasable {
        override fun release() = CGColorSpaceRelease(colorSpace)
    }

    data class Context(val context: CGContextRef) : CFScopeReleasable {
        override fun release() = CGContextRelease(context)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class CFReleaseScope {
    private val items = mutableListOf<CFScopeReleasable>()

    fun release() = items.reversed().fastForEach { it.release() }

    private fun add(item: CFScopeReleasable) {
        items.add(item)
    }

    fun CGImageRef.deferRelease(): CGImageRef {
        add(CFScopeReleasable.Image(this))
        return this
    }

    fun CGContextRef.deferRelease(): CGContextRef {
        add(CFScopeReleasable.Context(this))
        return this
    }

    fun CGColorSpaceRef.deferRelease(): CGColorSpaceRef {
        add(CFScopeReleasable.ColorSpace(this))
        return this
    }
}

private fun <R> withCFReleaseScope(block: CFReleaseScope.() -> R): R {
    val scope = CFReleaseScope()

    return try {
        scope.block()
    } finally {
        scope.release()
    }
}
