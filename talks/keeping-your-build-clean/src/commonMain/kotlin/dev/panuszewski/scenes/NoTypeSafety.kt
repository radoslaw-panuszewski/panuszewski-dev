package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.NoTypeSafety() {
    scene(100) {
        withStateTransition {
            TitleScaffold("No type safety") {
                val files = buildList {
                    addFile(
                        name = "build.gradle.kts",
                        content = createChildTransition { BUILD_GRADLE_KTS.safeGet(it) }
                    )
                }

                IdeLayout {
                    ideState = IdeState(
                        files = files,
                        selectedFile = "build.gradle.kts",
                    )
                }
            }
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val kotlin by tag()

    $$"""
    $${kotlin}buildscript {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
    }

    allprojects {
        apply(plugin = "kotlin")
    }

    subprojects
        .filter { it.name.endsWith("-library") }
        .forEach { it.apply(plugin = "java-library") }

    dependencies {
        implementation(project(":first-library"))
    }

    tasks.register("sayHello") {
        doLast {
            println("lol")
        }
    }$${kotlin}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}