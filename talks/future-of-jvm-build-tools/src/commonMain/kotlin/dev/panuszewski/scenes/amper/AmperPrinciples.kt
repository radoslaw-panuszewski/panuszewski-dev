package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.panuszewski.components.IDE
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.Stages
import dev.panuszewski.template.Text
import dev.panuszewski.template.withPrimaryColor

@Composable
fun Transition<Int>.AmperPrinciples(stages: Stages, title: MutableState<String>) {
    val initialState = stages.lastState + 1
    val finalState = initialState + 5

    if (currentState <= finalState) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RevealSequentially(since = initialState + 2) {
                    item {
                        Text {
                            withPrimaryColor { append("Show up when needed") }
                            append(", stay out of the way when not")
                        }
                    }
                    item {
                        Text {
                            withPrimaryColor { append("Catch errors early") }
                            append(", promote configuration that reflects reality")
                        }
                    }
                    item {
                        Text {
                            append("Minimize ")
                            withPrimaryColor { append("user interactions") }
                            append(" with the build config")
                        }
                    }
                    item {
                        Text {
                            append("Provide smooth ")
                            withPrimaryColor { append("IDE integration") }
                        }
                    }
                }
            }

            if (currentState in initialState..finalState) {
                IDE(
                    ideState = AMPER_IDE_STATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, top = 281.dp, bottom = 32.dp)
                )
            }
        }

        val states = stages.registerStatesByRange(start = initialState, end = finalState)

        if (currentState in states) {
            title.value = "Amper Principles"
        }
    }
}