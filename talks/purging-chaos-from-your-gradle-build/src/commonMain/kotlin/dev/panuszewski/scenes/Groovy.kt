package dev.panuszewski.scenes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.Agenda
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.palette
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.groovy_search_dark
import talks.purging_chaos_from_your_gradle_build.generated.resources.groovy_search_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.kotlin_search_dark
import talks.purging_chaos_from_your_gradle_build.generated.resources.kotlin_search_light

fun StoryboardBuilder.Groovy() {

    val files = listOf(
        "build.gradle" to BUILD_GRADLE_KTS
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withIntTransition {
            val ideState = buildIdeState(initialTitle = "Groovy", files = files)

            val title = ideState.currentState.title

            TitleScaffold(title) {
                ideState.IdeLayout {

                    adaptiveTopPanel("groovy") { panelState ->
                        val (
                            bulletpointsDisappear,
                            screenshotAppears
                        ) = subsequentNumbers(since = 4)

                        Box(Modifier.padding(bottom = 32.dp)) {
                            panelState.FadeOutAnimatedVisibility({ it < bulletpointsDisappear }) {
                                CenteredColumn(Modifier.fillMaxWidth().height(200.dp)) {
                                    panelState.RevealSequentially {
                                        stringItem("❌ Dynamic typing")
                                        stringItem("❌ Poor IDE support")
                                        stringItem("❌ Hard to debug (buildscript's classpath not visible in IDE)")
                                    }
                                }
                            }

                            panelState.SlideFromBottomAnimatedVisibility({ it == screenshotAppears }) {
                                ResourceImage(
                                    resource = if (palette.darkMode) Res.drawable.groovy_search_dark else Res.drawable.groovy_search_light,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(if (palette.darkMode) 0.dp else 1.dp, color = MaterialTheme.colors.onBackground, RoundedCornerShape(8.dp))
                                        .width(550.dp)
                                )
                            }
                        }
                    }

                    adaptiveTopPanel("kotlin") {
                        Box(Modifier.padding(bottom = 32.dp)) {
                            ResourceImage(
                                resource = if (palette.darkMode) Res.drawable.kotlin_search_dark else Res.drawable.kotlin_search_light,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(if (palette.darkMode) 0.dp else 1.dp, color = MaterialTheme.colors.onBackground, RoundedCornerShape(8.dp))
                                    .width(550.dp)
                            )
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
    val subprojects by tag()

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
    
    subprojects {
        apply plugin: 'java-library'
    }

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
    
    $${subprojects}subprojects {
        apply(plugin = "java-library")
    }

    $${subprojects}dependencies {
        "implementation"(project(":sub-project"))
        "implementation"("org.springframework.boot:spring-boot-starter-web:3.5.6")
    }$${kotlin}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { hide(kotlin) }
        .openPanel("groovy")
        .pass(5)
        .closePanel("groovy")
        .then { focus(groovy, focusedStyle = { SpanStyle(color = palette.gray, background = palette.warningHighlight) }) }
        .showEmoji("🤢")
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
        .openPanel("kotlin")
        .closePanel("kotlin")
        .openAgenda()
        .pass()
        .closeAgenda()
        .then { focusNoScroll(subprojects) }
        .then { hide(subprojects).unfocus() }
}