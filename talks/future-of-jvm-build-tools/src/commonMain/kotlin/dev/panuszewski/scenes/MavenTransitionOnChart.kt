package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.panuszewski.components.BuildToolChart
import dev.panuszewski.components.BuildToolItem
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.SlideDirection.FROM_LEFT
import dev.panuszewski.template.SlideDirection.FROM_RIGHT
import dev.panuszewski.template.code1
import dev.panuszewski.template.withPrimaryColor
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.amper
import talks.future_of_jvm_build_tools.generated.resources.gradle
import talks.future_of_jvm_build_tools.generated.resources.ideal_build_tool
import talks.future_of_jvm_build_tools.generated.resources.maven

fun StoryboardBuilder.MavenTransitionOnChart() {
    scene(stateCount = 3) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))

            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text(
                    text = buildAnnotatedString {
                        append("Maven")
                        withPrimaryColor { append(" 4") }
                    },
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/Title"),
                            animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        )
                )
            }

            BuildToolChart(
                itemsVisibleSince = 1,
                drawAxesSince = 1,
                makeItemsSmallSince = 1,
                moveItemsToTargetSince = 2
            ) {
                BuildToolItem(
                    slideDirection = FROM_LEFT,
                    initialX = -200.dp,
                    initialY = -120.dp,
                ) {
                    ResourceImage(Res.drawable.gradle, modifier = it.height(138.dp))
                }

                BuildToolItem(
                    slideDirection = FROM_RIGHT,
                    initialX = -100.dp,
                    initialY = 1.dp,
                    targetX = 0.dp
                ) {
                    ResourceImage(Res.drawable.maven, modifier = it.height(150.dp))
                }

                BuildToolItem(
                    slideDirection = FROM_RIGHT,
                    initialX = 200.dp,
                    initialY = 120.dp,
                ) {
                    Column(modifier = it, horizontalAlignment = Alignment.CenterHorizontally) {
                        ResourceImage(Res.drawable.amper, modifier = Modifier.height(120.dp))
                        ProvideTextStyle(MaterialTheme.typography.h5) { Text("Amper", fontWeight = Bold) }
                    }
                }

                BuildToolItem(
                    slideDirection = FROM_RIGHT,
                    initialX = 380.dp,
                    initialY = -180.dp,
                ) {
                    Column(modifier = it, horizontalAlignment = Alignment.CenterHorizontally) {
                        ResourceImage(Res.drawable.ideal_build_tool, modifier = it.height(180.dp))
                        ProvideTextStyle(MaterialTheme.typography.code1) {
                            Text(
                                text = "Ideal Build Tool",
                                fontSize = 20.sp,
                                fontWeight = Bold,
                                modifier = Modifier.offset(y = -32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}