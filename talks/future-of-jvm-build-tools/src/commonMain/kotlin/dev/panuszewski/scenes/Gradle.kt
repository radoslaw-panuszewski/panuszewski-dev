package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.MagicCodeSample
import dev.panuszewski.template.MagicString
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.toCode

fun StoryboardBuilder.Gradle() {
    scene(100) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            ProvideTextStyle(MaterialTheme.typography.h4) { Text("Gradle") }
            Spacer(Modifier.height(64.dp))

            val stateTransition = transition.createChildTransition { it.toState() }
            val phaseNameTextStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.background)
            val phaseDescriptionTextStyle = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.background, fontSize = 12.sp)

            val executionIsLong = transition.createChildTransition { it.toState() in listOf(6, 7, 8, 9) }
            val configurationIsLong = transition.createChildTransition { it.toState() in listOf(11) }

            val executionPhaseWeight by executionIsLong.animateFloat { if (it) 1.5f else 0.5f }
            val configurationPhaseWeight by configurationIsLong.animateFloat { if (it) 1.5f else 0.5f }

            stateTransition.AnimatedVisibility({ it >= 1 }) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 100.dp)) {
                    Column(Modifier.background(color = MaterialTheme.colors.primary)) {
                        ProvideTextStyle(phaseNameTextStyle) { Text("Initialization", Modifier.padding(16.dp)) }
                        stateTransition.AnimatedVisibility({ it == 2 }) {
                            ProvideTextStyle(phaseDescriptionTextStyle) {
                                Text(text = "Figure out the project structure", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    Column(Modifier.fillMaxWidth().weight(configurationPhaseWeight).background(color = MaterialTheme.colors.primaryVariant)) {
                        ProvideTextStyle(phaseNameTextStyle) {
                            configurationIsLong.createChildTransition {
                                if (it) "Configuraaaaaaaaaation"
                                else "Configuration"
                            }.MagicString(
                                modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                        }
                        stateTransition.AnimatedVisibility({ it == 3 }) {
                            ProvideTextStyle(phaseDescriptionTextStyle) {
                                Text(text = "Figure out the task graph", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    Column(Modifier.fillMaxWidth().weight(executionPhaseWeight).background(color = MaterialTheme.colors.secondary)) {
                        ProvideTextStyle(phaseNameTextStyle) {
                            executionIsLong.createChildTransition {
                                if (it) "Execuuuuuuuuuution"
                                else "Execution"
                            }.MagicString(
                                modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                        }
                        stateTransition.AnimatedVisibility({ it == 4 }) {
                            ProvideTextStyle(phaseDescriptionTextStyle) {
                                Text(text = "Execute the tasks! 🚀", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))

            stateTransition.AnimatedVisibility({ it in listOf(7, 8, 9) }, enter = EnterTransition.None, exit = fadeOut()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ProvideTextStyle(MaterialTheme.typography.h6) {
                        stateTransition.AnimatedVisibility({ it >= 7 }, enter = slideInVertically(), exit = slideOutVertically()) {
                            Text("Build Cache is there since Gradle 3.5 👴🏼")
                        }
                        Spacer(Modifier.height(32.dp))
                        stateTransition.AnimatedVisibility({ it >= 8 }, enter = slideInVertically(), exit = slideOutVertically()) {
                            Text("Just enable it in your gradle.properties!")
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    stateTransition.AnimatedVisibility({ it >= 9 }, enter = slideInVertically(), exit = slideOutVertically()) {
                        Column(
                            modifier = Modifier
                                .border(
                                    color = Color.Black,
                                    width = 1.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text("org.gradle.caching=true".toCode(language = Language.Properties))
                        }
                    }
                }
            }
        }
    }
}
