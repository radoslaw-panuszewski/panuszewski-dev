package dev.panuszewski.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState

@Composable
context(sceneScope: SceneScope<Int>, boxScope: BoxScope, chartContext: BuildToolChartContext)
fun BuildToolItem(
    slideDirection: SlideDirection,
    initialX: Dp,
    targetX: Dp,
    targetY: Dp,
    visibleSince: Int? = null,
    moveToTargetSince: Int? = null,
    content: @Composable (Modifier) -> Unit,
) = with(boxScope) {

    val isVisibleSince = visibleSince ?: chartContext.itemsVisibleSinceState ?: 0
    val moveAndScaleSince = moveToTargetSince ?: chartContext.moveItemsToTargetSinceState ?: 0

    val moveToTarget by sceneScope.transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        targetValueByState = { if (it.toState() >= moveAndScaleSince) 1f else 0f }
    )

    val scale by sceneScope.transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        targetValueByState = { if (it.toState() >= moveAndScaleSince) 0.5f else 1f }
    )

    Box(
        Modifier
            .align(Alignment.Center)
            .offset(
                x = (initialX + ((targetX - initialX) * moveToTarget)),
                y = (targetY * moveToTarget)
            )
    ) {
        sceneScope.transition.AnimatedVisibility(
            visible = { it.toState() >= isVisibleSince },
            enter = slideDirection.enter,
            exit = slideDirection.exit
        ) {
            content(Modifier.scale(scale))
        }
    }
}

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
    );
}