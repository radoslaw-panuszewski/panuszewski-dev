package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.Agenda
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.Groovy() {

    val files = listOf(
        "build.gradle" to BUILD_GRADLE_KTS
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()

            val ideState = buildIdeState(initialTitle = "Groovy", files = files)

            val title = ideState.currentState.title

            TitleScaffold(title) {
                ideState.IdeLayout {
                    topPanel("groovy") { panelState ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            panelState.RevealSequentially {
                                stringItem("Dynamic typing")
                                stringItem("Poor IDE support")
                                stringItem("Hard to debug")
                            }
                        }
                    }

                    leftPanel("agenda") { panelState ->
                        panelState.Agenda {
                            item("Groovy", crossedOutSince = 1)
                            item("No type safety")
                            item("Imperative code")
                            item("Cross configuration")
                            item("Mixed concerns")
                        }
                    }
                }
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val nothing by tag()
    val groovy by tag()
    val kotlin by tag()

    $$"""
    $${nothing}$${nothing}$${groovy}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath('org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20')
        }
    }
    
    apply plugin: 'org.jetbrains.kotlin.jvm'

    dependencies {
        implementation project(':sub-project')
        implementation 'org.springframework.boot:spring-boot-starter-web:3.5.6'
    }$${groovy}$${kotlin}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
        }
    }
    
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        "implementation"(project(":sub-project"))
        "implementation"("org.springframework.boot:spring-boot-starter-web:3.5.6")
    }$${kotlin}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { hide(kotlin) }
        .openPanel("groovy")
        .pass(3)
        .closePanel("groovy")
        .then { underline(groovy).focus(nothing).showEmoji("🤢") }
        .then {
            this
                .unfocus()
                .hide(groovy)
                .reveal(kotlin)
                .changeLanguage(Language.KotlinDsl)
                .hideEmoji()
                .changeTitle("Kotlin DSL ❤️")
                .renameSelectedFile("build.gradle.kts")
        }
        .openAgenda()
        .pass()
        .closeAgenda()
}