package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.panuszewski.template.BoxMovementSpec
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.TextMovementSpec

@Composable
context(animatedVisibilityScope: AnimatedVisibilityScope, _: SharedTransitionScope)
fun TimelineStageBox(
    stage: KotlinTimelineStage,
    visible: Transition<Boolean>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(110.dp)
    ) {
        visible.FadeInOutAnimatedVisibility(visible = { it }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("box/$stage"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = BoxMovementSpec
                    )
                    .defaultMinSize(minWidth = 110.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    stage.displayName,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/$stage"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = TextMovementSpec
                        )
                )
            }
        }
    }
}