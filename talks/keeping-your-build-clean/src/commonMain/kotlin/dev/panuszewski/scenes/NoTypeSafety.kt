package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeStateWithMapping
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.NoTypeSafety() {
    scene(100) {
        withStateTransition {
            TitleScaffold("No type safety") {
                val ideState = buildIdeStateWithMapping(
                    files = listOf(
                        "build.gradle.kts" to BUILD_GRADLE_KTS,
                        "settings.gradle.kts" to SETTINGS_GRADLE_KTS.initiallyHidden(),
                        ".gradle" to DIRECTORY.initiallyHidden(),
                        ".gradle/libs.versions.toml" to LIBS_VERSIONS_TOML.initiallyHidden(),
                    )
                )
                
                IdeLayout {
                    this.ideState = ideState
                }
            }
        }
    }
}

private val LIBS_VERSIONS_TOML = buildCodeSamples {
    val todo by tag()
    val libraries by tag()

    """
    ${libraries}[libraries]
    mongodb-driver-sync = "org.mongodb:mongodb-driver-sync:5.6.0"
    
    ${libraries}${todo}// TODO${todo}
    """
        .trimIndent()
        .toCodeSample(language = Language.Toml)
        .startWith { hide(libraries) }
        .then { reveal(libraries) }
        .then { hide(todo) }
        .switchTo("build.gradle.kts")
}

private val SETTINGS_GRADLE_KTS = buildCodeSamples {
    val typesafeProjectAccessors by tag()

    """
    ${typesafeProjectAccessors}enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    
    ${typesafeProjectAccessors}rootProject.name = "example-project"        
    """
        .trimIndent()
        .toCodeSample()
        .startWith { hide(typesafeProjectAccessors) }
        .then { reveal(typesafeProjectAccessors) }
        .switchTo("build.gradle.kts")
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val imperativePlugin by tag()
    val declarativePlugin by tag()
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
 
    subprojects
        .filter { it.name.endsWith("-library") }
        .forEach { it.apply(plugin = "java-library") }
    
    dependencies {
        ${nonTypesafeConfiguration1}"implementation"${nonTypesafeConfiguration1}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeProjectDependency}project(":first-library")${nonTypesafeProjectDependency}${typesafeProjectDependency}projects.firstLibrary${typesafeProjectDependency})
        ${nonTypesafeConfiguration2}"implementation"${nonTypesafeConfiguration2}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeExternalDependency}"org.mongodb:mongodb-driver-sync:5.6.0"${nonTypesafeExternalDependency}${typesafeExternalDependency}libs.mongodb.driver.sync${typesafeExternalDependency})
    }
    
    tasks.register("sayHello") {
        doLast {
            println("lol")
        }
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(declarativePlugin, typesafeConfiguration1, typesafeConfiguration2, typesafeProjectDependency, typesafeExternalDependency) }
        .then { focus(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { focus(imperativePlugin) }
        .then { unfocus().hide(imperativePlugin).reveal(declarativePlugin) }
        .then { focus(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { hide(nonTypesafeConfiguration1, nonTypesafeConfiguration2).reveal(typesafeConfiguration1, typesafeConfiguration2).focusNoStyling(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { focus(nonTypesafeProjectDependency) }
        .switchTo("settings.gradle.kts")
        .then { this }
        .then { hide(nonTypesafeProjectDependency).reveal(typesafeProjectDependency).focusNoStyling(nonTypesafeProjectDependency) }
        .then { focus(nonTypesafeExternalDependency) }
        .switchTo(".gradle/libs.versions.toml")
        .then { this }
        .then { hide(nonTypesafeExternalDependency).reveal(typesafeExternalDependency).focusNoStyling(nonTypesafeExternalDependency) }
        .then { unfocus() }
}