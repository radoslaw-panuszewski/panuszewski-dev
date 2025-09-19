package dev.panuszewski.scenes.gradle

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IdeState
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.components.addFile
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.body1
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withPrimaryColor

fun StoryboardBuilder.TwoTypesOfGradleUsers() {
    val bigEmojisAppear = 1
    val bigEmojisDisappear = bigEmojisAppear + 6
    val codeAppears = bigEmojisDisappear + 1
    val stateCount = codeAppears + BUILD_GRADLE_KTS.size

    scene(stateCount) {
        with(transition) {
            TitleScaffold("Two types of Gradle users") {
                SlideFromTopAnimatedVisibility({ it.toState() in bigEmojisAppear until bigEmojisDisappear }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) {
                        val appDeveloperHat = when {
                            currentState.toState() >= 6 -> Hat.BASEBALL_CAP
                            currentState.toState() >= 5 -> Hat.TOP_HAT
                            else -> Hat.BASEBALL_CAP
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
                                SlideFromTopAnimatedVisibility({ it.toState() >= 2 }) {
                                    body1 {
                                        Text {
                                            append("Ships new ")
                                            withPrimaryColor { append("features") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it.toState() >= 3 }) {
                                    body1 {
                                        Text {
                                            append("Interested in: ")
                                            withPrimaryColor { append("JDK version, dependencies") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it.toState() >= 4 }) {
                                    body1 { Text("Often changes hats") }
                                }
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GuyChangingHats(name = "Build Engineer", hat = Hat.TOP_HAT)

                            Column(
                                modifier = Modifier.width(400.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SlideFromTopAnimatedVisibility({ it.toState() >= 2 }) {
                                    body1 {
                                        Text {
                                            append("Maintains the ")
                                            withPrimaryColor { append("build") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it.toState() >= 3 }) {
                                    body1 {
                                        Text {
                                            append("Interested in: ")
                                            withPrimaryColor { append("plugins, tasks, configurations") }
                                        }
                                    }
                                }
                                SlideFromTopAnimatedVisibility({ it.toState() >= 4 }) {
                                    body1 { Text("Seen in the wild mostly in large codebases") }
                                }
                            }
                        }
                    }
                }

                SlideFromBottomAnimatedVisibility({ it.toState() >= codeAppears }) {
                    Box(contentAlignment = Alignment.Center) {
                        val files = buildList {
                            addFile(
                                name = "build.gradle.kts",
                                content = createChildTransition {
                                    BUILD_GRADLE_KTS.safeGet(it.toState() - codeAppears)
                                })
                        }
                        IDE(
                            IdeState(
                                files = files,
                                selectedFile = "build.gradle.kts",
                            ),
                            modifier = Modifier
                                .padding(start = 32.dp, end = 32.dp, top = 0.dp, bottom = 32.dp)
                        )

                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            SlideFromTopAnimatedVisibility({ it.toState() == codeAppears + 1 }) {
                                SoftwareDeveloper(Modifier.padding(end = 64.dp))
                            }

                            SlideFromTopAnimatedVisibility({ it.toState() == codeAppears + 3 }) {
                                BuildEngineer(Modifier.padding(end = 64.dp))
                            }
                        }
                    }
                }
            }
        }
    }
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