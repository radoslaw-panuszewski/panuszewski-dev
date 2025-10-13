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
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.AdvanceDirection.Forward
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.RevealedItem
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.sortedMapOf
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.BULLET_1

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
                ideState.IdeLayout {
                    leftPanel("agenda") { panelState ->
                        val (
                            groovyBulletpoint,
                            noTypeSafetyBulletpoint,
                            imperativeCodeBulletpoint,
                            crossConfigurationBulletpoint,
                            mixedConcernsBulletpoint,
                        ) =
                            subsequentNumbers()

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 32.dp).align(TopStart),
                        ) {
                            panelState.RevealSequentially {
                                val bulletpoints = sortedMapOf(
                                    noTypeSafetyBulletpoint to RevealedItem { Text("$BULLET_1 No type safety") },
                                    imperativeCodeBulletpoint to RevealedItem { Text("$BULLET_1 Imperative code") },
                                    crossConfigurationBulletpoint to RevealedItem { Text("$BULLET_1 Cross configuration") },
                                    mixedConcernsBulletpoint to RevealedItem { Text("$BULLET_1 Mixed concerns") },
                                    groovyBulletpoint to RevealedItem { Text("$BULLET_1 Groovy 🤢") },
                                )
                                bulletpoints.forEach { item(it.value) }
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
    val imperative1 by tag()
    val imperative2 by tag()
    val imperative3 by tag()
    val crossConfig1 by tag()
    val crossConfig2 by tag()
    val mixedConcerns by tag()

    $$"""
    $${allCode}buildscript {
        classpath($${noTypesafe1}'org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20'$${noTypesafe1})
    }

    $${crossConfig1}allprojects$${crossConfig1} {
        $${imperative1}apply plugin: 'kotlin'$${imperative1}
    }

    $${crossConfig2}subprojects$${crossConfig2}
        $${imperative2}.findAll { it.name.endsWith('-library') }
        .forEach { it.apply plugin: 'java-library' }$${imperative2}

    $${mixedConcerns}dependencies {
        implementation $${noTypesafe2}project(':sub-project')$${noTypesafe2}
    }

    tasks.register('sayHello') {
        doLast {
            $${imperative3}println 'lol'$${imperative3}
        }
    }$${mixedConcerns}$${allCode}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy, splitMethod = { listOf(it) })
        .startWith { this }
        .pass(2)
        .openAgenda()
        .then { focus(allCode) }
        .then { focus(noTypesafe1, noTypesafe2, noTypesafe3) }
        .then { focus(imperative1, imperative2, imperative3) }
        .then { focus(crossConfig1, crossConfig2) }
        .then { focus(mixedConcerns, scroll = false) }
        .then { unfocus().closeAgenda() }

}