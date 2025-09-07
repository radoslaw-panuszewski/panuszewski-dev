package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.Stages
import dev.panuszewski.template.Text
import dev.panuszewski.template.withPrimaryColor

@Composable
fun Transition<Int>.AmperBriefDescription(stages: Stages, title: MutableState<String>) {
    val initialState = stages.lastState + 1

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val count = RevealSequentially(since = initialState) {
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

        val states = stages.registerStatesByCount(start = initialState, count = count)

        if (currentState in states) {
            title.value = "Amper"
        }
    }
}