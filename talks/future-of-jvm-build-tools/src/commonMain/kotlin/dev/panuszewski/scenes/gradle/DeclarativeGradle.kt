package dev.panuszewski.scenes.gradle

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
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IdeState
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.components.addFile
import dev.panuszewski.template.AnimatedHorizontalTree
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.body2
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.buildTree
import dev.panuszewski.template.code2
import dev.panuszewski.template.h6
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.sogood

fun StoryboardBuilder.DeclarativeGradle() {
    val ideShrinkedSince = 0
    val titleChanges = ideShrinkedSince + 1
    val graphAppears = titleChanges + 1
    val ideBackToNormalSince = graphAppears + 7
    val migratedToDeclarative = ideBackToNormalSince + BUILD_GRADLE_KTS.size
    val soGoodVisible = migratedToDeclarative + 2
    val stateCount = soGoodVisible + 1

    scene(stateCount) {
        with(transition) {
            val title = when {
                currentState.toState() >= titleChanges -> "Declarative Gradle"
                else -> null
            }

            TitleScaffold(title) {

                val ideTopPadding by animateDp {
                    when {
                        it.toState(start = -1) >= ideBackToNormalSince -> 0.dp
                        it.toState(start = -1) >= ideShrinkedSince -> 300.dp
                        else -> 0.dp
                    }
                }

                FadeOutAnimatedVisibility({ it is Frame.State<*> }) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

                        val textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.background)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FadeInOutAnimatedVisibility({ it.toState() in graphAppears until ideBackToNormalSince }) {

                                val tree = when {
                                    currentState.toState() >= graphAppears + 6 -> buildTree {
                                        node("Software Definition") {
                                            node("javaLibrary { ... }")
                                            node("kotlinJvmApplication { ... }")
                                            node("...")
                                        }
                                    }
                                    currentState.toState() >= graphAppears + 5 -> buildTree {
                                        node("Software Definition") {
                                            node("javaLibrary { ... }")
                                            node("kotlinJvmApplication { ... }")
                                        }
                                    }
                                    currentState.toState() >= graphAppears + 4 -> buildTree {
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
                                                ProvideTextStyle(textStyle) {
                                                    if (node.isRoot) {
                                                        h6 { Text(node.value) }

                                                        SlideFromTopAnimatedVisibility({ it.toState() >= graphAppears + 1 }) {
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

                            FadeInOutAnimatedVisibility({ it.toState() in graphAppears + 2 until ideBackToNormalSince }) {
                                Column(
                                    modifier = Modifier
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colors.secondary)
                                        .padding(8.dp)
                                        .animateContentSize(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ProvideTextStyle(textStyle) {
                                        h6 {
                                            Text { append("Build Logic") }

                                            SlideFromTopAnimatedVisibility({ it.toState() >= graphAppears + 3 }) {
                                                body2 { Text { append("How to build it?") } }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Box(contentAlignment = Alignment.Center) {
                            val files = buildList {
                                addFile(
                                    name = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                                    path = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                                    content = createChildTransition { BUILD_GRADLE_KTS.safeGet(it.toState() - ideBackToNormalSince) })
                            }
                            IDE(
                                IdeState(
                                    files = files,
                                    selectedFile = if (currentState.toState() >= migratedToDeclarative + 1) "build.gradle.dcl" else "build.gradle.kts",
                                    enlargedFile = when {
                                        currentState.toState() >= migratedToDeclarative + 1 -> "build.gradle.dcl"
                                        currentState.toState() >= migratedToDeclarative -> "build.gradle.kts"
                                        else -> null
                                    },
                                ),
                                modifier = Modifier
                                    .padding(
                                        start = 32.dp,
                                        end = 32.dp,
                                        top = ideTopPadding,
                                        bottom = 32.dp
                                    )
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState("IDE"),
                                        animatedVisibilityScope = contextOf<AnimatedVisibilityScope>()
                                    ),
                            )

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .offset(x = 120.dp, y = -90.dp)
                                ) {
                                    FadeInOutAnimatedVisibility({ it.toState() == soGoodVisible }) {
                                        ResourceImage(
                                            remember { Res.drawable.sogood },
                                            modifier = Modifier.border(1.dp, Color(0xFFFF8A04))
                                        )
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
        .then { reveal(declarative).hide(normal) }
}