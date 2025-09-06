package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.ProjectFile
import dev.panuszewski.components.Terminal
import dev.panuszewski.scenes.Hat.BASEBALL_CAP
import dev.panuszewski.scenes.Hat.TOP_HAT
import dev.panuszewski.scenes.Stages.CHARACTERIZING_PHASES
import dev.panuszewski.scenes.Stages.CONFIGURATION_IS_LONG
import dev.panuszewski.scenes.Stages.EXECUTION_IS_LONG
import dev.panuszewski.scenes.Stages.EXPLAINING_BUILD_CACHE
import dev.panuszewski.scenes.Stages.EXPLAINING_CONFIG_EXECUTION_DIFFERENCE
import dev.panuszewski.scenes.Stages.CONVENTION_PLUGINS
import dev.panuszewski.scenes.Stages.APP_DEVELOPER_AND_BUILD_ENGINEER
import dev.panuszewski.scenes.Stages.DECLARATIVE_GRADLE
import dev.panuszewski.scenes.Stages.EXPLAINING_CONVENTION_PLUGINS
import dev.panuszewski.scenes.Stages.PHASES_BAR_VISIBLE
import dev.panuszewski.scenes.Stages.SHOWING_THAT_BUILD_CACHE_IS_OLD
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.Connection
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.HorizontalTree
import dev.panuszewski.template.MagicCodeSample
import dev.panuszewski.template.MagicString
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.body1
import dev.panuszewski.template.body2
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.code2
import dev.panuszewski.template.h1
import dev.panuszewski.template.h4
import dev.panuszewski.template.h6
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.toCode
import dev.panuszewski.template.withColor
import dev.panuszewski.template.withPrimaryColor
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

    val DECLARATIVE_GRADLE = states(lastState + 1, count = 100)
    val APP_DEVELOPER_AND_BUILD_ENGINEER = states(lastState + 1, count = 6)
    val EXTRACTING_CONVENTION_PLUGIN = states(since = lastState + 1, count = 9)
    val EXPLAINING_CONVENTION_PLUGINS = states(since = lastState + 1, count = 24)
    val PHASES_BAR_APPEARS = states(since = lastState + 1, count = 1)
    val CHARACTERIZING_PHASES = states(since = lastState + 1, count = 3)
    val EXPLAINING_CONFIG_EXECUTION_DIFFERENCE = states(since = lastState + 2, count = 5)
    val EXECUTION_BECOMES_LONG = states(since = lastState + 2, count = 1)
    val EXPLAINING_BUILD_CACHE = states(since = lastState + 1, count = 12)
    val SHOWING_THAT_BUILD_CACHE_IS_OLD = states(since = lastState + 2, count = 2)
    val EXECUTION_BECOMES_SHORT = states(since = lastState + 1, count = 1)
    val CONFIGURATION_IS_LONG = states(since = lastState + 1, count = 21)
    val PHASES_BAR_DISAPPEARS = states(since = lastState + 2, count = 1)

    val PHASES_BAR_VISIBLE = PHASES_BAR_APPEARS.first() until PHASES_BAR_DISAPPEARS.first()
    val EXECUTION_IS_LONG = EXECUTION_BECOMES_LONG.first() until EXECUTION_BECOMES_SHORT.first()
    val CONVENTION_PLUGINS = EXTRACTING_CONVENTION_PLUGIN + EXPLAINING_CONVENTION_PLUGINS
}

fun StoryboardBuilder.Gradle() {
    scene(stateCount = Stages.stateCount) {
        val stateTransition = transition.createChildTransition { it.toState() }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            stateTransition.Title()
            stateTransition.PhasesBar()
            stateTransition.ExplainingConfigExecutionDifference()
            stateTransition.ExplainingBuildCache()
            stateTransition.ShowingThatBuildCacheIsOld()
            // TODO merge Build Cache and Configuration Cache to a single example: "Caching in Action!"
            stateTransition.ExplainingConfigurationCache()
            stateTransition.ConventionPlugins()
            stateTransition.AppDeveloperAndBuildEngineer()
            stateTransition.DeclarativeGradle()
        }
    }
}

@Composable
private fun Transition<Int>.Title() {
    Spacer(Modifier.height(16.dp))
    h4 {
        AnimatedContent(
            targetState = when (currentState) {
                in EXECUTION_IS_LONG.drop(1) -> "Build Cache!"
                in CONFIGURATION_IS_LONG.drop(1) -> "Configuration Cache!"
                in EXPLAINING_CONVENTION_PLUGINS -> "Convention Plugins"
                in DECLARATIVE_GRADLE -> "Declarative Gradle"
                else -> "Gradle"
            },
        ) { text ->
            Text(text)
        }
    }
}

@Composable
private fun Transition<Int>.PhasesBar() {
    val phaseNameTextStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.background)
    val phaseDescriptionTextStyle = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.background, fontSize = 12.sp)
    val executionIsLong = createChildTransition { it in EXECUTION_IS_LONG }
    val configurationIsLong = createChildTransition { it in CONFIGURATION_IS_LONG }
    val executionPhaseWeight by executionIsLong.animateFloat { if (it) 1.5f else 0.5f }
    val configurationPhaseWeight by configurationIsLong.animateFloat { if (it) 1.5f else 0.5f }

    if (currentState in (PHASES_BAR_VISIBLE.first - 1)..PHASES_BAR_VISIBLE.last) {
        Spacer(Modifier.height(32.dp))
    }

    AnimatedVisibility({ it in PHASES_BAR_VISIBLE }) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 100.dp)) {
            Column(Modifier.background(color = MaterialTheme.colors.primary)) {
                ProvideTextStyle(phaseNameTextStyle) { Text("Initialization", Modifier.padding(16.dp)) }
                AnimatedVisibility({ it == CHARACTERIZING_PHASES[0] }) {
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
                AnimatedVisibility({ it == CHARACTERIZING_PHASES[1] }) {
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
                AnimatedVisibility({ it == CHARACTERIZING_PHASES[2] }) {
                    ProvideTextStyle(phaseDescriptionTextStyle) {
                        Text(text = "Execute the tasks! 🚀", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
    if (currentState in PHASES_BAR_VISIBLE) {
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun Transition<Int>.ShowingThatBuildCacheIsOld() {
    FadeOutAnimatedVisibility({ it in SHOWING_THAT_BUILD_CACHE_IS_OLD }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            h6 {
                SlideFromTopAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[0] }) {
                    Text("Build Cache is there since Gradle 3.5 👴🏼")
                }
                Spacer(Modifier.height(32.dp))
                SlideFromTopAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[1] }) {
                    Text("Just enable it in your gradle.properties!")
                }
            }

            Spacer(Modifier.height(32.dp))

            SlideFromBottomAnimatedVisibility({ it >= SHOWING_THAT_BUILD_CACHE_IS_OLD[1] }) {
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
private fun Transition<Int>.ExplainingConfigExecutionDifference() {
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

    SlideFromBottomAnimatedVisibility({ it in EXPLAINING_CONFIG_EXECUTION_DIFFERENCE }) {
        code2 {
            createChildTransition { phaseSamples.safeGet(it - EXPLAINING_CONFIG_EXECUTION_DIFFERENCE.first()) }
                .MagicCodeSample()
        }
    }
}

@Composable
private fun Transition<Int>.ExplainingBuildCache() {
    val codeSamples = buildAndRememberCodeSamples {
        val cacheable by tag()

        """
        ${cacheable}@CacheableTask
        ${cacheable}abstract class PrintMessageTask : DefaultTask() {
        
            @OutputFile
            val outputFile = project.objects.fileProperty()
        
            @TaskAction
            fun execute() {
                println("Executing the task...")
                outputFile.get().asFile.writeText("Job done")
            }
        }
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(cacheable) }
            .then { reveal(cacheable).focus(cacheable) }
    }

    FadeOutAnimatedVisibility({ it in EXPLAINING_BUILD_CACHE }) {
        val terminalTexts = listOf(
            "$ ./gradlew printMessage",
            "> Task :printMessage\nExecuting the task...",
            "$ ./gradlew printMessage",
            "> Task :printMessage UP-TO-DATE",
            "$ ./gradlew clean printMessage",
            "> Task :printMessage\nExecuting the task... 😞",
            "",
            "$ ./gradlew clean printMessage",
            "> Task :printMessage FROM-CACHE ❤️",
        )
        val terminalTextsToDisplay = terminalTexts
            .take(max(0, currentState - EXPLAINING_BUILD_CACHE[2]))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                SlideFromBottomAnimatedVisibility({ it >= EXPLAINING_BUILD_CACHE[1] }) {
                    code2 {
                        createChildTransition {
                            val texts = terminalTexts.take(max(0, it - EXPLAINING_BUILD_CACHE[2]))
                            if (texts.contains("")) {
                                codeSamples[1]
                            } else {
                                codeSamples[0]
                            }
                        }
                            .MagicCodeSample()
                    }
                }

                Spacer(Modifier.width(32.dp))

                SlideFromRightAnimatedVisibility({ it >= EXPLAINING_BUILD_CACHE[2] }) {
                    Terminal(terminalTextsToDisplay)
                }
            }
        }
    }
}

@Composable
fun Transition<Int>.ExplainingConfigurationCache() {
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
        "Configuring the task...\n\n> Task :printMessage\nExecuting the task...",
        "$ ./gradlew printMessage",
        "Configuring the task... 😞\n\n> Task :printMessage UP-TO-DATE",
        "$ ./gradlew printMessage --configuration-cache",
        "Reusing configuration cache. ❤️\n\n> Task :printMessage UP-TO-DATE",
    )
    val terminalTextsToDisplay = terminalTexts.take(max(0, currentState - afterCodeSamples))
    val afterTerminal = afterCodeSamples + terminalTexts.size + 1

    FadeOutAnimatedVisibility({ it in CONFIGURATION_IS_LONG }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SlideFromBottomAnimatedVisibility({ it == CONFIGURATION_IS_LONG[2] }) {
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

            FadeOutAnimatedVisibility({ it in CONFIGURATION_IS_LONG[4] until afterTerminal }) {
                Row {
                    Spacer(Modifier.width(32.dp))

                    SlideFromBottomAnimatedVisibility({ it >= CONFIGURATION_IS_LONG[4] }) {
                        code2 {
                            createChildTransition { codeSamples.safeGet(it - CONFIGURATION_IS_LONG[4]) }
                                .MagicCodeSample()
                        }
                    }

                    Spacer(Modifier.width(32.dp))

                    SlideFromRightAnimatedVisibility({ it >= afterCodeSamples }) {
                        Terminal(terminalTextsToDisplay)
                    }
                }
            }

            FadeOutAnimatedVisibility({ it in afterTerminal..(afterTerminal + 2) }) {

                Column(verticalArrangement = Arrangement.spacedBy(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SlideFromTopAnimatedVisibility({ it > afterTerminal }) {
                        h6 {
                            Text(buildAnnotatedString {
                                append("It can really save you ")
                                withColor(Color(0xFFFF8A04)) { append("a lot of") }
                                append(" time!")
                            })
                        }
                    }

                    SlideFromBottomAnimatedVisibility({ it > afterTerminal + 1 }) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("30 s")
                                Box(Modifier.background(MaterialTheme.colors.primary).width(100.dp).height(150.dp))
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

            FadeOutAnimatedVisibility({ it >= afterTerminal + 3 }) {

                Column(verticalArrangement = Arrangement.spacedBy(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SlideFromTopAnimatedVisibility({ it > afterTerminal + 3 }) {
                        h6 { Text("Introduced in Gradle 6.6, made stable in 8.1") }
                    }

                    SlideFromTopAnimatedVisibility({ it > afterTerminal + 4 }) {
                        h6 { Text("Preferred mode in 9.0 (still not default, though)") }
                    }

                    SlideFromTopAnimatedVisibility({ it > afterTerminal + 5 }) {
                        h6 { Text("Enable it now!") }
                    }

                    SlideFromBottomAnimatedVisibility({ it > afterTerminal + 5 }) {
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

private fun MutableList<ProjectFile>.addFile(
    name: String,
    path: String = name,
    content: Transition<CodeSample>? = null,
    staticContent: Transition<AnnotatedString>? = null
) {
    val file = ProjectFile(name = name, path = path, content = content, staticContent = staticContent)
    add(file)
}

private fun MutableList<ProjectFile>.addDirectory(name: String, path: String = name) {
    val file = ProjectFile(name = name, path = path, isDirectory = true)
    add(file)
}

@Composable
fun Transition<Int>.ConventionPlugins() {
    val buildGradleKts = buildAndRememberCodeSamples {
        val pluginsBlock by tag()
        val pluginsBlockNewline by tag()
        val mavenPublishDeclarative by tag()
        val mavenPublishImperative by tag()
        val topIfCi by tag()
        val bottomIfCi by tag()
        val topWhen by tag()
        val bottomWhen by tag()
        val monday by tag()
        val postgres by tag()
        val cassandra by tag()
        val masochistIfTop by tag()
        val masochistIfBottom by tag()
        val javaPlugin by tag()
        val wtfAppPlugin by tag()
        val randomDatabase by tag()
        val groovy by tag()
        val someImperativeCode by tag()

        """
        ${pluginsBlock}plugins {${javaPlugin}
            `java`${javaPlugin}${mavenPublishDeclarative}
            `maven-publish`${mavenPublishDeclarative}${wtfAppPlugin}
            `wtf-app`${wtfAppPlugin}${pluginsBlockNewline}
        ${pluginsBlockNewline}}
        
        ${pluginsBlock}${mavenPublishImperative}${topIfCi}if (System.getenv("CI") == "true") {
            ${topIfCi}apply(plugin = "maven-publish")${bottomIfCi}
        }${bottomIfCi}
        
        ${mavenPublishImperative}${someImperativeCode}for (project in subprojects) {
            apply(plugin = "kotlin")
        }
        
        ${someImperativeCode}dependencies {
            implementation(libs.spring.boot)${randomDatabase}
            ${topWhen}
            when (today()) {
                ${topWhen}${monday}MONDAY -> ${monday}implementation(libs.mongodb)${postgres}
                TUESDAY -> implementation(libs.postgres)${postgres}${cassandra}
                else -> implementation(libs.cassandra)${cassandra}${bottomWhen}
            }${bottomWhen}${randomDatabase}${groovy}
            ${masochistIfTop}
            if (masochistModeEnabled()) {
                ${masochistIfTop}implementation(libs.groovy)${masochistIfBottom}
            }${masochistIfBottom}${groovy}
        }
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(wtfAppPlugin, mavenPublishImperative, topIfCi, bottomIfCi, topWhen, bottomWhen, monday, postgres, cassandra, masochistIfTop, masochistIfBottom, someImperativeCode) }
            .then { reveal(mavenPublishImperative).hide(mavenPublishDeclarative) }
            .then { reveal(topIfCi, bottomIfCi) }
            .then { reveal(topWhen, bottomWhen, monday) }
            .then { reveal(postgres, cassandra) }
            .then { reveal(masochistIfTop, masochistIfBottom) }
            .then { focus(mavenPublishImperative, randomDatabase, groovy) }
            .then { hide(mavenPublishImperative, randomDatabase, groovy).unfocus() }
            .then { this }
            .then { focus(javaPlugin) }
            .then { hide(javaPlugin, pluginsBlockNewline).unfocus() }
            .then { reveal(wtfAppPlugin, pluginsBlockNewline).focus(wtfAppPlugin) }
            .then { unfocus() }
            .then { reveal(someImperativeCode).focus(someImperativeCode) }
            .then { this }
            .then { unfocus() }
    }

    val wtfApp = buildAndRememberCodeSamples {
        val todo by tag()
        val imperativeCode by tag()
        val javaPlugin by tag()
        val implementation1 by tag()
        val implementation2 by tag()
        val implementation3 by tag()
        val implementation4 by tag()

        """
        ${javaPlugin}plugins {
            `java`
        }
        
        ${javaPlugin}${imperativeCode}if (System.getenv("CI") == "true") {
            apply(plugin = "maven-publish")
        }
        
        dependencies {
            when (today()) {
                MONDAY -> ${implementation1}implementation${implementation1}(libs.mongodb)
                TUESDAY -> ${implementation2}implementation${implementation2}(libs.postgres)
                else -> ${implementation3}implementation${implementation3}(libs.cassandra)
            }
            if (masochistModeEnabled()) {
                ${implementation4}implementation${implementation4}(libs.groovy)
            }
        }
        
        ${imperativeCode}${todo}// TODO${todo}
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(javaPlugin, imperativeCode) }
            .then { this }
            .then { reveal(imperativeCode) }
            .then { focus(implementation1, implementation2, implementation3, implementation4, focusedStyle = SpanStyle(Color.Red), unfocusedStyle = null).hide(todo) }
            .then { this }
            .then { reveal(javaPlugin).unfocus() }
            .then { focus(todo) }
            .then { unfocus() }
    }

    val buildGradleKtsBeforeSplitPane = buildGradleKts.take(6)
    val buildGradleKtsOnSplitPane = buildGradleKts.drop(buildGradleKtsBeforeSplitPane.size - 1).take(8)
    val buildGradleKtsInSmallIde = buildGradleKts.takeLast(4)

    val grimacingEmojiVisible = CONVENTION_PLUGINS[0] + buildGradleKtsBeforeSplitPane.size
    val ideIsShrinkedSince = grimacingEmojiVisible + 2
    val ideIsBackToNormalSince = EXPLAINING_CONVENTION_PLUGINS[4]

    val splitPaneEnabledSince = ideIsBackToNormalSince + 1
    val fileTreeHiddenSince = splitPaneEnabledSince + 1
    val splitPaneClosedSince = fileTreeHiddenSince + buildGradleKtsOnSplitPane.size
    val fileTreeRevealedSince = splitPaneClosedSince

    val ideIsShrinkedAgainSince = fileTreeRevealedSince + 1

    val ideBackToNormalAgainSince = ideIsShrinkedAgainSince + 3
    val noIdeaEmojiVisible = ideBackToNormalAgainSince + 2
    val ideIsShrinked3Since = noIdeaEmojiVisible + 2
    val ideHiddenSince = ideIsShrinked3Since + 2

    val ideTopPadding by animateDp {
        when {
            it >= ideIsShrinked3Since -> 275.dp
            it >= ideBackToNormalAgainSince -> 150.dp
            it >= ideIsShrinkedAgainSince -> 275.dp
            it >= ideIsBackToNormalSince -> 32.dp
            it >= ideIsShrinkedSince -> 275.dp
            else -> 32.dp
        }
    }

    FadeOutAnimatedVisibility({ it in CONVENTION_PLUGINS }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            FadeOutAnimatedVisibility({ EXPLAINING_CONVENTION_PLUGINS[0] <= it && it < ideIsBackToNormalSince }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SlideFromTopAnimatedVisibility({ EXPLAINING_CONVENTION_PLUGINS[1] <= it }) {
                        h6 {
                            Text {
                                append("A ")
                                withPrimaryColor { append("convention plugin") }
                                append(" is just a Gradle plugin local to your project")
                            }
                        }
                    }

                    SlideFromTopAnimatedVisibility({ EXPLAINING_CONVENTION_PLUGINS[2] <= it }) {
                        h6 {
                            Text {
                                append("It can be used to encapsulate an ")
                                withPrimaryColor { append("imperative build logic") }
                            }
                        }
                    }

                    SlideFromTopAnimatedVisibility({ EXPLAINING_CONVENTION_PLUGINS[3] <= it }) {
                        h6 {
                            Text {
                                append("Or share some ")
                                withPrimaryColor { append("common defaults") }
                                append(" (like the JVM version)")
                            }
                        }
                    }
                }
            }

            FadeOutAnimatedVisibility({ ideIsShrinkedAgainSince <= it && it < ideHiddenSince }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SlideFromTopAnimatedVisibility({ ideIsShrinkedAgainSince + 1 <= it }) {
                        h6 { Text { append("It's cool, but... is it enough?") } }
                    }

                    SlideFromTopAnimatedVisibility({ ideIsShrinkedAgainSince + 2 <= it }) {
                        h6 {
                            Text {
                                append("This approach encourages declarativity, but ")
                                withPrimaryColor { append("does not enforce it") }
                            }
                        }
                    }

                    SlideFromTopAnimatedVisibility({ ideIsShrinked3Since + 1 <= it }) {
                        h6 {
                            Text {
                                append("Also, conventions often ")
                                withPrimaryColor { append("mix") }
                                append(" build logic with shared defaults")
                            }
                        }
                    }
                }
            }

            Box(contentAlignment = Alignment.Center) {
                SlideFromBottomAnimatedVisibility({ CONVENTION_PLUGINS[0] <= it && it < ideHiddenSince }) {
                    val files = buildList {
                        addFile(
                            name = "build.gradle.kts",
                            content = createChildTransition {
                                when {
                                    it < fileTreeHiddenSince -> buildGradleKtsBeforeSplitPane.safeGet(it - CONVENTION_PLUGINS[0])
                                    it < ideBackToNormalAgainSince -> buildGradleKtsOnSplitPane.safeGet(it - fileTreeHiddenSince)
                                    else -> buildGradleKtsInSmallIde.safeGet(it - ideBackToNormalAgainSince)
                                }
                            }
                        )
                        if (currentState >= ideIsBackToNormalSince + 1) {
                            addDirectory("buildSrc")
                            addDirectory(name = "src/main/kotlin", path = "buildSrc/src/main/kotlin")
                            addFile(
                                name = "wtf-app.gradle.kts",
                                path = "buildSrc/src/main/kotlin/wtf-app.gradle.kts",
                                content = createChildTransition { wtfApp.safeGet(it - fileTreeHiddenSince) }
                            )
                        }
                    }

                    val selectedFile = when {
                        currentState < splitPaneEnabledSince -> "build.gradle.kts"
                        currentState == splitPaneEnabledSince -> "buildSrc/src/main/kotlin/wtf-app.gradle.kts"
                        currentState >= splitPaneClosedSince -> "build.gradle.kts"
                        else -> null
                    }

                    val leftPaneFile = when {
                        currentState >= splitPaneClosedSince -> null
                        currentState >= splitPaneEnabledSince -> "build.gradle.kts"
                        else -> null
                    }

                    val rightPaneFile = when {
                        currentState >= splitPaneClosedSince -> null
                        currentState >= splitPaneEnabledSince -> "buildSrc/src/main/kotlin/wtf-app.gradle.kts"
                        else -> null
                    }

                    IDE(
                        files = files,
                        selectedFile = selectedFile,
                        leftPaneFile = leftPaneFile,
                        rightPaneFile = rightPaneFile,
                        fileTreeHidden = currentState in fileTreeHiddenSince until fileTreeRevealedSince,
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp),
                    )
                }

                FadeInOutAnimatedVisibility({ it == grimacingEmojiVisible }) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        Text(text = "😬")
                    }
                }

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                    FadeInOutAnimatedVisibility({ it == noIdeaEmojiVisible }) {
                        ProvideTextStyle(MaterialTheme.typography.h5) {
                            Text(text = """¯\_(ツ)_/¯""", modifier = Modifier.padding(top = 64.dp, end = 230.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Transition<Int>.AppDeveloperAndBuildEngineer() {
    SlideFromTopAnimatedVisibility({ it in APP_DEVELOPER_AND_BUILD_ENGINEER }) {
        Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) {
            val appDeveloperHat = when {
                currentState >= APP_DEVELOPER_AND_BUILD_ENGINEER[5] -> BASEBALL_CAP
                currentState >= APP_DEVELOPER_AND_BUILD_ENGINEER[4] -> TOP_HAT
                else -> BASEBALL_CAP
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GuyChangingHats(name = "App Developer", hat = appDeveloperHat)

                Column(
                    modifier = Modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[1] }) {
                        body1 {
                            Text {
                                append("Ships new ")
                                withPrimaryColor { append("features") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[2] }) {
                        body1 {
                            Text {
                                append("Interested in: ")
                                withPrimaryColor { append("JDK version, dependencies") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[3] }) {
                        body1 { Text("Often changes hats") }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GuyChangingHats(name = "Build Engineer", hat = TOP_HAT)

                Column(
                    modifier = Modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[1] }) {
                        body1 {
                            Text {
                                append("Maintains the ")
                                withPrimaryColor { append("build") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[2] }) {
                        body1 {
                            Text {
                                append("Interested in: ")
                                withPrimaryColor { append("plugins, tasks, configurations") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= APP_DEVELOPER_AND_BUILD_ENGINEER[3] }) {
                        body1 { Text("Seen in the wild mostly in large codebases") }
                    }
                }
            }
        }
    }
}

@Composable
fun Transition<Int>.DeclarativeGradle() {
    val buildGradleKts = buildAndRememberCodeSamples {
        val appDeveloper1 by tag()
        val appDeveloper2 by tag()
        val appDeveloper3 by tag()
        val buildEngineer1 by tag()
        val buildEngineer2 by tag()

        """
        ${buildEngineer1}plugins {
            ${appDeveloper1}`application`
            alias(libs.plugins.kotlin.jvm)${appDeveloper1}
            alias(libs.plugins.ktlint)
            alias(libs.plugins.integration.test)
        }${buildEngineer1}
        
        kotlin {
            ${appDeveloper2}jvmToolchain(24)${appDeveloper2}
        }
        
        ${appDeveloper3}dependencies {
            implementation(libs.spring.boot.web)
        }${appDeveloper3}
        
        ${buildEngineer2}tasks {
            register("mySpecialTask") {
                // ...
            }
        }${buildEngineer2}
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { this }
            .then { focus(appDeveloper1, appDeveloper2, appDeveloper3) }
            .then { unfocus() }
            .then { focus(buildEngineer1, buildEngineer2) }
            .then { unfocus() }
    }

    val ideTopPadding by animateDp {
        when {
            it >= DECLARATIVE_GRADLE[6] -> 300.dp
            else -> 32.dp
        }
    }

    FadeOutAnimatedVisibility({ it in DECLARATIVE_GRADLE }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            val textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.background)

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
            ) {
                SlideFromTopAnimatedVisibility({ DECLARATIVE_GRADLE[7] <= it }) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.primary)
                            .padding(8.dp)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProvideTextStyle(textStyle) {
                            h6 { Text { append("Software Definition") } }

                            SlideFromTopAnimatedVisibility({ DECLARATIVE_GRADLE[8] <= it }) {
                                body2 { Text { append("What needs to be built?") } }
                            }
                        }
                    }
                }

                SlideFromTopAnimatedVisibility({ DECLARATIVE_GRADLE[9] <= it }) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.secondary)
                            .padding(8.dp)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProvideTextStyle(textStyle) {
                            h6 {
                                Text { append("Build Logic") }

                                SlideFromTopAnimatedVisibility({ DECLARATIVE_GRADLE[10] <= it }) {
                                    body2 { Text { append("How to build it?") } }
                                }
                            }
                        }
                    }
                }
            }

            SlideFromBottomAnimatedVisibility({ it in DECLARATIVE_GRADLE.drop(1) }) {
                Box(contentAlignment = Alignment.Center) {
                    val files = buildList {
                        addFile(name = "build.gradle.kts", content = createChildTransition { buildGradleKts.safeGet(it - DECLARATIVE_GRADLE[1]) })
                    }
                    IDE(
                        files = files,
                        selectedFile = "build.gradle.kts",
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp),
                    )

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        SlideFromTopAnimatedVisibility({ it == DECLARATIVE_GRADLE[2] }) {
                            AppDeveloper(Modifier.padding(end = 64.dp))
                        }

                        SlideFromTopAnimatedVisibility({ it == DECLARATIVE_GRADLE[4] }) {
                            BuildEngineer(Modifier.padding(end = 64.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppDeveloper(modifier: Modifier = Modifier) {
    GuyChangingHats(modifier = modifier.scale(0.75f), hat = BASEBALL_CAP, name = "App Developer")
}

@Composable
fun BuildEngineer(modifier: Modifier = Modifier) {
    GuyChangingHats(modifier = modifier.scale(0.75f), hat = TOP_HAT, name = "Build Engineer")
}

@Composable
fun GuyChangingHats(modifier: Modifier = Modifier, name: String?, hat: Hat) {
    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            h1 { Text("😁", Modifier.offset(y = 50.dp)) }

            AnimatedContent(
                targetState = hat,
                contentAlignment = Alignment.Center,
                transitionSpec = { slideInVertically() togetherWith slideOutVertically() }
            ) { state ->
                h1 {
                    when (state) {
                        BASEBALL_CAP -> Text("🧢", Modifier.offset(x = -10.dp, y = 10.dp))
                        TOP_HAT -> Text("🎩", Modifier.offset(x = -2.dp, y = -10.dp)) // TODO military helmet
                    }
                }
            }
        }

        if (name != null) {
            h6 { Text(name) }
        }
    }
}

enum class Hat { BASEBALL_CAP, TOP_HAT }