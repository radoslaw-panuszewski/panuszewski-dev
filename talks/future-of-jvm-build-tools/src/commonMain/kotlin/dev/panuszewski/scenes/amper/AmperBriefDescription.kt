package dev.panuszewski.scenes.amper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.theme.withPrimaryColor
import dev.panuszewski.template.extensions.withIntTransition

fun StoryboardBuilder.AmperBriefDescription() {
    scene(stateCount = 5) {
        withIntTransition {
            TitleScaffold("Amper") {
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
                                append(" executable")
                            }
                        }
                        item { Text { append("Focused on UX and toolability") } }
                    }
                }
            }
        }
    }
}