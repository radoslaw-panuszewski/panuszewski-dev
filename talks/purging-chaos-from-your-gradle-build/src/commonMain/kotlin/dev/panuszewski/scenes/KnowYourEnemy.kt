package dev.panuszewski.scenes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.AdvanceDirection.Forward
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.BULLET_1
import dev.panuszewski.template.theme.NICE_ORANGE

fun StoryboardBuilder.KnowYourEnemy() {

    val files = listOf(
        "build.gradle" to BUILD_GRADLE
    )

    val totalStates = calculateTotalStates(files)

    scene(
        stateCount = totalStates,
        enterTransition = { direction -> if (direction == Forward) SceneEnter(CenterEnd).invoke(direction) else EnterTransition.None },
        exitTransition = { direction -> if (direction == Forward) ExitTransition.None else SceneExit(CenterEnd).invoke(direction) },
    ) {
        withIntTransition {
            val ideState = buildIdeState(files)

            SlidingTitleScaffold("Know your enemy") {
                SlideFromBottomAnimatedVisibility({ it >= 1 }) {
                    ideState.IdeLayout {
                        leftPanel("agenda") { panelState ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(top = 32.dp).align(TopStart),
                            ) {
                                panelState.RevealSequentially(highlightedTextStyle = TextStyle(color = NICE_ORANGE)) {
                                    item { Text("$BULLET_1 Groovy 🤢") }
                                    item { Text("$BULLET_1 No type safety") }
                                    item { Text("$BULLET_1 Imperative code") }
                                    item { Text("$BULLET_1 Cross configuration") }
                                    item { Text("$BULLET_1 Mixed concerns") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val BUILD_GRADLE = buildCodeSamples {
    val allCode by tag()
    val noTypesafe1 by tag()
    val noTypesafe2 by tag()
    val noTypesafe3 by tag()
    val noTypesafe4 by tag()
    val noTypesafe5 by tag()
    val imperative1 by tag()
    val imperative2 by tag()
    val imperative3 by tag()
    val crossConfig1 by tag()
    val crossConfig2 by tag()
    val mixedConcerns by tag()

    $$"""    
    $${allCode}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath($${noTypesafe1}'org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20'$${noTypesafe1})
        }
    }

    $${imperative1}apply plugin: $${noTypesafe4}'org.jetbrains.kotlin.jvm'$${noTypesafe4}$${imperative1}

    $${crossConfig1}subprojects {$${crossConfig1}
        $${imperative2}apply plugin: $${noTypesafe5}'java-library'$${noTypesafe5}$${imperative2}
    $${crossConfig2}}$${crossConfig2}

    dependencies {
        implementation project($${noTypesafe2}':sub-project')$${noTypesafe2}
        implementation $${noTypesafe3}'org.springframework.boot:spring-boot-starter-web:3.5.6'$${noTypesafe3}    
    }
    
    $${mixedConcerns}kotlin {
        jvmToolchain(25)
    }
    
    tasks.register('customTask') {
        doLast {
            println 'lol'
        }
    }$${mixedConcerns}$${allCode}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
        .pass(2)
        .openAgenda()
//        .then { focus(allCode) }
        .pass()
        .then { focus(noTypesafe1, noTypesafe2, noTypesafe3, noTypesafe4, noTypesafe5, unfocusedStyle = null) }
        .then { focus(imperative1, imperative2, imperative3, unfocusedStyle = null) }
        .then { focus(crossConfig1, crossConfig2, unfocusedStyle = null) }
        .then { focus(mixedConcerns, unfocusedStyle = null) }
        .then { unfocus() }
        .closeAgenda()
}