package dev.panuszewski.scenes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
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
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.sortedMapOf
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers5
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.BULLET_1

fun StoryboardBuilder.WhyBother() {

    val ideAppears = 2
    val fileTreeHiddenSince = ideAppears + 1
    val ideShrinksToShowGroovy = fileTreeHiddenSince
    val bulletpointsAppear = ideShrinksToShowGroovy + 1

    val (
        imperativeCodeBulletpoint,
        groovyBulletpoint,
        crossConfigurationBulletpoint,
        mixedConcernsBulletpoint,
        noTypeSafetyBulletpoint,
    ) =
        subsequentNumbers5(since = bulletpointsAppear)

    scene(stateCount = 100) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()

            SlidingTitleScaffold("Why bother?") {
                val files = buildList {
                    addFile(
                        name = "build.gradle",
                        content = createChildTransition {
                            when (it) {
                                mixedConcernsBulletpoint -> BUILD_GRADLE_KTS[5]
                                crossConfigurationBulletpoint -> BUILD_GRADLE_KTS[4]
                                imperativeCodeBulletpoint -> BUILD_GRADLE_KTS[3]
                                noTypeSafetyBulletpoint -> BUILD_GRADLE_KTS[2]
                                groovyBulletpoint -> BUILD_GRADLE_KTS[1]
                                else -> BUILD_GRADLE_KTS[0]
                            }
                        }
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 32.dp).align(TopStart),
                ) {
                    RevealSequentially(since = bulletpointsAppear) {
                        val bulletpoints = sortedMapOf<Int, ComposableLambda>(
                            noTypeSafetyBulletpoint to { Text("$BULLET_1 No type safety") },
                            imperativeCodeBulletpoint to { Text("$BULLET_1 Imperative code") },
                            crossConfigurationBulletpoint to { Text("$BULLET_1 Cross configuration") },
                            mixedConcernsBulletpoint to { Text("$BULLET_1 Mixed concerns") },
                            groovyBulletpoint to { Text("$BULLET_1 Groovy 🤢") },
                        )
                        bulletpoints.forEach { item(content = it.value) }
                    }
                }

                val ideStartPadding by animateDp {
                    when {
                        it >= ideShrinksToShowGroovy -> 260.dp
                        else -> 0.dp
                    }
                }

                val fileTreeWidth by animateDp {
                    val value = when {
                        it >= fileTreeHiddenSince -> 0.dp
                        else -> 275.dp
                    }
                    value
                }

                val fileTreeHidden = currentState >= fileTreeHiddenSince

                IDE(
                    IdeState(
                        files = files,
                        selectedFile = "build.gradle",
                        fileTreeHidden = fileTreeHidden,
                    ),
                    modifier = Modifier.padding(start = ideStartPadding),
                    fileTreeWidth = fileTreeWidth
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
    val bottom by tag()

    """
    ${allCode}buildscript {
        classpath(${noTypesafe1}'org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20'${noTypesafe1})
    }
    
    ${crossConfig1}allprojects${crossConfig1} {
        ${imperative1}apply plugin: 'kotlin'${imperative1}
    }
        
    ${crossConfig2}subprojects${crossConfig2}
        ${imperative2}.findAll { it.name.endsWith('-library') }
        .forEach { it.apply plugin: 'java-library' }${imperative2}
    
    ${mixedConcerns}dependencies {
        implementation ${noTypesafe2}project(':first-library')${noTypesafe2}
    }
    
    tasks.register('sayHello') {
        doLast {
            ${imperative3}println 'lol'${imperative3}
        }
    }${mixedConcerns}${allCode}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
        .then { underline(allCode) }
        .then { resetUnderline(allCode).focus(noTypesafe1, noTypesafe2, noTypesafe3) }
        .then { focus(imperative1, imperative2, imperative3) }
        .then { focus(crossConfig1, crossConfig2) }
        .then { focus(mixedConcerns, scroll = false) }
}