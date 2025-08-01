package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.drawCoordinateSystem
import org.jetbrains.compose.resources.DrawableResource
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.gradle
import talks.future_of_jvm_build_tools.generated.resources.maven

fun StoryboardBuilder.Overview() {
    scene(4) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))

            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text(
                    text = TITLE[1],
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/Present"),
                            animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        )
                )
            }

            val axisLength by transition.animateFloat(
                transitionSpec = { tween(durationMillis = 500) },
                targetValueByState = { if (it.toState() >= 2) 0.99f else 0f }
            )

            val moveToCorners by transition.animateFloat(
                transitionSpec = { tween(durationMillis = 1000) },
                targetValueByState = { if (it.toState() >= 3) 1f else 0f }
            )

            val scale by transition.animateFloat(
                transitionSpec = { tween(durationMillis = 1000) },
                targetValueByState = { if (it.toState() >= 3) 0.5f else 1f }
            )

            val textMeasurer = rememberTextMeasurer()

            BuildToolsChart(
                axisLength = axisLength,
                textMeasurer = textMeasurer,
                xLabel = "Toolability",
                yLabel = "Extensibility"
            ) {
                // Gradle item in the top-left quadrant
                BuildToolItem(
                    resource = Res.drawable.gradle,
                    initialX = -100.dp,
                    targetX = -200.dp,
                    targetY = -120.dp,
                    moveToCorners = moveToCorners,
                    scale = scale,
                    height = 138.dp,
                    isVisible = transition.currentState.toState() >= 1,
                    slideInFromLeft = true
                )
                
                // Maven item in the bottom-right quadrant
                BuildToolItem(
                    resource = Res.drawable.maven,
                    initialX = 100.dp,
                    targetX = -50.dp,
                    targetY = 1.dp,
                    moveToCorners = moveToCorners,
                    scale = scale,
                    height = 150.dp,
                    isVisible = transition.currentState.toState() >= 1,
                    slideInFromLeft = false
                )
            }
        }
    }
}

/**
 * A composable that displays a chart of build tools with a coordinate system.
 */
@Composable
private fun BuildToolsChart(
    axisLength: Float,
    textMeasurer: TextMeasurer,
    xLabel: String,
    yLabel: String,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .drawBehind { drawCoordinateSystem(axisLength, textMeasurer, xLabel, yLabel) }
    ) {
        content()
    }
}

/**
 * A composable that displays a build tool item with animations.
 */
@Composable
private fun BoxScope.BuildToolItem(
    resource: DrawableResource,
    initialX: Dp,
    targetX: Dp,
    targetY: Dp,
    moveToCorners: Float,
    scale: Float,
    height: Dp,
    isVisible: Boolean,
    slideInFromLeft: Boolean = true
) {
    Box(
        Modifier
            .align(Alignment.Center)
            .offset(
                x = (initialX + ((targetX - initialX) * moveToCorners)),
                y = (targetY * moveToCorners)
            )
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(initialOffsetX = { if (slideInFromLeft) -it else it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { if (slideInFromLeft) -it else it }) + fadeOut()
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