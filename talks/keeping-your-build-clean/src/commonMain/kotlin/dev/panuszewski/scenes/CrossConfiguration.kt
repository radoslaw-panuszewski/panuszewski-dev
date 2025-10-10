package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "first-library" to DIRECTORY.initiallyHidden(),
        "first-library/build.gradle.kts" to FIRST_LIBRARY_BUILD_GRADLE_KTS.initiallyHidden(),
        "second-library" to DIRECTORY.initiallyHidden(),
        "second-library/build.gradle.kts" to SECOND_LIBRARY_BUILD_GRADLE_KTS.initiallyHidden(),
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
    val subproject by tag()
    val springBoot by tag()
    val library1 by tag()
    val library2 by tag()

    """
    plugins {
        `wtf-app`
    }
    
    dependencies {${subproject}
        implementation(projects.subProject)${subproject}${springBoot}
        implementation(libs.spring.boot.web)${springBoot}${library1}
        implementation(projects.firstLibrary)${library1}${library2}
        implementation(projects.secondLibrary)${library2}
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(library1, library2) }
        .then { hide(springBoot) }
        .then { hide(subproject).reveal(library1).reveal(library1).revealFile("first-library/build.gradle.kts")}
        .then { reveal(library2).revealFile("second-library/build.gradle.kts") }
        .switchTo("first-library/build.gradle.kts")
}

private val FIRST_LIBRARY_BUILD_GRADLE_KTS = buildCodeSamples {
    """
        
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
        .openInRightPane("second-library/build.gradle.kts")
}

private val SECOND_LIBRARY_BUILD_GRADLE_KTS = buildCodeSamples {
    """
        
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}