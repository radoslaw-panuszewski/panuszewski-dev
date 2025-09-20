package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.appendWithPrimaryColor
import dev.panuszewski.template.theme.appendWithSecondaryColor
import dev.panuszewski.template.theme.appendWithSecondaryVariantColor
import dev.panuszewski.template.extensions.withStateTransition

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
                            appendWithSecondaryVariantColor("Configuration cache")
                            append(" can save you a lot of time!")
                        }
                        textItem {
                            appendWithSecondaryColor("Declarative Gradle")
                            append(" is what Gradle needs the most")
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
