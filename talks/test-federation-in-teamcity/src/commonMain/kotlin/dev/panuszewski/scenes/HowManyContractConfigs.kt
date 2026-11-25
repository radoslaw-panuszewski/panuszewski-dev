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
import dev.panuszewski.components.Requirement.PREDICTABLE_STATS
import dev.panuszewski.components.Requirement.TEAMCITY_UX
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

fun StoryboardBuilder.HowManyContractConfigs() {
    val (
        titleOnly,
        introAppears,
        perDomainAppears,
        oneVariantAppears,
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

            SectionScaffold("How many contract configurations?") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = introAppears) {
                            annotatedStringItem {
                                appendAsCode("Foo_CONTRACT")
                                append(" is still ")
                                appendWithColor(palette.red, "dynamic")
                                append(" — it runs a different number of tests depending on changed domains")
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        CardSlot(transition, since = perDomainAppears) {
                            OptionCard("Variant per contracted domain", palette.red, compact = true) {
                                CardLine { appendAsCode("Foo_CONTRACT_FOR_DOMAIN1") }
                                CardLine { appendAsCode("Foo_CONTRACT_FOR_DOMAIN2") }
                                CardLine { appendAsCode("Foo_CONTRACT_FOR_DOMAIN3") }
                                CardLine { appendWithColor(palette.gray, "…and so on") }
                                CardLine {
                                    appendRequirementShort(PREDICTABLE_STATS)
                                    append(" contracts ✅")
                                }
                                CardLine { appendRequirementShort(TEAMCITY_UX, "❌ the variant count explodes") }
                            }
                        }

                        CardSlot(transition, since = oneVariantAppears) {
                            OptionCard("One variant for all contracts", palette.green, compact = true) {
                                CardLine { appendAsCode("Foo_CONTRACT") }
                                CardLine {
                                    appendRequirementShort(PREDICTABLE_STATS)
                                    append(" contracts ❌")
                                }
                                CardLine { appendRequirementShort(TEAMCITY_UX, "✅") }
                            }
                        }
                    }

                    Verdict(since = verdictAppears) {
                        appendWithColor(palette.green, "One variant for all contracts")
                        append(" — we trade contract-level statistics for a UI people can actually navigate")
                    }
                }
            }
        }
    }
}
