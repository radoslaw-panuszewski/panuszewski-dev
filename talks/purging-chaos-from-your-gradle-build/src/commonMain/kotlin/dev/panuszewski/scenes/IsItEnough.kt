package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.NICE_LIGHT_TURQUOISE
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.IsItEnough() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "buildSrc" to DIRECTORY,
        "buildSrc/src/main/kotlin" to DIRECTORY,
        "buildSrc/src/main/kotlin/app-convention.gradle.kts" to APP_CONVENTION_GRADLE_KTS,
        "buildSrc/src/main/kotlin/library-convention.gradle.kts" to LIBRARY_CONVENTION_GRADLE_KTS,
    )

    val totalStates = calculateTotalStates(files) + 1

    scene(totalStates) {
        transition.FadeOutAnimatedVisibility({ it is Frame.State<*> }) {

            withIntTransition {

                val ideState = buildIdeState(
                    files = files,
                    initialTitle = "Is it enough?"
                )

                TitleScaffold(ideState.currentState.title) {
                    SlideFromBottomAnimatedVisibility({ it in 1 until totalStates - 1}) {
                        ideState.IdeLayout {
                            adaptiveTopPanel("bulletpoints") { panelState ->
                                val panelHeight = when {
                                    panelState.currentState >= 5 -> 290.dp
                                    panelState.currentState >= 2 -> 110.dp
                                    else -> 290.dp
                                }

                                Column(
                                    modifier = Modifier.height(panelHeight),
                                    verticalArrangement = Arrangement.spacedBy(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    panelState.RevealSequentially {
                                        annotatedStringItem {
                                            append("Conventions are ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("cool") }
                                            append(" but... are they enough?")
                                        }

                                        annotatedStringItem {
                                            append("They encourages declarativity, but ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("do not enforce it") }
                                        }
                                    }

                                    panelState.RevealSequentially(since = 7) {
                                        annotatedStringItem {
                                            append("Also, conventions often ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("mix") }
                                            append(" build logic with shared defaults")
                                        }
                                    }
                                }
                            }
                        }

                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            FadeInOutAnimatedVisibility({ it == 7 }) {
                                ProvideTextStyle(MaterialTheme.typography.h5) {
                                    Text(
                                        text = """¯\_(ツ)_/¯""",
                                        modifier = Modifier.padding(top = 100.dp, end = 230.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val someImperativeCode by tag()

    """
    plugins {
        `app-convention`
    }
    
    ${someImperativeCode}for (project in subprojects) {
        apply(plugin = "kotlin")
    }
    
    ${someImperativeCode}dependencies {
        implementation(projects.subProject)
        implementation(libs.spring.boot.web)
    }    
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(someImperativeCode) }
        .pass()
        .openPanel("bulletpoints")
        .pass(3)
        .then { revealAndFocus(someImperativeCode) }
        .pass()
        .then { unfocus() }
        .pass()
}

private val APP_CONVENTION_GRADLE_KTS = buildCodeSamples {
    """
        
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}

private val LIBRARY_CONVENTION_GRADLE_KTS = buildCodeSamples {
    """
        
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}