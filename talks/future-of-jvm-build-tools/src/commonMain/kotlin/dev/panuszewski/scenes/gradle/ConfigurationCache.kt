package dev.panuszewski.scenes.gradle

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.Terminal
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.components.MagicCodeSample
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.theme.appendWithPrimaryColor
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.code2
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.theme.primaryVariantColor
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.theme.withColor
import kotlin.math.max

fun StoryboardBuilder.ConfigurationCache() {
    val configurationBecomesLong = 1
    val titleChanges = configurationBecomesLong + 1
    val codeSamplesAppear = configurationBecomesLong + 1
    val terminalAppears = codeSamplesAppear + CODE_SAMPLES.size

    val terminalTexts = listOf(
        "$ ./gradlew doSomething",
        "Configuring the task...\n\n> Task :doSomething\nExecuting the task...",
        "$ ./gradlew doSomething",
        "Configuring the task... 😞\n\n> Task :doSomething UP-TO-DATE",
        "$ ./gradlew doSomething --configuration-cache",
        "Reusing configuration cache. ❤️\n\n> Task :doSomething UP-TO-DATE",
    )
    val terminalDisappears = terminalAppears + terminalTexts.size + 1

    val chartAppears = terminalDisappears + 1
    val chartDisappears = chartAppears + 2

    val treeAppears = chartDisappears + 1
    val treeDisappears = treeAppears + 6

    val bulletpointsAppear = treeDisappears + 1
    val bulletpointsDisappear = bulletpointsAppear + 3
    val stateCount = bulletpointsDisappear + 1

    scene(stateCount) {
        with(transition) {
            val configurationIsLong = createChildTransition { it.toState() in configurationBecomesLong until bulletpointsDisappear }

            val title = when {
                currentState.toState() >= titleChanges -> "Configuration Cache!"
                else -> "Gradle"
            }

            TitleScaffold(title) {
                PhasesBar(
                    phasesBarVisible = createChildTransition { it !is Frame.End },
                    configurationIsLong = configurationIsLong
                )

                FadeOutAnimatedVisibility({ it is Frame.State<*> }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SlideFromBottomAnimatedVisibility({ it.toState() in treeAppears until treeDisappears }) {
                            val primaryVariantColor = primaryVariantColor

                            val tree = when {
                                currentState.toState() >= treeAppears + 5 -> buildTree {
                                    val configuration =
                                        reusableNode("Configuration", primaryVariantColor) { node("Task graph", NICE_BLUE) }
                                    node("Gradle config files") { node(configuration) }
                                    node("Files read at config time") { node(configuration) }
                                    node("System props read at config time") { node(configuration) }
                                    node("Env variables read at config time") { node(configuration) }
                                }
                                currentState.toState() >= treeAppears + 4 -> buildTree {
                                    val configuration =
                                        reusableNode("Configuration", primaryVariantColor) { node("Task graph", NICE_BLUE) }
                                    node("Gradle config files") { node(configuration) }
                                    node("Files read at config time") { node(configuration) }
                                    node("System props read at config time") { node(configuration) }
                                }
                                currentState.toState() >= treeAppears + 3 -> buildTree {
                                    val configuration =
                                        reusableNode("Configuration", primaryVariantColor) { node("Task graph", NICE_BLUE) }
                                    node("Gradle config files") { node(configuration) }
                                    node("Files read at config time") { node(configuration) }
                                }
                                currentState.toState() >= treeAppears + 2 -> buildTree {
                                    val configuration =
                                        reusableNode("Configuration", primaryVariantColor) { node("Task graph", NICE_BLUE) }
                                    node("Gradle config files") { node(configuration) }
                                }
                                currentState.toState() >= treeAppears + 1 -> buildTree {
                                    node("Configuration", primaryVariantColor) { node("Task graph", NICE_BLUE) }
                                }
                                else -> buildTree {
                                    node("Configuration", primaryVariantColor)
                                }
                            }

                            AnimatedHorizontalTree(tree) { node ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(node.color ?: NICE_GREEN)
                                ) {
                                    ProvideTextStyle(TextStyle(color = MaterialTheme.colors.background)) {
                                        Text(text = node.value, modifier = Modifier.padding(8.dp))
                                    }
                                }
                            }
                        }

                        FadeOutAnimatedVisibility({ it.toState() in codeSamplesAppear until terminalDisappears }) {
                            Row {
                                Spacer(Modifier.width(32.dp))

                                SlideFromBottomAnimatedVisibility({ it.toState() >= codeSamplesAppear }) {
                                    code2 {
                                        createChildTransition { CODE_SAMPLES.safeGet(it.toState() - codeSamplesAppear) }
                                            .MagicCodeSample()
                                    }
                                }

                                Spacer(Modifier.width(32.dp))

                                SlideFromRightAnimatedVisibility({ it.toState() >= terminalAppears }) {
                                    val terminalTextsToDisplay = terminalTexts.take(max(0, currentState.toState() - terminalAppears))
                                    Terminal(terminalTextsToDisplay)
                                }
                            }
                        }

                        FadeOutAnimatedVisibility({ it.toState() in chartAppears until chartDisappears }) {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SlideFromTopAnimatedVisibility({ it.toState() >= chartAppears }) {
                                    h6 {
                                        Text {
                                            append("It can really save you ")
                                            withColor(Color(0xFFFF8A04)) { append("a lot of") }
                                            append(" time!")
                                        }
                                    }
                                }

                                SlideFromBottomAnimatedVisibility({ it.toState() >= chartAppears + 1 }) {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("30 s")
                                            Box(
                                                Modifier.background(MaterialTheme.colors.primary).width(100.dp)
                                                    .height(150.dp)
                                            )
                                            Text("CC off")
                                        }

                                        Spacer(Modifier.width(64.dp))

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("200 ms")
                                            Box(Modifier.background(Color(0xFFFF8A04)).width(100.dp).height(10.dp))
                                            Text("CC on")
                                        }
                                    }
                                }
                            }
                        }

                        FadeOutAnimatedVisibility({ it.toState() in bulletpointsAppear until bulletpointsDisappear }) {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SlideFromTopAnimatedVisibility({ it.toState() >= bulletpointsAppear }) {
                                    h6 { Text("Introduced in Gradle 6.6, made stable in 8.1") }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= bulletpointsAppear + 1 }) {
                                    h6 { Text("Preferred mode in 9.0 (still not default, though)") }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= bulletpointsAppear + 2 }) {
                                    h6 {
                                        Text {
                                            append("Enable it now in your ")
                                            appendWithPrimaryColor("gradle.properties")
                                            append("!")
                                        }
                                    }
                                }

                                SlideFromBottomAnimatedVisibility({ it.toState() >= bulletpointsAppear + 2 }) {
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                color = Color.Black,
                                                width = 1.dp,
                                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
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
        }
    }
}

val CODE_SAMPLES = buildCodeSamples {
    val configuring by tag()
    val executing by tag()
    val writeOutput by tag()

    """
    tasks {
        register("doSomething") {
            ${configuring}println("Configuring the task...")${configuring}
            doLast {
                ${executing}println("Executing the task...")${executing}
                ${writeOutput}File("build/out.txt").writeText("Done!")${writeOutput}
            }
        }
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
        .then { focus(configuring) }
        .then { focus(executing) }
        .then { focus(writeOutput) }
        .then { unfocus() }
}