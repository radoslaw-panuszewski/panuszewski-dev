package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Requirement.BREADCRUMBS
import dev.panuszewski.components.Requirement.MENTAL_MODEL
import dev.panuszewski.components.Requirement.TEAMCITY_UX
import dev.panuszewski.components.appendRequirement
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.AnnotationsAndAggregate() {

    val files = listOf(
        "Aggregate.kt" to AGGREGATE
    )

    val totalStates = calculateTotalStates(files)

    scene(
        stateCount = totalStates,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            val ideState = buildIdeState(files)

            SectionScaffold("(3.2) Inject real configurations + annotations") {
                ideState.IdeLayout {

                    adaptiveTopPanel("scorecard") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem { appendRequirement(BREADCRUMBS, "✅") }
                                    annotatedStringItem { appendRequirement(MENTAL_MODEL, "⭐️⭐️⭐️") }
                                    annotatedStringItem { appendRequirement(TEAMCITY_UX, "⭐️⭐️") }
                                }
                            }
                        }
                    }

                    adaptiveTopPanel("demo") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem {
                                        appendWithColor(palette.green, "Variants are real configurations")
                                        append(" — they appear as snapshot dependencies of the Aggregate")
                                    }
                                    annotatedStringItem {
                                        append("So you can navigate ")
                                        appendWithColor(palette.blue, "both ways")
                                        append(" between the chain and its variants")
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

private val AGGREGATE = buildCodeSamples {
    val sealedSnapshot by tag()
    val concreteSnapshot by tag()
    val baseClass by tag()
    val allTests by tag()
    val smokeTests by tag()
    val contractTests by tag()
    val additional by tag()

    """
    object Aggregate : BuildType() {
        init {
            dependencies {
                ${sealedSnapshot}// adds all subclasses of Foo as snapshot dependencies
                // (requires Foo to be a sealed class)
                snapshot<Foo>()${sealedSnapshot}
    
                ${concreteSnapshot}// it's also possible to add a concrete configuration
                // (it will be skipped according to its annotations)
                snapshot(AdditionalSmokeCheck) {}${concreteSnapshot}
            }
        }
    }
    
    ${baseClass}// optional base class, convenient to share config between variants
    // the domain assignment is inherited by subclasses
    @Domains(Wasm)
    sealed class Foo${baseClass}
    
    ${allTests}@AllTests
    object Foo_ALL : Foo() {
        init {
            requirements {
                highMemoryAgentRequirement()
            }
        }
    }${allTests}
    
    ${smokeTests}@SmokeTests
    object Foo_SMOKE : Foo()${smokeTests}
    
    ${contractTests}@ContractTests
    object Foo_CONTRACT : Foo()${contractTests}
    
    ${additional}@SmokeTests
    @Domains(Wasm)
    object AdditionalSmokeCheck : BuildType()${additional}
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
        .then { focus(sealedSnapshot) }
        .then { focus(baseClass) }
        .then { focus(allTests) }
        .then { focus(smokeTests, contractTests) }
        .then { focus(concreteSnapshot, additional) }
        .then { unfocus() }
        .openPanel("scorecard")
        .pass(2)
        .closePanel("scorecard")
        .openPanel("demo")
        .pass()
        .closePanel("demo")
}
