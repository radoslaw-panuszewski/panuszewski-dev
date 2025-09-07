package dev.panuszewski.scenes.amper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.Title
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.Text
import dev.panuszewski.template.withPrimaryColor
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.AmperBriefDescription() {
    scene(stateCount = 5) {
        withStateTransition {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (transition.currentState) {
                    is Frame.State<*> -> AMPER_TITLE = "Amper"
                    else -> {}
                }
                Title(AMPER_TITLE)

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RevealSequentially(since = 1) {
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