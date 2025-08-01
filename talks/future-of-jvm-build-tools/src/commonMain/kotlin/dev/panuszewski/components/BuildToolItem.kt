package dev.panuszewski.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.ResourceImage
import org.jetbrains.compose.resources.DrawableResource

@Composable
context(sceneScope: SceneScope<Int>, boxScope: BoxScope, chartContext: BuildToolChartContext)
fun BuildToolItem(
    resource: DrawableResource,
    initialX: Dp,
    targetX: Dp,
    targetY: Dp,
    scale: Float,
    height: Dp,
    isVisible: Transition<Boolean>,
    slideDirection: SlideDirection,
) = with(boxScope) {
    val moveToTarget by sceneScope.transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        targetValueByState = { if (it.toState() >= chartContext.moveItemsToTargetSinceState) 1f else 0f }
    )

    Box(
        Modifier
            .align(Alignment.Center)
            .offset(
                x = (initialX + ((targetX - initialX) * moveToTarget)),
                y = (targetY * moveToTarget)
            )
    ) {
        isVisible.AnimatedVisibility(
            visible = { it },
            enter = slideDirection.enter,
            exit = slideDirection.exit
        ) {
            ResourceImage(
                resource = resource,
                modifier = Modifier
                    .height(height)
                    .scale(scale)
            )
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