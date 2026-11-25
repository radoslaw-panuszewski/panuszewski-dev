package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.CardLine
import dev.panuszewski.components.CardSlot
import dev.panuszewski.components.OptionCard
import dev.panuszewski.components.Requirement.PERFORMANCE
import dev.panuszewski.components.Requirement.PREDICTABLE_STATS
import dev.panuszewski.components.Verdict
import dev.panuszewski.components.appendRequirementShort
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.ContractsOrSeparated() {
    val (
        titleOnly,
        introAppears,
        togetherAppears,
        separatedAppears,
        performanceNoteAppears,
        optInAppears,
        verdictAppears,
        lastState
    ) = subsequentNumbers()

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            val transition = this

            SectionScaffold("Contracts: together with smokes, or separated?") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = introAppears) {
                            annotatedStringItem {
                                appendAsCode("Foo")
                                append(" sometimes runs smokes, sometimes smokes + N contracts, sometimes everything")
                            }
                        }

                        RevealSequentially(since = performanceNoteAppears) {
                            annotatedStringItem {
                                append("But to satisfy ")
                                appendRequirementShort(PERFORMANCE)
                                append(" some configurations ")
                                appendWithColor(palette.orange, "must")
                                append(" keep running contracts with smokes (e.g. Gradle Integration Tests)")
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        CardSlot(transition, since = togetherAppears) {
                            OptionCard("Together with smokes", palette.red, compact = true) {
                                CardLine {
                                    appendAsCode("Foo_FULL")
                                    append(", ")
                                    appendAsCode("Foo_SMOKE")
                                }
                                CardLine {
                                    appendRequirementShort(PREDICTABLE_STATS)
                                    append(" — all tests ✅, smokes ❌")
                                }
                                CardLine { appendRequirementShort(PERFORMANCE, "✅") }
                            }
                        }

                        CardSlot(transition, since = separatedAppears) {
                            OptionCard("Fully separated", palette.red, compact = true) {
                                CardLine {
                                    appendAsCode("Foo_FULL")
                                    append(", ")
                                    appendAsCode("Foo_SMOKE")
                                    append(", ")
                                    appendAsCode("Foo_CONTRACT")
                                }
                                CardLine {
                                    appendRequirementShort(PREDICTABLE_STATS)
                                    append(" — all tests ✅, smokes ✅")
                                }
                                CardLine { appendRequirementShort(PERFORMANCE, "❌") }
                            }
                        }

                        CardSlot(transition, since = optInAppears) {
                            OptionCard("Separated + opt-in merge", palette.green, compact = true) {
                                CardLine {
                                    appendAsCode("Foo_FULL")
                                    append(", ")
                                    appendAsCode("Foo_SMOKE")
                                    append(", ")
                                    appendAsCode("Foo_CONTRACT")
                                }
                                CardLine {
                                    append("or: ")
                                    appendAsCode("Foo_FULL")
                                    append(", ")
                                    appendAsCode("Foo_SMOKE_CONTRACT")
                                }
                                CardLine {
                                    appendRequirementShort(PREDICTABLE_STATS)
                                    append(" — all tests ✅, smokes ✅")
                                }
                                CardLine { appendRequirementShort(PERFORMANCE, "✅") }
                            }
                        }
                    }

                    Verdict(since = verdictAppears) {
                        appendWithColor(palette.green, "Contracts separated by default")
                        append("; opt-in to merge them with smokes")
                    }
                }
            }
        }
    }
}
