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
import dev.panuszewski.template.theme.INTELLIJ_DARK_CODE_STYLE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.theme.NICE_LIGHT_TURQUOISE
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.NICE_PINK
import dev.panuszewski.template.theme.NICE_LIGHT_PURPLE
import dev.panuszewski.template.theme.NICE_LIGHT_RED
import dev.panuszewski.template.theme.NICE_YELLOW
import dev.panuszewski.template.theme.appendWithColor

fun StoryboardBuilder.WrappingUp() {
    scene(
        stateCount = 8,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withIntTransition {
            TitleScaffold("Wrapping Up") {
                CenteredColumn {
                    RevealSequentially {
                        annotatedStringItem {
                            append("Groovy sucks, migrate to ")
                            appendWithColor(Color(0xFF0690cf), "Kotlin DSL")
                            append("!")
                        }
                        annotatedStringItem {
                            append("Use ")
                            appendWithColor(NICE_LIGHT_PURPLE, "version catalogs ")
                            append("for type safety")
                        }
                        annotatedStringItem {
                            append("Extract imperative code to ")
                            appendWithColor(NICE_YELLOW, "convention plugins")
                        }
                        annotatedStringItem {
                            append("Apply the ")
                            appendWithColor(NICE_LIGHT_TURQUOISE, "typesafe-conventions")
                            append(" plugin to use ")
                            appendWithColor(INTELLIJ_DARK_CODE_STYLE.property.color, "libs")
                            append(" in your conventions")
                        }
                        annotatedStringItem {
                            append("Avoid ")
                            appendWithColor(NICE_LIGHT_RED, "cross configuration")
                            append(" (subprojects & allprojects)")
                        }
                        annotatedStringItem {
                            append("Always prefer ")
                            appendWithColor(NICE_GREEN, "build-logic")
                            append(" over ")
                            appendWithColor(NICE_PINK, "buildSrc")
                        }
                        annotatedStringItem {
                            appendWithColor(NICE_ORANGE, "Declarative Gradle")
                            append(" is what Gradle needs the most! 🔥")
                        }
                    }
                }
            }
        }
    }
}