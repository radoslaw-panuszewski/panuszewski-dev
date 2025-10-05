package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.BULLET_1

fun StoryboardBuilder.Groovy() {
    val ideShrinksVertically = 1
    val bulletpointsAppear = ideShrinksVertically + 1
    val ideExpandsVertically = bulletpointsAppear + 3
    val vomitEmojiAppears = ideExpandsVertically + BUILD_GRADLE_KTS.size - 2
    val titleChanges = vomitEmojiAppears + 1
    val ideShrinksHorizontally = titleChanges + 1
    val groovyCrossedOut = ideShrinksHorizontally + 1
    val ideExpandsHorizontally = groovyCrossedOut + 1

    scene(ideExpandsHorizontally + 1) {
        withStateTransition {
            BUILD_GRADLE_KTS.precompose()

            val title = when {
                currentState >= titleChanges -> "Kotlin DSL ❤️"
                else -> "Groovy"
            }

            TitleScaffold(title) {
                val fileName = if (currentState >= titleChanges) "build.gradle.kts" else "build.gradle"

                val ideState = IdeState(
                    files = buildList {
                        addFile(
                            name = fileName,
                            content = createChildTransition { BUILD_GRADLE_KTS.safeGet(it - ideExpandsVertically) }
                        )
                    },
                    selectedFile = fileName,
                )
                
                IdeLayout {
                    this.ideState = ideState

                    topPanel(openAt = ideShrinksVertically until ideExpandsVertically) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            RevealSequentially(since = bulletpointsAppear) {
                                stringItem("Dynamic typing")
                                stringItem("Poor IDE support")
                                stringItem("Hard to debug")
                            }
                        }
                    }

                    leftPanel(openAt = ideShrinksHorizontally until ideExpandsHorizontally) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 32.dp).align(TopStart),
                        ) {
                            h6 {
                                createChildTransition {
                                    when {
                                        it >= groovyCrossedOut -> buildAnnotatedString {
                                            append(BULLET_1)
                                            withStyle(SpanStyle(textDecoration = LineThrough, color = Color.DarkGray)) { append("Groovy") }
                                        }
                                        else -> "$BULLET_1 Groovy".annotate()
                                    }
                                }.MagicAnnotatedString()

                                Text("$BULLET_1 No type safety")
                                Text("$BULLET_1 Imperative code")
                                Text("$BULLET_1 Cross configuration")
                                Text("$BULLET_1 Mixed concerns")
                            }
                        }
                    }

                    centerEmoji(visibleAt = vomitEmojiAppears) {
                        Text(text = "🤢")
                    }
                }
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val nothing by tag()
    val groovy by tag()
    val kotlin by tag()

    $$"""
    $${nothing}$${nothing}$${groovy}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath('org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20')
        }
    }
    
    allprojects {
        apply plugin: 'org.jetbrains.kotlin.jvm'
    
        compileKotlin {
            compilerOptions {
                explicitApiMode = Strict
            }
        }
    }
    
    subprojects
        .findAll { it.name.endsWith('-library') }
        .forEach { it.apply plugin: 'java-library' }
    
    dependencies {
        implementation project(':first-library')
        implementation 'org.springframework.boot:spring-boot-starter-web:3.5.6'
    }
    
    tasks.register('sayHello') {
        doLast {
            println 'lol'
        }
    }$${groovy}$${kotlin}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
        }
    }
    
    allprojects {
        apply(plugin = "org.jetbrains.kotlin.jvm")
    
        tasks.named<KotlinCompile>("compileKotlin") {
            compilerOptions {
                explicitApiMode = Strict
            }
        }
    }
    
    subprojects
        .filter { it.name.endsWith("-library") }
        .forEach { it.apply(plugin = "java-library") }
    
    dependencies {
        "implementation"(project(":first-library"))
        "implementation"("org.springframework.boot:spring-boot-starter-web:3.5.6")
    }
    
    tasks.register("sayHello") {
        doLast {
            println("lol")
        }
    }$${kotlin}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { hide(kotlin) }
        .then { underline(groovy).focus(nothing) }
        .then { this }
        .then { unfocus().hide(groovy).reveal(kotlin).changeLanguage(Language.KotlinDsl) }
}