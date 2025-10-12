package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.body2
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.code2
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.sogood

fun StoryboardBuilder.DeclarativeGradle() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withIntTransition {

            val ideState = buildIdeState(files)

            val title = when {
                currentState >= 1 -> "Declarative Gradle"
                else -> null
            }

            TitleScaffold(title) {

                ideState.IdeLayout {

                    topPanel("tree") { panelState ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            val softwareDefinitionAppears = 1
                            val buildLogicAppears = softwareDefinitionAppears + 2

                            val tree = when {
                                panelState.currentState >= softwareDefinitionAppears + 6 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                        node("kotlinJvmApplication { ... }")
                                        node("...")
                                    }
                                }
                                panelState.currentState >= softwareDefinitionAppears + 5 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                        node("kotlinJvmApplication { ... }")
                                    }
                                }
                                panelState.currentState >= softwareDefinitionAppears + 4 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                    }
                                }
                                else -> buildTree {
                                    node("Software Definition")
                                }
                            }

                            panelState.FadeInOutAnimatedVisibility({ it >= softwareDefinitionAppears }) {
                                Box(
                                    contentAlignment = Alignment.TopCenter,
                                    modifier = Modifier.fillMaxWidth().height(120.dp)
                                ) {
                                    AnimatedHorizontalTree(tree) { node ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (node.isRoot) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant)
                                                .animateContentSize()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                ProvideTextStyle(TextStyle(color = Color.White)) {
                                                    if (node.isRoot) {
                                                        h6 { Text(node.value) }

                                                        panelState.SlideFromTopAnimatedVisibility({ it >= softwareDefinitionAppears + 1 }) {
                                                            body2 { Text { append("What needs to be built?") } }
                                                        }
                                                    } else {
                                                        code2 { Text(node.value) }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            panelState.FadeInOutAnimatedVisibility({ it >= buildLogicAppears }) {
                                Column(
                                    modifier = Modifier
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colors.secondary)
                                        .padding(8.dp)
                                        .animateContentSize(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ProvideTextStyle(TextStyle(color = Color.White)) {
                                        h6 {
                                            Text { append("Build Logic") }

                                            panelState.SlideFromTopAnimatedVisibility({ it >= buildLogicAppears + 1 }) {
                                                body2 { Text { append("How to build it?") } }
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
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val normal by tag()
    val declarative by tag()

    """
    ${normal}plugins {
        `application`
        alias(libs.plugins.kotlin.jvm)
    }
    
    kotlin {
        jvmToolchain(24)
    }
    
    application {
        mainClass = "com.example.AppKt"
    }
    
    dependencies {
        implementation(libs.spring.boot.web)
    }
    
    tasks.register("mySpecialTask") {
        // ...
    }${normal}${declarative}kotlinJvmApplication {
        javaVersion = 24
    
        mainClass = "com.example.AppKt"
                
        dependencies {
            implementation(libs.spring.boot.web)
        }
    }${declarative}
    """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { hide(declarative) }
        .openPanel("tree")
        .pass(6)
        .closePanel("tree")
        .then { reveal(declarative).hide(normal) }
        .renameSelectedFile("build.gradle.dcl")
        .showImage(Res.drawable.sogood)
}