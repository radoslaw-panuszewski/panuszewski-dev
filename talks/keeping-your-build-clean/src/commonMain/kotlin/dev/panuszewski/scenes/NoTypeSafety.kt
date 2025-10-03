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
                    addFile(
                        name = "settings.gradle.kts",
                        content = createChildTransition { SETTINGS_GRADLE_KTS.safeGet(it) }
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

private val SETTINGS_GRADLE_KTS = buildCodeSamples {
    val typesafeProjectAccessors by tag()

    """
    rootProject.name = "example-project"    
    
    ${typesafeProjectAccessors}enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")${typesafeProjectAccessors}
    """
        .trimIndent()
        .toCodeSample()
        .startWith { hide(typesafeProjectAccessors) }
        .then { reveal(typesafeProjectAccessors) }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val imperativePlugin by tag()
    val declarativePlugin by tag()
    val nonTypesafeTask by tag()
    val typesafeTask by tag()
    val nonTypesafeConfiguration1 by tag()
    val nonTypesafeConfiguration2 by tag()
    val typesafeConfiguration1 by tag()
    val typesafeConfiguration2 by tag()
    val nonTypesafeProjectDependency by tag()
    val typesafeProjectDependency by tag()
    val nonTypesafeExternalDependency by tag()
    val typesafeExternalDependency by tag()

    """
    ${imperativePlugin}buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
        }
    }
    
    apply(plugin = "org.jetbrains.kotlin.jvm")${imperativePlugin}${declarativePlugin}plugins {
        id("org.jetbrains.kotlin.jvm")
    }${declarativePlugin}

    tasks.${nonTypesafeTask}named<KotlinCompile>("compileKotlin")${nonTypesafeTask}${typesafeTask}compileKotlin${typesafeTask} {
        compilerOptions {
            explicitApiMode = Strict
        }
    }
    
    subprojects
        .filter { it.name.endsWith("-library") }
        .forEach { it.apply(plugin = "java-library") }
    
    dependencies {
        ${nonTypesafeConfiguration1}"implementation"${nonTypesafeConfiguration1}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeProjectDependency}project(":first-library")${nonTypesafeProjectDependency})
        ${nonTypesafeConfiguration2}"implementation"${nonTypesafeConfiguration2}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeExternalDependency}"org.mongodb:mongodb-driver-sync:5.6.0"${nonTypesafeExternalDependency})
    }
    
    tasks.register("sayHello") {
        doLast {
            println("lol")
        }
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(declarativePlugin, typesafeTask, typesafeConfiguration1, typesafeConfiguration2) }
        .then { focus(nonTypesafeTask) }
        .then { focus(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { focus(nonTypesafeProjectDependency) }
        .then { focus(nonTypesafeExternalDependency) }
        .then { unfocus() }
        .then { focus(imperativePlugin) }
        .then { unfocus().hide(imperativePlugin).reveal(declarativePlugin) }
        .then { focus(nonTypesafeTask) }
        .then { unfocus().hide(nonTypesafeTask).reveal(typesafeTask) }
        .then { focus(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { hide(nonTypesafeConfiguration1, nonTypesafeConfiguration2).reveal(typesafeConfiguration1, typesafeConfiguration2).focusNoStyling(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
}