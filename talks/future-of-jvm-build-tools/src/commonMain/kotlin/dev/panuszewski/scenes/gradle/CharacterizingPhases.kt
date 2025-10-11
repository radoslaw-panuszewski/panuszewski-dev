package dev.panuszewski.scenes.gradle

import androidx.compose.animation.core.createChildTransition
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.scenes.gradle.GradlePhase.*
import dev.panuszewski.template.extensions.withIntTransition

fun StoryboardBuilder.CharacterizingPhases() {
    scene(6) {
        withIntTransition {
            TitleScaffold("Gradle") {
                PhasesBar(
                    phasesBarVisible = createChildTransition { it >= 1 },
                    highlightedPhase = createChildTransition {
                        when (it) {
                            2 -> INITIALIZATION
                            3 -> CONFIGURATION
                            4 -> EXECUTION
                            else -> null
                        }
                    },
                    executionIsLong = createChildTransition { false },
                    configurationIsLong = createChildTransition { false },
                )
            }
        }
    }
}