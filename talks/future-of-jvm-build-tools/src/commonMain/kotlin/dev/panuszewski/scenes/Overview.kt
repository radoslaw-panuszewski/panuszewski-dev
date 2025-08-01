package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.BuildToolItem
import dev.panuszewski.components.BuildToolChart
import dev.panuszewski.components.SlideDirection.FROM_LEFT
import dev.panuszewski.components.SlideDirection.FROM_RIGHT
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.gradle
import talks.future_of_jvm_build_tools.generated.resources.maven

@Suppress("FunctionName")
fun StoryboardBuilder.Overview() {
    scene(5) {
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

            val scale by transition.animateFloat(
                transitionSpec = { tween(durationMillis = 1000) },
                targetValueByState = { if (it.toState() >= 3) 0.5f else 1f }
            )

            val isVisible = transition.createChildTransition { it.toState() >= 1 }

            BuildToolChart(
                drawAxesSinceState = 2,
                moveItemsToTargetSinceState = 3
            ) {
                BuildToolItem(
                    resource = Res.drawable.gradle,
                    initialX = -100.dp,
                    targetX = -200.dp,
                    targetY = -120.dp,
                    scale = scale,
                    height = 138.dp,
                    isVisible = isVisible,
                    slideDirection = FROM_LEFT
                )

                BuildToolItem(
                    resource = Res.drawable.maven,
                    initialX = 100.dp,
                    targetX = -50.dp,
                    targetY = 1.dp,
                    scale = scale,
                    height = 150.dp,
                    isVisible = isVisible,
                    slideDirection = FROM_RIGHT
                )
            }
        }
    }
}

