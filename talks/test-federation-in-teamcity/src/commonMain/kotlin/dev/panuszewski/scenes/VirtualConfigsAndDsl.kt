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

fun StoryboardBuilder.VirtualConfigsAndDsl() {

    val files = listOf(
        "KotlinDev.kt" to KOTLIN_DEV
    )

    val totalStates = calculateTotalStates(files)

    scene(
        stateCount = totalStates,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            val ideState = buildIdeState(files)

            SectionScaffold("(3.1) Generate virtual configurations + DSL") {
                ideState.IdeLayout {

                    adaptiveTopPanel("scorecard") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem { appendRequirement(BREADCRUMBS, "❌") }
                                    annotatedStringItem { appendRequirement(MENTAL_MODEL, "⭐️⭐️") }
                                    annotatedStringItem { appendRequirement(TEAMCITY_UX, "⭐️⭐️⭐️") }
                                }
                            }
                        }
                    }

                    adaptiveTopPanel("demo") { panelState ->
                        Box(Modifier.padding(bottom = 24.dp)) {
                            CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                                panelState.RevealSequentially(since = 0) {
                                    annotatedStringItem {
                                        appendWithColor(palette.orange, "Variants live in an auto-generated subproject")
                                        append(" — they never show up as dependencies of the Aggregate")
                                    }
                                    annotatedStringItem {
                                        append("Great to browse, but you ")
                                        appendWithColor(palette.red, "cannot walk back")
                                        append(" from a variant to its definition in Kotlin DSL")
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

private val KOTLIN_DEV = buildCodeSamples {
    val federated by tag()
    val aggregate by tag()
    val steps by tag()
    val federation by tag()
    val full by tag()
    val smoke by tag()
    val contract by tag()

    """
    object Kotlin_Dev : Project() {
        init {
            buildType(Aggregate)
            ${federated}federatedBuildType(DemoTests)${federated}
        }
    }
    
    ${aggregate}object Aggregate : BuildType() {
        init {
            dependencies {
                snapshot(DemoTests)
            }
        }
    }${aggregate}
    
    object DemoTests : FederatedBuildType() {
        init {
            ${steps}steps {
                gradle { tasks = "test" }
            }${steps}
    
            ${federation}testFederation(Domain.AnalysisApi) {
                ${full}full {
                    requirements {
                        highMemoryAgentRequirement()
                    }
                }${full}
                ${smoke}smoke {
                    // ...
                }${smoke}
                ${contract}contractFor(Domain.Native) {
                    // ...
                }${contract}
            }${federation}
        }
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
        .then { focus(federated) }
        .then { focus(aggregate) }
        .then { focus(steps) }
        .then { focus(federation) }
        .then { focus(full) }
        .then { focus(smoke) }
        .then { focus(contract) }
        .then { unfocus() }
        .openPanel("scorecard")
        .pass(2)
        .closePanel("scorecard")
        .openPanel("demo")
        .pass()
        .closePanel("demo")
}
