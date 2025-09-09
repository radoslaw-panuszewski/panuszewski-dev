package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.template.NICE_BLUE
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.appendWithColor
import dev.panuszewski.template.appendWithPrimaryColor
import dev.panuszewski.template.appendWithSecondaryColor
import dev.panuszewski.template.appendWithSecondaryVariantColor
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.WrappingUp() {
    scene(
        stateCount = 7,
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withStateTransition {
            TitleScaffold("Wrapping up!") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RevealSequentially {
                        textItem {
                            append("Maven 4 will support ")
                            appendWithPrimaryColor("alternative POM syntaxes")
                        }
                        textItem {
                            append("Don't wait and enable ")
                            appendWithSecondaryVariantColor("configuration cache")
                            append(" in your Gradle build!")
                        }
                        textItem {
                            appendWithSecondaryColor("Declarative Gradle")
                            append(" will replace Kotlin with DCL - a tiny subset of Kotlin")
                        }
                        textItem {
                            append("Play around with ")
                            appendWithColor(NICE_BLUE, "Amper")
                            append(" and see how simple your build can be 😉")
                        }
                        textItem {
                            append("The overall direction is towards improving toolability 🛠️")
                        }
                        textItem {
                            append("Remember that most this stuff is experimental! 🧪")
                        }
                    }
                }
            }
        }
    }
}
