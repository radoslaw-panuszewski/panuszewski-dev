package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.drawCoordinateSystem
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.gradle
import talks.future_of_jvm_build_tools.generated.resources.maven

fun StoryboardBuilder.Overview() {
    scene(3) {
        val axisLength by transition.animateFloat(
            transitionSpec = { tween(durationMillis = 500) },
            targetValueByState = { if (it.toState() >= 2) 0.99f else 0f }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .drawBehind { drawCoordinateSystem(axisLength) }
        ) {
            Box(Modifier.align(Alignment.Center).offset(x = (-100).dp)) {
                transition.AnimatedVisibility(
                    visible = { it.toState() >= 1 },
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                ) {
                    ResourceImage(
                        resource = Res.drawable.maven,
                        modifier = Modifier.height(150.dp)
                    )
                }
            }

            Box(Modifier.align(Alignment.Center).offset(x = 100.dp)) {
                transition.AnimatedVisibility(
                    visible = { it.toState() >= 1 },
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    ResourceImage(
                        resource = Res.drawable.gradle,
                        modifier = Modifier.height(138.dp)
                    )
                }
            }
        }
    }
}

