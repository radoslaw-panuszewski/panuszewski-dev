package dev.panuszewski.template

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween

val BoxMovementSpec: BoundsTransform = BoundsTransform { _, _ -> tween(durationMillis = ANIMATION_DURATION, delayMillis = 0, easing = EaseInOut) }
val TextMovementSpec: BoundsTransform = BoxMovementSpec