package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState
import dev.panuszewski.stages.BoxMovementSpec
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_1
import dev.panuszewski.stages.TextMovementSpec

fun StoryboardBuilder.Kotlin2_1(startState: Int = 0) {
    scene(stateCount = 4 - startState) {
        Box(Modifier.padding(horizontal = 200.dp, vertical = 100.dp)) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("box/$KOTLIN_2_1"),
                        animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        boundsTransform = BoxMovementSpec
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(Modifier.height(16.dp))
                    Header()
                    Spacer(Modifier.height(16.dp))
                    Bulletpoints(startState)
                }
            }
        }
    }
}

@Composable
context(globalAnimation: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun Header() {
    Text(
        KOTLIN_2_1.displayName,
        style = MaterialTheme.typography.h4,
        modifier = Modifier
            .sharedBounds(
                sharedContentState = rememberSharedContentState("text/$KOTLIN_2_1"),
                animatedVisibilityScope = globalAnimation,
                boundsTransform = TextMovementSpec
            )
    )
}

@Composable
context(globalAnimation: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun SceneScope<Int>.Bulletpoints(startState: Int) {

    val highlightedColorTransition = transition.createChildTransition { frame ->
        when (frame) {
            Frame.End -> MaterialTheme.colors.secondary
            else -> Color.Unspecified
        }
    }

    Box(Modifier.padding(16.dp)) {
        val items = listOf(
            "Guard Conditions",
            "Non-local break & continue",
            "Multi-dollar interpolation"
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            RevealEach(transition = transition.createChildTransition { it.toState() + startState - 1 }) {
                for (value in items) {
                    item {
                        highlightedColorTransition.AnimatedContent {
                            Text(
                                text = AnnotatedString("$BULLET_1 $value"),
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState("text/$value"),
                                        animatedVisibilityScope = globalAnimation,
                                        boundsTransform = TextMovementSpec
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}

