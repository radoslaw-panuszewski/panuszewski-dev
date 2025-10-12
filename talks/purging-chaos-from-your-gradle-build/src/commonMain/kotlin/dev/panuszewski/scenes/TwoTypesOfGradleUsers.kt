package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.GuyChangingHats
import dev.panuszewski.template.components.Hat.BASEBALL_CAP
import dev.panuszewski.template.components.Hat.TOP_HAT
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.body1
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.NICE_LIGHT_TURQUOISE
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.TwoTypesOfGradleUsers() {
    val bigEmojisAppear = 1
    val bigEmojisDisappear = bigEmojisAppear + 6
    val codeAppears = bigEmojisDisappear + 1
    val stateCount = codeAppears + BUILD_GRADLE_KTS.size

    scene(stateCount) {
        withIntTransition {
            TitleScaffold("Two types of Gradle users") {
                SlideFromTopAnimatedVisibility({ it in bigEmojisAppear until bigEmojisDisappear }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) {
                        val appDeveloperHat = when {
                            currentState >= 6 -> BASEBALL_CAP
                            currentState >= 5 -> TOP_HAT
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
                                SlideFromTopAnimatedVisibility({ it >= 2 }) {
                                    body1 {
                                        Text {
                                            append("Ships new ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("features") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it >= 3 }) {
                                    body1 {
                                        Text {
                                            append("Interested in: ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("JDK version, dependencies") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it >= 4 }) {
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
                                SlideFromTopAnimatedVisibility({ it >= 2 }) {
                                    body1 {
                                        Text {
                                            append("Maintains the ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("build") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it >= 3 }) {
                                    body1 {
                                        Text {
                                            append("Interested in: ")
                                            withColor(NICE_LIGHT_TURQUOISE) { append("plugins, tasks, configurations") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it >= 4 }) {
                                    body1 { Text("Seen in the wild mostly in large codebases") }
                                }
                            }
                        }
                    }
                }

                SlideFromBottomAnimatedVisibility({ it >= codeAppears }) {
                    Box(contentAlignment = Alignment.Center) {
                        val files = buildList {
                            addFile(
                                name = "build.gradle.kts",
                                content = createChildTransition {
                                    BUILD_GRADLE_KTS.safeGet(it - codeAppears)
                                })
                        }
                        IDE(
                            IdeState(
                                files = files,
                                selectedFile = "build.gradle.kts",
                            ),
                        )

                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            SlideFromTopAnimatedVisibility({ it == codeAppears + 1 }) {
                                SoftwareDeveloper(Modifier.padding(end = 64.dp))
                            }

                            SlideFromTopAnimatedVisibility({ it == codeAppears + 3 }) {
                                BuildEngineer(Modifier.padding(end = 64.dp))
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

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val appDeveloper1 by tag()
    val appDeveloper2 by tag()
    val appDeveloper3 by tag()
    val appDeveloper4 by tag()
    val buildEngineer1 by tag()
    val buildEngineer2 by tag()

    """
    ${buildEngineer1}plugins {
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
    }${buildEngineer2}
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { this }
        .then { focus(appDeveloper1, appDeveloper2, appDeveloper3, appDeveloper4) }
        .then { unfocus() }
        .then { focus(buildEngineer1, buildEngineer2) }
        .then { unfocus() }
}