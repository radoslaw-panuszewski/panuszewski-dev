package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Requirement.PERFORMANCE
import dev.panuszewski.components.Requirement.PREDICTABLE_STATS
import dev.panuszewski.components.appendRequirementShort
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.splitByChars
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.TestSlices() {

    val files = listOf(
        "TestFederation.kt" to TEST_FEDERATION
    )

    val totalStates = calculateTotalStates(files)

    scene(
        stateCount = totalStates,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            val ideState = buildIdeState(files)

            SectionScaffold("How do we run contracts without smokes?") {
                ideState.IdeLayout {

                    adaptiveTopPanel("blocker") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem {
                                        append("We can't \"run only contracts\" in this model")
                                    }
                                    annotatedStringItem {
                                        append("But even if we could, we wouldn't have ")
                                        appendWithColor(palette.turquoise, "composability")
                                    }
                                }
                            }
                        }
                    }

                    adaptiveTopPanel("slices") {
                        Box(Modifier.padding(bottom = 24.dp)) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                SliceLegendLine("🛡️", "AllTests", "execute all the tests") {
                                    appendWithColor(palette.gray, "previously: Full")
                                }
                                SliceLegendLine("💨", "SmokeTests", "execute only smoke tests") {
                                    appendWithColor(palette.gray, "previously: Smoke && changedDomains.isEmpty")
                                }
                                SliceLegendLine("🤝", "ContractTests", "execute only contract tests") {
                                    appendWithColor(palette.red, "no equivalent in the old world")
                                }
                            }
                        }
                    }

                    adaptiveTopPanel("merged") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem {
                                        append("Or combine both slices into one configuration — this is the opt-in that keeps ")
                                        appendRequirementShort(PERFORMANCE)
                                        append(" satisfied")
                                    }
                                }
                            }
                        }
                    }

                    adaptiveTopPanel("summary") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem {
                                        appendWithColor(palette.green, "Contracts separated by default")
                                        append("; opt-in to merge them with smokes")
                                    }
                                    annotatedStringItem {
                                        appendWithColor(palette.green, "One variant")
                                        append(" for all contracts")
                                    }
                                    annotatedStringItem {
                                        append("Dump contracts at ")
                                        appendWithColor(palette.green, "lifecycleTestTask level")
                                    }
                                    annotatedStringItem {
                                        append("Execution modes are replaced with ")
                                        appendWithColor(palette.green, "test slices")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SliceLegendLine(
    emoji: String,
    name: String,
    description: String,
    note: @Composable AnnotatedString.Builder.() -> Unit,
) {
    Text {
        append("$emoji  ")
        appendAsCode(name)
        append("  —  $description    ")
        note()
    }
}

private val TEST_FEDERATION = buildCodeSamples {
    val oldModel by tag()
    val newModel by tag()
    val oldConfigs by tag()
    val sliceAll by tag()
    val sliceSeparate by tag()
    val sliceMerged by tag()
    val contractCondition by tag()
    val warning by tag()

    """
    ${oldModel}class TestFederationState(
        val changedDomains: List<Domain>,
        val executionModes: Map<Domain, TestFederationMode>
    )${oldModel}${newModel}class TestFederationState(
        val testSlices: Map<Domain, List<TestSlice>>
    )${newModel}
    
    ${oldConfigs}// runs if (executionModes[Wasm] == Full)
    @Full
    @Domains(Wasm)
    object Foo_FULL : BuildType()
    
    // runs if (executionModes[Wasm] == Smoke)
    @Smoke
    @Domains(Wasm)
    object Foo_SMOKE : BuildType()
    
    ${contractCondition}// runs if (executionModes[Wasm] == Smoke && Wasm in changedDomains)
    ${warning}// WARNING: it also runs smoke tests!${warning}${contractCondition}
    @Contract
    @Domains(Wasm)
    object Foo_CONTRACT : BuildType()${oldConfigs}${sliceAll}@AllTests
    @Domains(Wasm)
    object Foo_ALL : BuildType() // "🛡️ Foo"${sliceAll}${sliceSeparate}
    
    @SmokeTests
    @Domains(Wasm)
    object Foo_SMOKE : BuildType() // "💨 Foo"
    
    @ContractTests
    @Domains(Wasm)
    object Foo_CONTRACT : BuildType() // "🤝 Foo"${sliceSeparate}${sliceMerged}
    
    @SmokeTests
    @ContractTests
    @Domains(Wasm)
    object Foo_SMOKE_CONTRACT : BuildType() // "💨🤝 Foo"${sliceMerged}
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin, splitMethod = { it.splitByChars() })
        .startWith { hide(newModel, sliceAll, sliceSeparate, sliceMerged) }
        .openPanel("blocker")
        .pass()
        .closePanel("blocker")
        .then {
            this
                .unfocus()
                .hide(oldModel)
                .reveal(newModel)
        }
        .openPanel("slices")
        .closePanel("slices")
        .then {
            this
                .unfocus()
                .hide(oldConfigs)
                .reveal(sliceAll, sliceSeparate)
        }
        .then {
            this
                .hide(sliceSeparate)
                .reveal(sliceMerged)
        }
        .openPanel("merged")
        .pass()
        .closePanel("merged")
        .then { unfocus() }
        .openPanel("summary")
        .pass(3)
        .closePanel("summary")
}
