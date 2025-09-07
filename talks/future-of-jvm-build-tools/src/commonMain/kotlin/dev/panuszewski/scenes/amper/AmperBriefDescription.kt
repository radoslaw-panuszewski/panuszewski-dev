package dev.panuszewski.scenes.amper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.Title
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.Stages
import dev.panuszewski.template.Text
import dev.panuszewski.template.withPrimaryColor
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.AmperBriefDescription(stages: Stages) {
    val initialState = stages.lastState
    val states = stages.registerStatesByCount(start = initialState, count = 4)

    scene(states = states) {
        withStateTransition {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Title("Amper")

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RevealSequentially(since = initialState + 1) {
                        item {
                            Text {
                                append("An ")
                                withPrimaryColor { append("experimental") }
                                append(" build tool from JetBrains")
                            }
                        }

                        item {
                            Text {
                                append("Started as a Gradle plugin, now ")
                                withPrimaryColor { append("standalone") }
                            }
                        }
                        item { Text { append("Focused on UX and toolability") } }
                    }
                }
            }
        }
    }
}