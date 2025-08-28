package dev.panuszewski.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.SlideDirection

@Composable
context(sceneScope: SceneScope<Int>, boxScope: BoxScope, chartContext: BuildToolChartContext)
fun BuildToolItem(
    slideDirection: SlideDirection,
    initialX: Dp = 0.dp,
    initialY: Dp = 0.dp,
    targetX: Dp = initialX,
    targetY: Dp = initialY,
    visibleSince: Int? = null,
    moveToTargetSince: Int? = null,
    content: @Composable (Modifier) -> Unit,
) = with(boxScope) {

    val isVisibleSince = visibleSince ?: chartContext.itemsVisibleSinceState ?: 0
    val moveSince = moveToTargetSince ?: chartContext.moveItemsToTargetSinceState ?: 0
    val scaleSince = moveToTargetSince ?: chartContext.makeItemsSmallSince ?: 0

    val moveToTarget by sceneScope.transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        targetValueByState = { if (it.toState() >= moveSince) 1f else 0f }
    )

    val scale by sceneScope.transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        targetValueByState = { if (it.toState() >= scaleSince) 0.5f else 1f }
    )

    Box(
        Modifier
            .align(Alignment.Center)
            .offset(
                x = (initialX + ((targetX - initialX) * moveToTarget)),
                y = initialY + ((targetY - initialY) * moveToTarget)
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
