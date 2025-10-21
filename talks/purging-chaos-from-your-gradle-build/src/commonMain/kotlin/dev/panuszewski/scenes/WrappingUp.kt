package dev.panuszewski.scenes

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.INTELLIJ_DARK_CODE_STYLE
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.WrappingUp() {
    scene(
        stateCount = 8,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withIntTransition {
            SlidingTitleScaffold("Wrapping Up") { contentVisible ->
                contentVisible.FadeInOutAnimatedVisibility {
                    CenteredColumn {
                        RevealSequentially(since = 2) {
                            annotatedStringItem {
                                append("Groovy sucks, migrate to ")
                                appendWithColor(Color(0xFF0690cf), "Kotlin DSL")
                                append("!")
                            }
                            annotatedStringItem {
                                append("Use ")
                                appendWithColor(palette.purple, "version catalogs ")
                                append("for type safety")
                            }
                            annotatedStringItem {
                                append("Extract imperative code to ")
                                appendWithColor(palette.yellow, "convention plugins")
                            }
                            annotatedStringItem {
                                append("Apply the ")
                                appendWithColor(palette.turquoise, "typesafe-conventions")
                                append(" plugin to use ")
                                appendWithColor(INTELLIJ_DARK_CODE_STYLE.property.color, "libs")
                                append(" in your conventions")
                            }
                            annotatedStringItem {
                                append("Avoid ")
                                appendWithColor(palette.red, "cross configuration")
                                append(" (subprojects & allprojects)")
                            }
                            annotatedStringItem {
                                append("Always prefer ")
                                appendWithColor(palette.green, "build-logic")
                                append(" over ")
                                appendWithColor(palette.pink, "buildSrc")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.orange, "Declarative Gradle")
                                append(" is what Gradle needs the most! 🔥")
                            }
                        }
                    }
                }
            }
        }
    }
}