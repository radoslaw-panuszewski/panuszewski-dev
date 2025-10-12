package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.body2
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.code2
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.sogood

fun StoryboardBuilder.DeclarativeGradle() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS
    )

    val ideShrinkedSince = 0
    val titleChanges = ideShrinkedSince + 1
    val graphAppears = titleChanges + 1
    val ideBackToNormalSince = graphAppears + 7
    val migratedToDeclarative = ideBackToNormalSince + BUILD_GRADLE_KTS.size
    val soGoodVisible = migratedToDeclarative + 2
    val stateCount = soGoodVisible + 1

    scene(stateCount) {
        withIntTransition {

            val ideState = buildIdeState(files)

            val title = when {
                currentState >= titleChanges -> "Declarative Gradle"
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

                            val tree = when {
                                panelState.currentState >= 6 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                        node("kotlinJvmApplication { ... }")
                                        node("...")
                                    }
                                }
                                panelState.currentState >= 5 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                        node("kotlinJvmApplication { ... }")
                                    }
                                }
                                panelState.currentState >= 4 -> buildTree {
                                    node("Software Definition") {
                                        node("javaLibrary { ... }")
                                    }
                                }
                                else -> buildTree {
                                    node("Software Definition")
                                }
                            }

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

                                                    panelState.SlideFromTopAnimatedVisibility({ it >= 1 }) {
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

                            panelState.FadeInOutAnimatedVisibility({ it >= 2 }) {
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

                                            panelState.SlideFromTopAnimatedVisibility({ it >= 3 }) {
                                                body2 { Text { append("How to build it?") } }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
//
//                Box(contentAlignment = Alignment.Center) {
//                    val files = buildList {
//                        addFile(
//                            name = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
//                            path = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
//                            content = createChildTransition { BUILD_GRADLE_KTS.safeGet(it.toState() - ideBackToNormalSince) })
//                    }
//                    IDE(
//                        IdeState(
//                            files = files,
//                            selectedFile = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
//                            enlargedFile = when {
//                                currentState.toState() >= migratedToDeclarative + 1 -> "build.gradle.dcl"
//                                currentState.toState() >= migratedToDeclarative -> "build.gradle.kts"
//                                else -> null
//                            },
//                        ),
//                        modifier = Modifier
//                            .padding(top = ideTopPadding)
//                            .sharedElement(
//                                sharedContentState = rememberSharedContentState("IDE"),
//                                animatedVisibilityScope = contextOf<AnimatedVisibilityScope>()
//                            ),
//                    )
//
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.CenterStart
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(100.dp)
//                                .offset(x = 120.dp, y = -90.dp)
//                        ) {
//                            FadeInOutAnimatedVisibility({ it.toState() == soGoodVisible }) {
//                                ResourceImage(
//                                    remember { Res.drawable.sogood },
//                                    modifier = Modifier.border(1.dp, Color(0xFFFF8A04))
//                                )
//                            }
//                        }
//                    }
//                }
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
}