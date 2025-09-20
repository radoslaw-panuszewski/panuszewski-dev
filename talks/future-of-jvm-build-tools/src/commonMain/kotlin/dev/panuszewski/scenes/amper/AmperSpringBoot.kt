package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.Terminal
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addDirectory
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import kotlin.math.max

fun StoryboardBuilder.AmperSpringBoot() {

    val initialState = 0
    val ideAppears = initialState + 1

    val samplesBeforeTerminal = 1
    val moduleYamlBeforeTerminal = MODULE_YAML.take(samplesBeforeTerminal)
    val moduleYamlAfterTerminal = MODULE_YAML.drop(samplesBeforeTerminal)

    val mainKtIsSelected = ideAppears + samplesBeforeTerminal
    val exampleTestKtIsSelected = mainKtIsSelected + 1

    val terminalAppears = exampleTestKtIsSelected + 2

    val terminalTexts = listOf(
        "$ ./amper show settings",
        """
        compose:
          enabled: false  # [default]
          experimental:
            hotReload:
              enabled: false  # [default]
        resources:
          exposedAccessors: <truncated...>
        """.trimIndent()
    )

    val terminalDisappears = terminalAppears + terminalTexts.size + 1
    val ideIsBackToNormal = terminalDisappears + 1

    val ideShrinks = ideIsBackToNormal + moduleYamlAfterTerminal.size
    val finalState = ideShrinks

    scene(stateCount = finalState + 1) {
        MODULE_YAML.precompose()
        MAIN_KT.precompose()
        EXAMPLE_TEST_KT.precompose()

        withStateTransition {
            val ideTopPadding by animateDp { if (it >= ideShrinks) 281.dp else 0.dp }

            TitleScaffold("Amper + Spring Boot") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SlideFromBottomAnimatedVisibility({ it in terminalAppears until terminalDisappears }) {
                        Terminal(
                            textsToDisplay = terminalTexts.take(max(0, currentState - terminalAppears)),
                            bottomSpacerHeight = 0.dp,
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .padding(start = 32.dp, bottom = 32.dp, end = 16.dp)
                        )
                    }

                    SlideFromBottomAnimatedVisibility({ ideAppears <= it }) {
                        IDE_STATE = IdeState(
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
                                addDirectory(name = "src")
                                addDirectory(name = "com/example", path = "src/com/example")
                                addFile(
                                    name = "main.kt",
                                    path = "src/com/example/main.kt",
                                    content = createChildTransition { MAIN_KT[0] }
                                )
                                addDirectory(name = "test")
                                addDirectory(name = "com/example", path = "test/com/example")
                                addFile(
                                    name = "ExampleTest.kt",
                                    path = "test/com/example/ExampleTest.kt",
                                    content = createChildTransition { EXAMPLE_TEST_KT[0] }
                                )
                            },
                            selectedFile = when (currentState) {
                                mainKtIsSelected -> "src/com/example/main.kt"
                                exampleTestKtIsSelected -> "test/com/example/ExampleTest.kt"
                                else -> "module.yaml"
                            },
                        )
                        IDE(
                            ideState = IDE_STATE,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp)
                        )
                    }
                }
            }
        }
    }
}

private val MODULE_YAML = buildCodeSamples {
    val normal by tag()
    val normalSpringBootVersion by tag()
    val afterShowSettings by tag()
    val springBootVersionFromShowSettings by tag()
    val springBootVersionReason by tag()
    val springBootEnabledReason by tag()
    val mainClass by tag()

    $$"""
    $${normal}product: jvm/app

    settings:
      springBoot:
        enabled: true$${normalSpringBootVersion}
        version: 3.5.3$${normalSpringBootVersion}$${mainClass}
      jvm:
        mainClass: com.example.MainKt$${mainClass}

    dependencies:
      - $spring.boot.starter.web$${normal}$${afterShowSettings}compose:
      enabled: false  # [default]
      experimental:
        hotReload:
          enabled: false  # [default]
      resources:
        exposedAccessors: false  # [default]
        packageName:   # [default]
    
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
      enabled: true  $${springBootEnabledReason}# [module.yaml (amper-playground)]$${springBootEnabledReason}
      $${springBootVersionFromShowSettings}version: 3.4.3$${springBootVersionFromShowSettings}  $${springBootVersionReason}# [default]$${springBootVersionReason}
      
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
        .startWith { hide(afterShowSettings, normalSpringBootVersion, mainClass) }
        .then { reveal(afterShowSettings).hide(normal) }
        .then { focus(springBootVersionFromShowSettings, unfocusedStyle = null) }
        .then { focus(springBootVersionReason, unfocusedStyle = null) }
        .then { focus(springBootEnabledReason, unfocusedStyle = null, scroll = false) }
        .then { unfocus() }
        .then { reveal(normal).hide(afterShowSettings) }
        .then { reveal(normalSpringBootVersion).focus(normalSpringBootVersion, scroll = false) }
        .then { unfocus() }
}

val MAIN_KT = buildCodeSamples {
    """
    package com.example
    
    @SpringBootApplication
    class Application
    
    fun main(args: Array<String>) {
        runApplication<Application>(*args)
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
}

val EXAMPLE_TEST_KT = buildCodeSamples {
    """
    package com.example
    
    @SpringBootTest
    class ExampleTest {
    
        @Test
        fun contextLoads() { }
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
}