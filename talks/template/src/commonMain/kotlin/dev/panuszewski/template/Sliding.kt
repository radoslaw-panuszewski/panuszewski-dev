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
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_LEFT.enter(fraction),
    exit = SlideDirection.FROM_LEFT.exit(fraction),
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromRightAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_RIGHT.enter(fraction),
    exit = SlideDirection.FROM_RIGHT.exit(fraction),
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromTopAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_TOP.enter(fraction),
    exit = SlideDirection.FROM_TOP.exit(fraction),
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromBottomAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
): Unit = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_BOTTOM.enter(fraction),
    exit = SlideDirection.FROM_BOTTOM.exit(fraction),
    content = content
)

enum class SlideDirection(
    val enter: (Float) -> EnterTransition,
    val exit: (Float) -> ExitTransition,
) {
    FROM_LEFT(
        enter = { fraction -> slideInHorizontally(initialOffsetX = { -(it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutHorizontally(targetOffsetX = { -(it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_RIGHT(
        enter = { fraction -> slideInHorizontally(initialOffsetX = { (it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutHorizontally(targetOffsetX = { (it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_TOP(
        enter = { fraction -> slideInVertically(initialOffsetY = { -(it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutVertically(targetOffsetY = { -(it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_BOTTOM(
        enter = { fraction -> slideInVertically(initialOffsetY = { (it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutVertically(targetOffsetY = { (it * fraction).toInt() }) + fadeOut() },
    );

    val combined: (Float) -> ContentTransform get() = { enter(it) togetherWith exit(it) }
}