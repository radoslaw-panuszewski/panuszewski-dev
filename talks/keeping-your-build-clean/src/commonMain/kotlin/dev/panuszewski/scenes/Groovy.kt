package dev.panuszewski.scenes

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.Groovy() {
    val ideShrinks = 1
    val bulletpointsAppear = ideShrinks + 1
    val ideBackToNormal = bulletpointsAppear + 3

    scene(100) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()
            val ideTopPadding by animateDp {
                when {
                    it >= ideBackToNormal -> 0.dp
                    it >= ideShrinks -> 260.dp
                    else -> 0.dp
                }
            }

            TitleScaffold("Groovy 🤢") {

                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    RevealSequentially(since = bulletpointsAppear) {
                        stringItem("Dynamic typing")
                        stringItem("Poor IDE support")
                        stringItem("Hard to debug")
                    }
                }

                IDE_STATE = IdeState(
                    files = buildList {
                        addFile(
                            name = "build.gradle",
                            content = createChildTransition { BUILD_GRADLE_KTS.safeGet(it - ideBackToNormal) }
                        )
                    },
                    selectedFile = "build.gradle",
                )

                IDE(
                    IDE_STATE,
                    modifier = Modifier.padding(top = ideTopPadding),
                )
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val allCode by tag()

    $$"""
    $${allCode}buildscript {
        classpath('org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20')
    }

    allprojects {
        apply plugin: 'kotlin'
    }

    subprojects
        .findAll { it.name.endsWith('-library') }
        .forEach { it.apply plugin: 'java-library' }

    dependencies {
        implementation project(':first-library')
    }

    tasks.register('sayHello') {
        doLast {
            println 'lol'
        }
    }$${allCode}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
        .then { underline(allCode) }
}
