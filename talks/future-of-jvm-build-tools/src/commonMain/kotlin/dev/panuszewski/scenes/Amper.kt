package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.Terminal
import dev.panuszewski.components.addFile
import dev.panuszewski.template.Stages
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromLeftAnimatedVisibility
import dev.panuszewski.template.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.h4
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withPrimaryColor
import kotlin.math.max

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

        val states = stages.registerStatesByCount(start = initialState, count = count)

        if (currentState in states) {
            title.value = "Amper"
        }
    }
}

@Composable
private fun Transition<Int>.SpringBoot(stages: Stages, title: MutableState<String>) {
    val moduleYaml = buildAndRememberCodeSamples {
        val normal by tag()
        val normalSpringBootVersion by tag()
        val afterShowSettings by tag()
        val springBootVersionFromShowSettings by tag()
        val springBootEnabledFromShowSettings by tag()

        $$"""
        $${normal}product: jvm/app

        settings:
          springBoot:
            enabled: true$${normalSpringBootVersion}
            version: 3.5.3$${normalSpringBootVersion}

        dependencies:
          - $spring.boot.starter.web$${normal}$${afterShowSettings}compose:
          enabled: false  # [default]
          experimental:
            hotReload:
              enabled: false  # [default]
          
          
          resources:
            exposedAccessors: false  # [default]
            packageName:   # [default]
          
          version:
        
        
        junit: junit-5  # [default]
        jvm:
          release: 17  # [default]
          storeParameterNames: true
          test:
            freeJvmArgs: []  # [default]
            systemProperties: {}  # [default]
            
        ktor:
          enabled: false  # [default]
          version: 3.1.1  # [default]
        
        springBoot:
          $${springBootEnabledFromShowSettings}enabled: true  # [module.yaml (amper-playground)]$${springBootEnabledFromShowSettings}
          $${springBootVersionFromShowSettings}version: 3.4.3  # [default]$${springBootVersionFromShowSettings}
          
        kotlin:
          allOpen:
            enabled: true
            presets: [spring]
          
          allWarningsAsErrors: false  # [default]
          apiVersion: 2.1  # [Inherited from 'languageVersion']
          debug: true  # [default]
          ksp:
            processorOptions: {}  # [default]
            processors: []  # [default]
            version: 2.1.21-2.0.1  # [default]
          
          languageVersion: 2.1  # [default]
          noArg:
            enabled: true
            invokeInitializers: false  # [default]
            presets: [jpa]
          
          progressiveMode: false  # [default]
          serialization:
            enabled: false  # [Enabled when 'format' is specified]
            format:
            
            version: 1.8.0  # [default]
          
          suppressWarnings: false  # [default]
          verbose: false  # [default]$${afterShowSettings}
        """
            .trimIndent()
            .toCodeSample(language = Language.Yaml)
            .startWith { hide(afterShowSettings, normalSpringBootVersion) }
            .then { reveal(afterShowSettings).hide(normal) }
            .then { focus(springBootVersionFromShowSettings, unfocusedStyle = null) }
            .then { focus(springBootEnabledFromShowSettings, unfocusedStyle = null, scroll = false) }
            .then { unfocus() }
            .then { reveal(normal).hide(afterShowSettings) }
            .then { reveal(normalSpringBootVersion).focus(normalSpringBootVersion) }
            .then { unfocus() }
    }

    val initialState = stages.lastState
    val ideAppears = initialState + 1

    val samplesBeforeTerminal = 1
    val moduleYamlBeforeTerminal = moduleYaml.take(samplesBeforeTerminal)
    val moduleYamlAfterTerminal = moduleYaml.drop(samplesBeforeTerminal)

    val fileTreeHides = ideAppears + samplesBeforeTerminal
    val terminalAppears = fileTreeHides + 1

    val terminalTexts = listOf(
        "$ ./amper show settings",
        moduleYamlAfterTerminal[1].String().text
    )

    val terminalDisappears = terminalAppears + terminalTexts.size + 1
    val fileTreeAppears = terminalDisappears + 1

    val ideIsBackToNormal = fileTreeAppears + 1

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SlideFromLeftAnimatedVisibility({ it in terminalAppears until terminalDisappears }) {
            Terminal(
                textsToDisplay = terminalTexts.take(max(0, currentState - terminalAppears)),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .padding(start = 32.dp, bottom = 32.dp, end = 32.dp)
            )
        }

        SlideFromBottomAnimatedVisibility({ ideAppears <= it }) {
            IDE(
                files = buildList {
                    addFile(
                        name = "module.yaml",
                        content = createChildTransition {
                            when {
                                it >= ideIsBackToNormal -> moduleYamlAfterTerminal.safeGet(it - ideIsBackToNormal)
                                else -> moduleYamlBeforeTerminal.safeGet(it - ideAppears)
                            }
                        }
                    )
                },
                selectedFile = "module.yaml",
                fileTreeHidden = currentState in fileTreeHides until fileTreeAppears,
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
            )
        }
    }

    val states = stages.registerStatesByRange(start = initialState, end = terminalAppears)

    if (currentState in states) {
        title.value = "Amper + Spring Boot"
    }
}
