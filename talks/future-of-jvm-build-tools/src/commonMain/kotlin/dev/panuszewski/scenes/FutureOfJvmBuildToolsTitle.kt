package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.extensions.safeGet

val TITLE = listOf(
    buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(Color(54, 161, 165))) { append("Future") }
        append(" of JVM Build Tools")
    },
    buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(Color(148, 45, 243))) { append("Present") }
        append(" of JVM Build Tools")
    }
)

fun StoryboardBuilder.FutureOfJvmBuildToolsTitle(animateToPresent: Boolean) {
    scene(
        stateCount = if (animateToPresent) 2 else 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    ) {
        val titleTransition = transition.createChildTransition { TITLE.safeGet(it.toState()) }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProvideTextStyle(MaterialTheme.typography.h2) {
                titleTransition.MagicAnnotatedString(
                    Modifier
                        .offset(y = -16.dp)
                        .padding(start = 64.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/Title"),
                            animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        )
                )
            }
        }
    }
}
