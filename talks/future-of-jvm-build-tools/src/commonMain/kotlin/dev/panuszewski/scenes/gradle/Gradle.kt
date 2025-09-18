package dev.panuszewski.scenes.gradle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IdeState
import dev.panuszewski.components.addFile
import dev.panuszewski.scenes.gradle.Hat.BASEBALL_CAP
import dev.panuszewski.scenes.gradle.Hat.TOP_HAT
import dev.panuszewski.template.Connection
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.HorizontalTree
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Stages
import dev.panuszewski.template.Text
import dev.panuszewski.template.body1
import dev.panuszewski.template.body2
import dev.panuszewski.template.buildAndRememberCodeSamples
import dev.panuszewski.template.code2
import dev.panuszewski.template.h1
import dev.panuszewski.template.h6
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withPrimaryColor
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.sogood
import kotlin.collections.iterator

private val stages = Stages()
private val lastState: Int get() = stages.lastState

private val PHASES_BAR_APPEARS = stages.registerStatesByCount(start = lastState + 1, count = 1)
private val CHARACTERIZING_PHASES = stages.registerStatesByCount(start = lastState + 1, count = 3)
private val EXPLAINING_CONFIG_EXECUTION_DIFFERENCE = stages.registerStatesByCount(start = lastState + 2, count = 5)
private val EXECUTION_BECOMES_LONG = stages.registerStatesByCount(start = lastState + 2, count = 1)
private val SHOWING_THAT_BUILD_CACHE_IS_OLD = stages.registerStatesByCount(start = lastState + 2, count = 2)
private val EXECUTION_BECOMES_SHORT = stages.registerStatesByCount(start = lastState + 1, count = 1)
private val CONFIGURATION_IS_LONG = stages.registerStatesByCount(start = lastState + 1, count = 28)
private val PHASES_BAR_DISAPPEARS = stages.registerStatesByCount(start = lastState + 2, count = 1)
private val EXTRACTING_CONVENTION_PLUGIN = stages.registerStatesByCount(start = lastState + 1, count = 9)
private val EXPLAINING_CONVENTION_PLUGINS = stages.registerStatesByCount(start = lastState + 1, count = 26)
private val SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER = stages.registerStatesByCount(lastState + 1, count = 7)
private val DECLARATIVE_GRADLE = stages.registerStatesByCount(lastState + 1, count = 19)

private val PHASES_BAR_VISIBLE = PHASES_BAR_APPEARS.first() until PHASES_BAR_DISAPPEARS.first()
private val EXECUTION_IS_LONG = EXECUTION_BECOMES_LONG.first() until EXECUTION_BECOMES_SHORT.first()
private val CONVENTION_PLUGINS = EXTRACTING_CONVENTION_PLUGIN + EXPLAINING_CONVENTION_PLUGINS

fun StoryboardBuilder.Gradle() {
    CharacterizingPhases()
    ExplainingConfigExecutionDifference()
    BuildCache()
    ConfigurationCache()
    ConventionPlugins()

//    scene(stateCount = stages.stateCount) {
//        withStateTransition {
//            SoftwareDeveloperAndBuildEngineer()
//            DeclarativeGradle()
//        }
//    }
}

@Composable
fun Transition<Int>.SoftwareDeveloperAndBuildEngineer() {
    SlideFromTopAnimatedVisibility({ it in SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER.drop(1) }) {
        Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) {
            val appDeveloperHat = when {
                currentState >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[6] -> BASEBALL_CAP
                currentState >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[5] -> TOP_HAT
                else -> BASEBALL_CAP
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GuyChangingHats(name = "Software Developer", hat = appDeveloperHat)

                Column(
                    modifier = Modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[2] }) {
                        body1 {
                            Text {
                                append("Ships new ")
                                withPrimaryColor { append("features") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[3] }) {
                        body1 {
                            Text {
                                append("Interested in: ")
                                withPrimaryColor { append("JDK version, dependencies") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[4] }) {
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
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[2] }) {
                        body1 {
                            Text {
                                append("Maintains the ")
                                withPrimaryColor { append("build") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[3] }) {
                        body1 {
                            Text {
                                append("Interested in: ")
                                withPrimaryColor { append("plugins, tasks, configurations") }
                            }
                        }
                    }
                    SlideFromTopAnimatedVisibility({ it >= SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER[4] }) {
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
        val appDeveloper4 by tag()
        val buildEngineer1 by tag()
        val buildEngineer2 by tag()
        val normal by tag()
        val declarative by tag()

        """
        ${normal}${buildEngineer1}plugins {
            ${appDeveloper1}`application`
            alias(libs.plugins.kotlin.jvm)${appDeveloper1}
        }${buildEngineer1}
        
        kotlin {
            ${appDeveloper2}jvmToolchain(24)${appDeveloper2}
        }
        
        application {
            ${appDeveloper3}mainClass = "com.example.AppKt"${appDeveloper3}
        }
        
        dependencies {
            ${appDeveloper4}implementation(libs.spring.boot.web)${appDeveloper4}
        }
        
        ${buildEngineer2}tasks.register("mySpecialTask") {
            // ...
        }${buildEngineer2}${normal}${declarative}kotlinJvmApplication {
            javaVersion = 24
        
            mainClass = "com.example.AppKt"
                    
            dependencies {
                implementation(libs.spring.boot.web)
            }
        } ${declarative}
        """
            .trimIndent()
            .toCodeSample(language = Language.Kotlin)
            .startWith { hide(declarative) }
            .then { focus(appDeveloper1, appDeveloper2, appDeveloper3, appDeveloper4) }
            .then { unfocus() }
            .then { focus(buildEngineer1, buildEngineer2) }
            .then { unfocus() }
            .then { reveal(declarative).hide(normal) }
    }

    val buildGradleKtsBeforeShrink = buildGradleKts.take(5)
    val buildGradleKtsAfterShrink = buildGradleKts.drop(4)

    val ideShrinkedSince = DECLARATIVE_GRADLE[6]
    val ideBackToNormalSince = DECLARATIVE_GRADLE[14]
    val migratedToDeclarative = ideBackToNormalSince + buildGradleKtsAfterShrink.size
    val soGoodVisible = migratedToDeclarative + 2

    val ideTopPadding by animateDp {
        when {
            it >= ideBackToNormalSince -> 0.dp
            it >= ideShrinkedSince -> 300.dp
            else -> 0.dp
        }
    }

    FadeOutAnimatedVisibility({ it in DECLARATIVE_GRADLE }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            val textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.background)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                FadeInOutAnimatedVisibility({ DECLARATIVE_GRADLE[7] <= it && it < ideBackToNormalSince }) {

                    val softwareTypes = when {
                        currentState >= DECLARATIVE_GRADLE[13] -> listOf("javaLibrary { ... }", "kotlinJvmApplication { ... }", "...")
                        currentState >= DECLARATIVE_GRADLE[12] -> listOf("javaLibrary { ... }", "kotlinJvmApplication { ... }")
                        currentState >= DECLARATIVE_GRADLE[11] -> listOf("javaLibrary { ... }")
                        else -> listOf()
                    }

                    SharedTransitionLayout {
                        AnimatedContent(
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            targetState = softwareTypes
                        ) { st ->

                            var offset by remember { mutableStateOf(Offset.Zero) }
                            val placements = remember { mutableStateMapOf<String, Rect>() }

                            Box(
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            ) {
                                HorizontalTree(
                                    root = "Software Definition",
                                    getChildren = {
                                        if (it == "Software Definition") st
                                        else emptyList()
                                    },
                                    modifier = Modifier.onPlaced { offset = it.positionInParent() }
                                ) { name ->
                                    val isRoot = name == "Software Definition"

                                    Box(
                                        modifier = Modifier
                                            .sharedElement(
                                                rememberSharedContentState(name),
                                                animatedVisibilityScope = this@AnimatedContent
                                            )
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isRoot) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant)
                                            .animateContentSize()
                                            .onPlaced { placements[name] = it.boundsInParent() },
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            ProvideTextStyle(textStyle) {
                                                if (isRoot) {
                                                    h6 { Text(name) }

                                                    SlideFromTopAnimatedVisibility({ DECLARATIVE_GRADLE[8] <= it }) {
                                                        body2 { Text { append("What needs to be built?") } }
                                                    }
                                                } else {
                                                    code2 { Text(name) }
                                                }
                                            }
                                        }
                                    }
                                }

                                val parentRect = placements["Software Definition"] ?: Rect.Zero

                                for ((childName, childRect) in placements.filterKeys { it != "Software Definition" }) {
                                    Connection(
                                        parentRect = parentRect.translate(offset),
                                        childRect = childRect.translate(offset),
                                        modifier = Modifier.sharedElement(
                                            rememberSharedContentState("connection:$childName"),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                    )
                                }
                            }
                        }
                    }

                }

                FadeInOutAnimatedVisibility({ DECLARATIVE_GRADLE[9] <= it && it < ideBackToNormalSince }) {
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
                        addFile(
                            name = if (currentState >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                            path = if (currentState >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                            content = createChildTransition {
                                when {
                                    it >= ideBackToNormalSince -> buildGradleKtsAfterShrink.safeGet(it - ideBackToNormalSince)
                                    else -> buildGradleKtsBeforeShrink.safeGet(it - DECLARATIVE_GRADLE[1])
                                }
                            })
                    }
                    IDE(
                        IdeState(
                            files = files,
                            selectedFile = if (currentState >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                            enlargedFile = when {
                                currentState >= migratedToDeclarative + 1 -> "build.gradle.dcl"
                                currentState >= migratedToDeclarative -> "build.gradle.kts"
                                else -> null
                            },
                        ),
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp),
                    )

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        SlideFromTopAnimatedVisibility({ it == DECLARATIVE_GRADLE[2] }) {
                            SoftwareDeveloper(Modifier.padding(end = 64.dp))
                        }

                        SlideFromTopAnimatedVisibility({ it == DECLARATIVE_GRADLE[4] }) {
                            BuildEngineer(Modifier.padding(end = 64.dp))
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x = 120.dp, y = -90.dp)
                        ) {
                            FadeInOutAnimatedVisibility({ it == soGoodVisible }) {
                                ResourceImage(remember { Res.drawable.sogood }, modifier = Modifier.border(1.dp, Color(0xFFFF8A04)))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoftwareDeveloper(modifier: Modifier = Modifier) {
    GuyChangingHats(modifier = modifier.scale(0.75f), hat = BASEBALL_CAP, name = "Software Developer")
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
                        TOP_HAT -> Text("🎩", Modifier.offset(x = -2.dp, y = -10.dp))
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