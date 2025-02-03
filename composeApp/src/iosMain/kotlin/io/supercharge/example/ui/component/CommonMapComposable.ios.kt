package io.supercharge.example.ui.component

import androidx.compose.runtime.ComposableTargetMarker

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@Retention(AnnotationRetention.BINARY)
@ComposableTargetMarker(description = "MapKit Map Composable")
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
)
actual annotation class CommonMapComposable
