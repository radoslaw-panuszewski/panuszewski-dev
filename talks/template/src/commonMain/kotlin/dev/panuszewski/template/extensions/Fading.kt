package dev.panuszewski.template.extensions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> Transition<T>.FadeInOutAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeInFast(),
    exit: ExitTransition = fadeOutFast(),
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(visible, modifier, enter, exit, content)

@Composable
fun <S> Transition<S>.FadeInOutAnimatedContent(
    modifier: Modifier = Modifier,
    transitionSpec: AnimatedContentTransitionScope<S>.() -> ContentTransform = { fadeIn() togetherWith fadeOut() },
    contentAlignment: Alignment = Alignment.TopStart,
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable() AnimatedContentScope.(targetState: S) -> Unit
) = AnimatedContent(modifier, transitionSpec, contentAlignment, contentKey, content)

fun fadeInFast() = fadeIn(fadeInSpec())
fun fadeOutFast() = fadeOut(fadeOutSpec())

val ANIMATION_DURATION = 350

fun <T> fadeInSpec(): TweenSpec<T> =
    tween(durationMillis = ANIMATION_DURATION, delayMillis = 0, easing = EaseIn)

fun <T> fadeOutSpec(): TweenSpec<T> =
    tween(durationMillis = ANIMATION_DURATION, delayMillis = 0, easing = EaseOut)
