package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.Terminal
import dev.panuszewski.scenes.Stages.CHARACTERIZING_PHASES
import dev.panuszewski.scenes.Stages.CONFIGURATION_IS_LONG
import dev.panuszewski.scenes.Stages.EXECUTION_IS_LONG
import dev.panuszewski.scenes.Stages.EXPLAINING_BUILD_CACHE
import dev.panuszewski.scenes.Stages.EXPLAINING_CONFIG_EXECUTION_DIFFERENCE
import dev.panuszewski.scenes.Stages.PHASES_BAR_VISIBLE_SINCE
import dev.panuszewski.scenes.Stages.SHOWING_THAT_BUILD_CACHE_IS_OLD
import dev.panuszewski.template.Connection
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.HorizontalTree
import dev.panuszewski.template.MagicCodeSample
import dev.panuszewski.template.MagicString
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.code2
import dev.panuszewski.template.code3
import dev.panuszewski.template.h4
import dev.panuszewski.template.h6
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.toCode
import dev.panuszewski.template.withPrimaryColor
import dev.panuszewski.template.withSecondaryColor
import kotlin.math.max

object Stages {
    var lastState = 0
        private set

    val stateCount get() = lastState + 1

    fun states(since: Int, count: Int): List<Int> {
        val stateList = (since until since + count).toList()
        stateList.lastOrNull()?.let { lastState = it }
        return stateList
    }

    val CHARACTERIZING_PHASES = states(since = lastState + 2, count = 3)
    val EXPLAINING_CONFIG_EXECUTION_DIFFERENCE = states(since = lastState + 2, count = 5)
    val EXECUTION_BECOMES_LONG = states(since = lastState + 2, count = 1)
    val EXPLAINING_BUILD_CACHE = states(since = lastState + 1, count = 12)
    val SHOWING_THAT_BUILD_CACHE_IS_OLD = states(since = lastState + 2, count = 2)
    val EXECUTION_BECOMES_SHORT = states(since = lastState, count = 1)
    val CONFIGURATION_IS_LONG = states(since = lastState + 2, count = 21)

    val PHASES_BAR_VISIBLE_SINCE = 1
    val EXECUTION_IS_LONG = EXECUTION_BECOMES_LONG.first()..EXECUTION_BECOMES_SHORT.first()
}

lateinit var stateTransition: Transition<Int>

fun StoryboardBuilder.Gradle() {
    scene(Stages.stateCount) {
        stateTransition = transition.createChildTransition { it.toState() }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            h4 {
                stateTransition.createChildTransition {
                    when {
                        it in EXECUTION_IS_LONG.drop(1) -> "Build Cache!"
                        it in CONFIGURATION_IS_LONG.drop(1) -> "Configuration Cache!"
                        else -> "Gradle"
                    }
                }.MagicString(split = { it.splitByChars() })
            }
            Spacer(Modifier.height(32.dp))
            PhasesBar()
            Spacer(Modifier.height(32.dp))
            ExplainingConfigExecutionDifference()
            ExplainingBuildCache()
            ShowingThatBuildCacheIsOld()
            ExplainingConfigurationCache()
        }
    }
}

@Composable
private fun PhasesBar() {
    val phaseNameTextStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.background)
    val phaseDescriptionTextStyle = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.background, fontSize = 12.sp)
    val executionIsLong = stateTransition.createChildTransition { it in EXECUTION_IS_LONG }
    val configurationIsLong = stateTransition.createChildTransition { it in CONFIGURATION_IS_LONG }
    val executionPhaseWeight by executionIsLong.animateFloat { if (it) 1.5f else 0.5f }
    val configurationPhaseWeight by configurationIsLong.animateFloat { if (it) 1.5f else 0.5f }

    stateTransition.AnimatedVisibility({ it >= PHASES_BAR_VISIBLE_SINCE }) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 100.dp)) {
            Column(Modifier.background(color = MaterialTheme.colors.primary)) {
                ProvideTextStyle(phaseNameTextStyle) { Text("Initialization", Modifier.padding(16.dp)) }
                stateTransition.AnimatedVisibility({ it == CHARACTERIZING_PHASES[0] }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Figure out the project structure", modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Column(Modifier.fillMaxWidth().weight(configurationPhaseWeight).background(color = MaterialTheme.colors.primaryVariant)) {
                ProvideTextStyle(phaseNameTextStyle) {
                    configurationIsLong.createChildTransition {
                        if (it) "Configuraaaaaaaaaation"
                        else "Configuration"
                    }.MagicString(modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                }
                stateTransition.AnimatedVisibility({ it == CHARACTERIZING_PHASES[1] }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Figure out the task graph", modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Column(Modifier.fillMaxWidth().weight(executionPhaseWeight).background(color = MaterialTheme.colors.secondary)) {
                ProvideTextStyle(phaseNameTextStyle) {
                    executionIsLong.createChildTransition {
                        if (it) "Execuuuuuuuuuution"
                        else "Execution"
                    }.MagicString(modifier = Modifier.padding(16.dp), split = { it.splitByChars() })
                }
                stateTransition.AnimatedVisibility({ it == CHARACTERIZING_PHASES[2] }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Execute the tasks! 🚀", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowingThatBuildCacheIsOld() {
    stateTransition.FadeOutAnimatedVisibility({ it in SHOWING_THAT_BUILD_CACHE_IS_OLD }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            h6 {
                stateTransition.SlideFromTopAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[0] }) {
                    Text("Build Cache is there since Gradle 3.5 👴🏼")
                }
                Spacer(Modifier.height(32.dp))
                stateTransition.SlideFromTopAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[1] }) {
                    Text("Just enable it in your gradle.properties!")
                }
            }

            Spacer(Modifier.height(32.dp))

            stateTransition.SlideFromBottomAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[1] }) {
                Box(
                    modifier = Modifier
                        .border(
                            color = Color.Black,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text("org.gradle.caching=true".toCode(language = Language.Properties))
                }
            }
        }
    }
}

@Composable
private fun ExplainingConfigExecutionDifference() {
    val phaseSamples = buildAndRememberCodeSamples {
        val first by tag()
        val second by tag()
        val third by tag()
        val fourth by tag()

        """
        plugins {${third}
            println("Even this place belongs to configuration phase")${third}
            java
        }${first}
        
        println("Hello from configuration phase!")${first}
        
        dependencies {${second}
            println("It's still configuration phase")${second}
            implementation("pl.allegro.tech.common:andamio-starter-core:9.0.0")
        }
    
        tasks {
            register("sayHello") {
                doLast {
                    ${fourth}println("Finally, the execution phase!")${fourth}
                }
            }
        }
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(first, second, third, fourth) }
            .then { reveal(first).focus(first) }
            .then { reveal(second).focus(second).hide(first) }
            .then { reveal(third).focus(third).hide(second) }
            .then { reveal(fourth).focus(fourth).hide(third) }
    }

    stateTransition.SlideFromBottomAnimatedVisibility({ it in EXPLAINING_CONFIG_EXECUTION_DIFFERENCE }) {
        code2 {
            stateTransition.createChildTransition { phaseSamples.safeGet(it - EXPLAINING_CONFIG_EXECUTION_DIFFERENCE.first()) }
                .MagicCodeSample()
        }
    }
}

@Composable
private fun ExplainingBuildCache() {
    val buildCacheSamples = buildAndRememberCodeSamples {
        val cacheable by tag()

        """
        ${cacheable}@CacheableTask
        ${cacheable}abstract class PrintMessageTask : DefaultTask() {
        
            @OutputFile
            val outputFile = project.objects.fileProperty()
        
            @TaskAction
            fun execute() {
                println("Groovy should die")
                outputFile.get().asFile.writeText("Job done")
            }
        }
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(cacheable) }
            .then { reveal(cacheable).focus(cacheable) }
    }

    stateTransition.FadeOutAnimatedVisibility({ it in EXPLAINING_BUILD_CACHE }) {
        val terminalTexts = listOf(
            "$ ./gradlew printMessage",
            "> Task :printMessage\nGroovy should die",
            "$ ./gradlew printMessage",
            "> Task :printMessage UP-TO-DATE",
            "$ ./gradlew clean printMessage",
            "> Task :printMessage\nGroovy should die",
            "",
            "$ ./gradlew clean printMessage",
            "> Task :printMessage FROM-CACHE",
        )
        val terminalTextsToDisplay = terminalTexts
            .take(max(0, stateTransition.currentState - EXPLAINING_BUILD_CACHE[2]))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(32.dp))

            Row {
                stateTransition.SlideFromBottomAnimatedVisibility({ it >= EXPLAINING_BUILD_CACHE[1] }) {
                    code2 {
                        stateTransition.createChildTransition {
                            val texts = terminalTexts.take(max(0, it - EXPLAINING_BUILD_CACHE[2]))
                            if (texts.contains("")) {
                                buildCacheSamples[1]
                            } else {
                                buildCacheSamples[0]
                            }
                        }
                            .MagicCodeSample()
                    }
                }

                Spacer(Modifier.width(32.dp))

                stateTransition.SlideFromRightAnimatedVisibility({ it >= EXPLAINING_BUILD_CACHE[2] }) {
                    Terminal(terminalTextsToDisplay)
                }
            }
        }
    }
}

@Composable
fun ExplainingConfigurationCache() {
    val codeSamples = buildAndRememberCodeSamples {
        val configuring by tag()
        val executing by tag()

        """
        tasks {
            register<PrintMessageTask>("printMessage") {
                ${configuring}println("Configuring the task...")${configuring}
                outputFile = file("output.txt")
            }
        }
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { this }
            .then { focus(configuring) }
            .then { unfocus() }
    }

    val afterCodeSamples = CONFIGURATION_IS_LONG[4] + codeSamples.size

    val terminalTexts = listOf(
        "$ ./gradlew printMessage",
        "Configuring the task...\n\n> Task :printMessage\nGroovy should die",
        "$ ./gradlew printMessage",
        "Configuring the task...\n\n> Task :printMessage UP-TO-DATE",
        "$ ./gradlew printMessage --configuration-cache",
        "Reusing configuration cache. ❤️\n\n> Task :printMessage UP-TO-DATE",
    )
    val terminalTextsToDisplay = terminalTexts.take(max(0, stateTransition.currentState - afterCodeSamples))
    val afterTerminal = afterCodeSamples + terminalTexts.size + 1

    stateTransition.FadeOutAnimatedVisibility({ it in CONFIGURATION_IS_LONG }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            stateTransition.SlideFromBottomAnimatedVisibility({ it == CONFIGURATION_IS_LONG[2] }) {
                val inputs = listOf(
                    "Gradle configs",
                    "Files read at config time",
                    "System props read at config time",
                    "Env variables read at config time",
                )

                HorizontalTree(
                    roots = inputs,
                    getChildren = { if (it in inputs) listOf("Configuration") else emptyList() },
                    connection = { _, parentRect, _, childRect -> Connection(parentRect, childRect) }
                ) {
                    Box(Modifier.border(1.dp, Color.Black, RoundedCornerShape(8.dp))) {
                        Text(modifier = Modifier.padding(8.dp), text = it)
                    }
                }
            }

            stateTransition.FadeOutAnimatedVisibility({ it in CONFIGURATION_IS_LONG[4] until afterTerminal }) {
                Row {
                    Spacer(Modifier.width(32.dp))

                    stateTransition.SlideFromBottomAnimatedVisibility({ it >= CONFIGURATION_IS_LONG[4] }) {
                        code2 {
                            stateTransition.createChildTransition { codeSamples.safeGet(it - CONFIGURATION_IS_LONG[4]) }
                                .MagicCodeSample()
                        }
                    }

                    Spacer(Modifier.width(32.dp))

                    stateTransition.SlideFromRightAnimatedVisibility({ it >= afterCodeSamples }) {
                        Terminal(terminalTextsToDisplay, Modifier.fillMaxWidth().fillMaxHeight().padding(bottom = 32.dp, end = 32.dp))
                    }
                }
            }

            stateTransition.FadeOutAnimatedVisibility({ it in afterTerminal..(afterTerminal + 2) }) {

                Column(verticalArrangement = Arrangement.spacedBy(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    stateTransition.SlideFromTopAnimatedVisibility({ it > afterTerminal }) {
                        h6 {
                            Text(buildAnnotatedString {
                                append("It can really save you ")
                                withPrimaryColor { append("a lot of") }
                                append(" time!")
                            })
                        }
                    }

                    stateTransition.SlideFromBottomAnimatedVisibility({ it > afterTerminal + 1 }) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("30 s")
                                Box(Modifier.background(MaterialTheme.colors.primaryVariant).width(100.dp).height(150.dp))
                                Text("CC off")
                            }

                            Spacer(Modifier.width(64.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("200 ms")
                                Box(Modifier.background(MaterialTheme.colors.primary).width(100.dp).height(10.dp))
                                Text("CC on")
                            }
                        }
                    }
                }
            }

            stateTransition.FadeOutAnimatedVisibility({ it >= afterTerminal + 3 }) {

                Column(verticalArrangement = Arrangement.spacedBy(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    stateTransition.SlideFromTopAnimatedVisibility({ it > afterTerminal + 3 }) {
                        h6 { Text("Introduced in Gradle 6.6, made stable in 8.1") }
                    }

                    stateTransition.SlideFromTopAnimatedVisibility({ it > afterTerminal + 4 }) {
                        h6 { Text("Preferred mode in 9.0 (still not default, though)") }
                    }

                    stateTransition.SlideFromTopAnimatedVisibility({ it > afterTerminal + 5 }) {
                        h6 { Text("Enable it now!") }
                    }

                    stateTransition.SlideFromBottomAnimatedVisibility({ it > afterTerminal + 5 }) {
                        Box(
                            modifier = Modifier
                                .border(
                                    color = Color.Black,
                                    width = 1.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Text("org.gradle.configuration-cache=true".toCode(language = Language.Properties))
                        }
                    }
                }
            }
        }
    }
}
