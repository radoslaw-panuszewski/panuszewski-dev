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
import dev.panuszewski.components.Requirement.RESOURCE_USAGE
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

fun StoryboardBuilder.DumpingContracts() {
    val (
        titleOnly,
        introAppears,
        domainLevelAppears,
        taskLevelAppears,
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

            SectionScaffold("Which configuration covers which contracts?") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = introAppears) {
                            annotatedStringItem {
                                append("We only want to create a ")
                                appendAsCode("_CONTRACT")
                                append(" variant for configurations that ")
                                appendWithColor(palette.orange, "actually handle contracts")
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        CardSlot(transition, since = domainLevelAppears) {
                            OptionCard("Dump at domain level", palette.red, compact = true) {
                                CardLine { appendAsCode("domain-contracts.dump.txt") }
                                CardLine { append("contracts between domains") }
                                CardLine {
                                    appendWithColor(palette.red, "not precise")
                                    append(" — creates redundant configurations that execute no tests")
                                }
                                CardLine { appendRequirementShort(RESOURCE_USAGE, "❌") }
                            }
                        }

                        CardSlot(transition, since = taskLevelAppears) {
                            OptionCard("Dump at lifecycleTestTask level", palette.green, compact = true) {
                                CardLine { appendAsCode("testLifecycleTask.dump.txt") }
                                CardLine { append("which test task handles which contracts") }
                                CardLine {
                                    appendWithColor(palette.green, "100% accurate")
                                    append(" — we know exactly which configuration needs a contract variant")
                                }
                                CardLine { appendRequirementShort(RESOURCE_USAGE, "✅") }
                            }
                        }
                    }

                    Verdict(since = verdictAppears) {
                        append("Dump contracts at ")
                        appendWithColor(palette.green, "lifecycleTestTask level")
                    }
                }
            }
        }
    }
}
