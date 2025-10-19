package dev.panuszewski.scenes

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.appendWithSecondaryColor

fun StoryboardBuilder.WrappingUp() {
    scene(
        stateCount = 7,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withIntTransition(initialState = 6) {
            TitleScaffold("Wrapping Up") {
                CenteredColumn {
                    RevealSequentially {
                        annotatedStringItem {
                            append("Groovy sucks, migrate to ")
                            appendWithColor(Color(0xFF0690cf), "Kotlin DSL!")
                        }
                        annotatedStringItem {
                            append("Use ")
                            append("version catalogs ")
                            append("for type safety")
                        }
                        annotatedStringItem {
                            append("Extract ")
                            append("imperative code")
                            append(" to convention plugins")
                        }
                        annotatedStringItem {
                            append("Avoid ")
                            append("cross configuration")
                            append(" ")
                        }
                        annotatedStringItem {
                            append("Start with buildSrc, switch to build-logic if you see the performance gain")
                        }
                        annotatedStringItem {
                            append("Declarative Gradle")
                            append(" is what Gradle needs the most!")
                        }
                    }
                }
            }
        }
    }
}