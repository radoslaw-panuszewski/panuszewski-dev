package dev.panuszewski.scenes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.AnimatedTitle
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.animateTextStyle
import dev.panuszewski.template.extensions.startWith

fun StoryboardBuilder.WhyWouldYouCareScene() {
    scene(stateCount = 3) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val durationMillis = 500
            val isLargeTitle = transition.createChildTransition { it.toState() == 0 }

            val titleTextStyle by isLargeTitle.animateTextStyle(
                targetValueByState = { isLarge ->
                    if (isLarge) MaterialTheme.typography.h2 else MaterialTheme.typography.h4
                },
                transitionSpec = { tween(durationMillis) }
            )

            val titleVerticalOffset by isLargeTitle.animateDp(
                targetValueByState = { isLarge -> if (isLarge) 0.dp else -200.dp },
                transitionSpec = { tween(durationMillis) }
            )

            ProvideTextStyle(titleTextStyle) {
                AnimatedTitle(
                    title = AnnotatedString("Why would you care?"),
                    modifier = Modifier.offset(y = titleVerticalOffset)
                )
            }

            transition.SlideFromBottomAnimatedVisibility({ it.toState() >= 2 }) {
                val files = buildList {
                    addFile(
                        name = "build.gradle.kts",
                        content = transition.createChildTransition {
                            BUILD_GRADLE_KTS.first()
                        }
                    )
                }
                IDE(
                    IdeState(
                        files = files,
                        selectedFile = "build.gradle.kts",
                    ),
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 132.dp, bottom = 32.dp)
                )
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    """
    """.trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
}