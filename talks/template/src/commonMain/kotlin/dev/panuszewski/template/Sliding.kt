package dev.panuszewski.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> Transition<T>.SlideFromLeftAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = SlideDirection.FROM_LEFT.enter,
    exit: ExitTransition = SlideDirection.FROM_LEFT.exit,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(visible, modifier, enter, exit, content)

@Composable
fun <T> Transition<T>.SlideFromRightAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = SlideDirection.FROM_RIGHT.enter,
    exit: ExitTransition = SlideDirection.FROM_RIGHT.exit,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(visible, modifier, enter, exit, content)

@Composable
fun <T> Transition<T>.SlideFromTopAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = SlideDirection.FROM_TOP.enter,
    exit: ExitTransition = SlideDirection.FROM_TOP.exit,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(visible, modifier, enter, exit, content)

@Composable
fun <T> Transition<T>.SlideFromBottomAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = SlideDirection.FROM_BOTTOM.enter,
    exit: ExitTransition = SlideDirection.FROM_BOTTOM.exit,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(visible, modifier, enter, exit, content)

enum class SlideDirection(
    val enter: EnterTransition,
    val exit: ExitTransition,
) {
    FROM_LEFT(
        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
    ),
    FROM_RIGHT(
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    ),
    FROM_TOP(
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ),
    FROM_BOTTOM(
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    );

    val combined: ContentTransform get() = enter togetherWith exit
}