package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.MagicText
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.drawCoordinateSystem
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .drawBehind { drawCoordinateSystem(axisLength, textMeasurer) }
            ) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .offset(
                            x = (-100 + (-300 * moveToCorners)).dp,
                            y = (-200 * moveToCorners).dp
                        )
                ) {
                    transition.AnimatedVisibility(
                        visible = { it.toState() >= 1 },
                        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    ) {
                        ResourceImage(
                            resource = Res.drawable.maven,
                            modifier = Modifier
                                .height(150.dp)
                                .scale(scale)
                        )
                    }
                }

                Box(
                    Modifier
                        .align(Alignment.Center)
                        .offset(
                            x = (100 + (300 * moveToCorners)).dp,
                            y = (200 * moveToCorners).dp
                        )
                ) {
                    transition.AnimatedVisibility(
                        visible = { it.toState() >= 1 },
                        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    ) {
                        ResourceImage(
                            resource = Res.drawable.gradle,
                            modifier = Modifier
                                .height(138.dp)
                                .scale(scale)
                        )
                    }
                }
            }
        }
    }
}