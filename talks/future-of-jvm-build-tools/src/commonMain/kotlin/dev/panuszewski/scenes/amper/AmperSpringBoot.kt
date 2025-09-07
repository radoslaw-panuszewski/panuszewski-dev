package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.IDE
import dev.panuszewski.components.Terminal
import dev.panuszewski.components.addDirectory
import dev.panuszewski.components.addFile
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.Stages
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import kotlin.math.max

/**
 * - [ ] avoid touching build tool
 * - [ ] it tells you that dependency version is overridden by a newer one (Gradle does not do that)
 * - [ ] configuration that reflects reality
 * - [x] ./amper show settings (pokazać default wersję spring boota)
 * - [x] show up when needed, stay out of the way when not
 * - [ ] smart completion (like when typed 'jdk' it finds 'jvm.release' property)
 */
@Composable
fun Transition<Int>.AmperSpringBoot(stages: Stages, title: MutableState<String>) {
    val moduleYaml = ModuleYaml()
    val mainKt = MainKt()
    val exampleTestKt = ExampleTestKt()

    val initialState = stages.lastState
    val ideAppears = initialState + 1

    val samplesBeforeTerminal = 1
    val moduleYamlBeforeTerminal = moduleYaml.take(samplesBeforeTerminal)
    val moduleYamlAfterTerminal = moduleYaml.drop(samplesBeforeTerminal)

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    addDirectory(name = "src")
                    addDirectory(name = "com/example", path = "src/com/example")
                    addFile(
                        name = "main.kt",
                        path = "src/com/example/main.kt",
                        content = createChildTransition { mainKt[0] }
                    )
                    addDirectory(name = "test")
                    addDirectory(name = "com/example", path = "test/com/example")
                    addFile(
                        name = "ExampleTest.kt",
                        path = "test/com/example/ExampleTest.kt",
                        content = createChildTransition { exampleTestKt[0] }
                    )
                },
                selectedFile = when (currentState) {
                    mainKtIsSelected -> "src/com/example/main.kt"
                    exampleTestKtIsSelected -> "test/com/example/ExampleTest.kt"
                    else -> "module.yaml"
                },
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
            )
        }
    }

    val states = stages.registerStatesByRange(start = initialState, end = terminalAppears)

    if (currentState in states) {
        title.value = "Amper + Spring Boot"
    }
}

@Composable
private fun ModuleYaml(): List<CodeSample> = buildAndRememberCodeSamples {
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
        .then { reveal(normalSpringBootVersion).focus(normalSpringBootVersion) }
        .then { unfocus() }
}

@Composable
private fun MainKt() = buildAndRememberCodeSamples {
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

@Composable
private fun ExampleTestKt() = buildAndRememberCodeSamples {
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