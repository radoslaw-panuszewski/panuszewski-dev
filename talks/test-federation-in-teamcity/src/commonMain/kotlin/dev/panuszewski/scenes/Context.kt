package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Verdict
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.Context() {
    val (
        titleOnly,
        introAppears,
        introNote,
        fullMode,
        smokeNoDomains,
        smokeOneDomain,
        smokeTwoDomains,
        smokeNDomains,
        problemAppears,
        goalAppears,
        lastState
    ) = subsequentNumbers()

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            SectionScaffold("Context") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = introAppears, until = problemAppears) {
                            annotatedStringItem {
                                append("With Test Federation, every configuration can run in ")
                                appendWithColor(palette.orange, "N + 2")
                                append(" different ways")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.gray, "where N = number of domains it declares contracts against")
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 8.dp) {
                        RevealSequentially(since = fullMode) {
                            annotatedStringItem { appendAsCode("FULL") }
                            annotatedStringItem { appendAsCode("SMOKE, changedDomains = []") }
                            annotatedStringItem { appendAsCode("SMOKE, changedDomains = [Domain1]") }
                            annotatedStringItem { appendAsCode("SMOKE, changedDomains = [Domain1, Domain2]") }
                            annotatedStringItem { appendAsCode("SMOKE, changedDomains = [Domain1, Domain2, …, DomainN]") }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = problemAppears) {
                            annotatedStringItem {
                                appendWithColor(palette.red, "This breaks TeamCity build statistics")
                                append(" — we compare different execution modes with each other")
                            }
                        }
                    }

                    Verdict(since = goalAppears, icon = "🎯", accent = palette.orange) {
                        append("Goal: Split the configuration into ")
                        appendWithColor(palette.orange, "variants")
                        append(", where each variant always executes the same tests")
                    }
                }
            }
        }
    }
}
