package dev.panuszewski.components

import androidx.compose.animation.core.tween

/**
 * Scene enter/exit fade used across the talk.
 *
 * Section scenes cross-fade rather than slide, so that the statically positioned titles rendered by
 * [SectionScaffold] dissolve into each other while the body content does its own slide.
 */
val SCENE_FADE = tween<Float>(durationMillis = 400)
