package dev.panuszewski.scenes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.RevealedItem
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.sortedMapOf
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.BULLET_1

fun StoryboardBuilder.WhyBother() {

    val ideAppears = 2

    val (
        ideShrinks,
        bulletpointsAppear
    ) =
        subsequentNumbers(since = ideAppears + 1)

    val (
        groovyBulletpoint,
        noTypeSafetyBulletpoint,
        imperativeCodeBulletpoint,
        crossConfigurationBulletpoint,
        mixedConcernsBulletpoint,
        ideBackToNormal
    ) =
        subsequentNumbers(since = bulletpointsAppear)

    scene(stateCount = ideBackToNormal + 1) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()

            SlidingTitleScaffold("Why bother?") {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 32.dp).align(TopStart),
                ) {
                    RevealSequentially(since = bulletpointsAppear) {
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

                val ideStartPadding by animateDp {
                    when {
                        it >= ideBackToNormal -> 0.dp
                        it >= ideShrinks -> 260.dp
                        else -> 0.dp
                    }
                }

                val fileTreeWidth by animateDp {
                    when {
                        it >= ideBackToNormal -> 275.dp
                        it >= ideShrinks -> 0.dp
                        else -> 275.dp
                    }
                }

                IDE_STATE = IdeState(
                    files = buildList {
                        addFile(
                            name = "build.gradle",
                            content = createChildTransition {
                                when (it) {
                                    mixedConcernsBulletpoint -> BUILD_GRADLE_KTS[5]
                                    crossConfigurationBulletpoint -> BUILD_GRADLE_KTS[4]
                                    imperativeCodeBulletpoint -> BUILD_GRADLE_KTS[3]
                                    noTypeSafetyBulletpoint -> BUILD_GRADLE_KTS[2]
                                    groovyBulletpoint -> BUILD_GRADLE_KTS[0]
                                    else -> BUILD_GRADLE_KTS[0]
                                }
                            }
                        )
                    },
                    selectedFile = "build.gradle",
                    fileTreeWidth = fileTreeWidth,
                )

                IDE(
                    IDE_STATE,
                    modifier = Modifier.padding(start = ideStartPadding),
                )
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
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
        implementation $${noTypesafe2}project(':first-library')$${noTypesafe2}
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
        .then { focus(allCode) }
        .then { focus(noTypesafe1, noTypesafe2, noTypesafe3) }
        .then { focus(imperative1, imperative2, imperative3) }
        .then { focus(crossConfig1, crossConfig2) }
        .then { focus(mixedConcerns, scroll = false) }
}