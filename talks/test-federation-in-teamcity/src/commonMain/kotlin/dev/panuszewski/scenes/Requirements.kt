package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Requirement
import dev.panuszewski.components.appendRequirement
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.withIntTransition

fun StoryboardBuilder.Requirements() {
    val requirementsAppear = 1
    val lastState = requirementsAppear + Requirement.entries.size

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            SectionScaffold("Requirements") {
                CenteredColumn(Modifier.fillMaxWidth(), spacing = 16.dp) {
                    RevealSequentially(since = requirementsAppear) {
                        Requirement.entries.forEach { requirement ->
                            annotatedStringItem {
                                appendRequirement(requirement)
                            }
                        }
                    }
                }
            }
        }
    }
}
