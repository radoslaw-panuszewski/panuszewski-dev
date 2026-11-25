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
import dev.panuszewski.components.Design.ANNOTATIONS
import dev.panuszewski.components.Design.VIRTUAL_CONFIGS
import dev.panuszewski.components.OptionCard
import dev.panuszewski.components.appendDesign
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.Comparison() {
    val (
        titleOnly,
        pluginAppears,
        dbcAppears,
        skipTagsAppears,
        observationAppears,
        lastState
    ) = subsequentNumbers()

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            val transition = this

            SectionScaffold("Three ways to get there") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        CardSlot(transition, since = pluginAppears) {
                            OptionCard("(4.1) TeamCity plugin", palette.purple, compact = true) {
                                CardLine { appendWithColor(palette.gray, "freeze time") }
                                CardLine { appendWithColor(palette.green, "🟢 full flexibility") }
                                CardLine { appendWithColor(palette.red, "🔴🔴 custom plugin install") }
                                CardLine { appendDesign(VIRTUAL_CONFIGS, "✅") }
                                CardLine { appendDesign(ANNOTATIONS, "✅") }
                            }
                        }

                        CardSlot(transition, since = dbcAppears) {
                            OptionCard("(4.2) Dynamic Build Chains", palette.blue, compact = true) {
                                CardLine { appendWithColor(palette.gray, "freeze time") }
                                CardLine { appendWithColor(palette.green, "🟢 built-in, targets our use-case") }
                                CardLine { appendWithColor(palette.red, "🔴 two generation paths") }
                                CardLine { appendWithColor(palette.red, "🔴 noisy generator builds, experimental") }
                                CardLine { appendDesign(VIRTUAL_CONFIGS, "❌") }
                                CardLine { appendDesign(ANNOTATIONS, "✅") }
                            }
                        }

                        CardSlot(transition, since = skipTagsAppears) {
                            OptionCard("(4.3) Skip tags", palette.green, compact = true) {
                                CardLine { appendWithColor(palette.orange, "trigger time") }
                                CardLine { appendWithColor(palette.green, "🟢 built-in, already in use") }
                                CardLine { appendWithColor(palette.green, "🟢 very simple, only filters deps") }
                                CardLine { appendWithColor(palette.yellow, "🔴🟢 needs a triggering build") }
                                CardLine { appendDesign(VIRTUAL_CONFIGS, "❌") }
                                CardLine { appendDesign(ANNOTATIONS, "✅") }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = observationAppears) {
                            annotatedStringItem {
                                append("Every mechanism supports ")
                                appendDesign(ANNOTATIONS)
                                append(" — only the plugin fully supports ")
                                appendDesign(VIRTUAL_CONFIGS)
                            }
                        }
                    }
                }
            }
        }
    }
}
