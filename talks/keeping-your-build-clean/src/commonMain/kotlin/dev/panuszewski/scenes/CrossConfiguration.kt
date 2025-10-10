package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withStateTransition {

            val ideState = buildIdeState(
                files = files,
                initialTitle = "Cross configuration"
            )

            TitleScaffold(ideState.currentState.title) {
                ideState.IdeLayout {

                }
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    """
    plugins {
        `wtf-app`
    }
    
    dependencies {
        implementation(projects.subProject)
        implementation(libs.spring.boot.web)
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}