package dev.panuszewski.scenes.gradle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.scenes.gradle.GradlePhase.CONFIGURATION
import dev.panuszewski.scenes.gradle.GradlePhase.EXECUTION
import dev.panuszewski.scenes.gradle.GradlePhase.INITIALIZATION
import dev.panuszewski.template.AnimatedVisibility
import dev.panuszewski.template.MagicString

enum class GradlePhase { INITIALIZATION, CONFIGURATION, EXECUTION }

@Composable
fun PhasesBar(
    phasesBarVisible: Transition<Boolean>,
    highlightedPhase: Transition<GradlePhase?>,
    executionIsLong: Transition<Boolean>,
    configurationIsLong: Transition<Boolean>,
) {
    val phaseNameTextStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.background)
    val phaseDescriptionTextStyle = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.background, fontSize = 12.sp)
    val executionPhaseWeight by executionIsLong.animateFloat { if (it) 1.5f else 0.5f }
    val configurationPhaseWeight by configurationIsLong.animateFloat { if (it) 1.5f else 0.5f }

    phasesBarVisible.AnimatedVisibility {
        Row(Modifier.fillMaxWidth().padding(horizontal = 100.dp)) {
            Column(Modifier.background(color = MaterialTheme.colors.primary)) {
                ProvideTextStyle(phaseNameTextStyle) { Text("Initialization", Modifier.padding(16.dp)) }
                highlightedPhase.AnimatedVisibility({ it == INITIALIZATION }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Figure out the project structure", modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Column(
                Modifier.fillMaxWidth().weight(configurationPhaseWeight)
                    .background(color = MaterialTheme.colors.primaryVariant)
            ) {
                ProvideTextStyle(phaseNameTextStyle) {
                    configurationIsLong.createChildTransition {
                        if (it) "Configuraaaaaaaaaation"
                        else "Configuration"
                    }.MagicString(modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                }
                highlightedPhase.AnimatedVisibility({ it == CONFIGURATION }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Figure out the task graph", modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Column(
                Modifier.fillMaxWidth().weight(executionPhaseWeight)
                    .background(color = MaterialTheme.colors.secondary)
            ) {
                ProvideTextStyle(phaseNameTextStyle) {
                    executionIsLong.createChildTransition {
                        if (it) "Execuuuuuuuuuution"
                        else "Execution"
                    }.MagicString(modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                }
                highlightedPhase.AnimatedVisibility({ it == EXECUTION }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Execute the tasks! 🚀", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
    if (phasesBarVisible.currentState) {
        Spacer(Modifier.height(32.dp))
    }
}