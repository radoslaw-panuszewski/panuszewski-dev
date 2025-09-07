package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.addFile
import dev.panuszewski.template.Stages
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.h4
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withPrimaryColor

fun StoryboardBuilder.Amper() {
    scene(100) {
        val stages = Stages()
        val stateTransition = transition.createChildTransition { it.toState() }
        val title = remember { mutableStateOf("") }

        if (stateTransition.currentState <= stages.lastState) {
            title.value = "Amper"
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stateTransition.Title(title.value)
            stateTransition.BriefDescription(stages, title)
            stateTransition.SpringBoot(stages, title)
        }
    }
}

@Composable
private fun Transition<Int>.Title(title: String) {
    Spacer(Modifier.height(16.dp))
    AnimatedContent(targetState = title) { targetTitle ->
        h4 { Text(targetTitle) }
    }
    Spacer(Modifier.height(32.dp))
}

@Composable
private fun Transition<Int>.BriefDescription(stages: Stages, title: MutableState<String>) {
    val initialState = stages.lastState + 1

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val count = RevealSequentially(since = initialState) {
            item {
                Text {
                    append("An ")
                    withPrimaryColor { append("experimental") }
                    append(" build tool from JetBrains")
                }
            }

            item {
                Text {
                    append("Started as a Gradle plugin, now ")
                    withPrimaryColor { append("standalone") }
                }
            }
            item { Text { append("Focused on UX and toolability") } }
        }

        val states = stages.registerStates(since = initialState, count = count)

        if (currentState in states) {
            title.value = "Amper"
        }
    }
}

@Composable
private fun Transition<Int>.SpringBoot(stages: Stages, title: MutableState<String>) {
    val initialState = stages.lastState
    val ideVisibleSince = initialState + 1

    val moduleYaml = buildAndRememberCodeSamples {
        val springBootVersion by tag()

        $$"""
        product: jvm/app

        settings:
          springBoot:
            enabled: true$${springBootVersion}
            version: 3.5.3$${springBootVersion}

        dependencies:
          - $spring.boot.starter.web
        """
            .trimIndent()
            .toCodeSample(language = Language.Yaml)
            .startWith { hide(springBootVersion) }
            .then { reveal(springBootVersion).focus(springBootVersion) }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SlideFromBottomAnimatedVisibility({ ideVisibleSince <= it }) {
            IDE(
                files = buildList {
                    addFile(
                        name = "module.yaml",
                        content = createChildTransition { moduleYaml.safeGet(it - ideVisibleSince) }
                    )
                },
                selectedFile = "module.yaml",
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
            )
        }
    }

    val states = stages.registerStates(since = initialState, count = moduleYaml.size)

    if (currentState in states) {
        title.value = "Amper + Spring Boot"
    }
}
