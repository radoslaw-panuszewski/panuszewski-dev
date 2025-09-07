package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.Title
import dev.panuszewski.template.Stages

/**
 * - [ ] avoid touching build tool
 * - [ ] it tells you that dependency version is overridden by a newer one (Gradle does not do that)
 * - [ ] configuration that reflects reality
 * - [x] ./amper show settings (pokazać default wersję spring boota)
 * - [x] show up when needed, stay out of the way when not
 * - [ ] smart completion (like when typed 'jdk' it finds 'jvm.release' property)
 */
fun StoryboardBuilder.Amper() {
    scene(100) {
        val stages = Stages()
        val stateTransition = transition.createChildTransition { it.toState() }
        val title = remember { mutableStateOf("") }

        if (stateTransition.currentState <= stages.lastState) {
            title.value = "Amper"
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            with(stateTransition) {
                Title(title.value)
                AmperBriefDescription(stages, title)
                AmperSpringBoot(stages, title)
                AmperPrinciples(stages, title)
            }
        }
    }
}

