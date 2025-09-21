package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith

fun StoryboardBuilder.WhyWouldYouCareScene() {
    scene(stateCount = 3) {
        SlidingTitleScaffold("Why would you care?") {
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