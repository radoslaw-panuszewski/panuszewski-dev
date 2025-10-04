package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
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
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeStateWithMapping
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.BULLET_1

fun StoryboardBuilder.NoTypeSafety() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "settings.gradle.kts" to SETTINGS_GRADLE_KTS.initiallyHidden(),
        ".gradle" to DIRECTORY.initiallyHidden(),
        ".gradle/libs.versions.toml" to LIBS_VERSIONS_TOML.initiallyHidden(),
    )

    val topPanelOpens = calculateTotalStates(files) - 1
    val noTypeSafetyCrossedOut = topPanelOpens + 1
    val topPanelCloses = noTypeSafetyCrossedOut + 1

    scene(topPanelCloses + 1) {
        withStateTransition {
            TitleScaffold("No type safety") {
                IDE_STATE = buildIdeStateWithMapping(files)

                IdeLayout {
                    leftPanel(openAt = topPanelOpens until topPanelCloses) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 32.dp).align(TopStart),
                        ) {
                            h6 {
                                Text {
                                    append(BULLET_1)
                                    withStyle(SpanStyle(textDecoration = LineThrough, color = Color.DarkGray)) { append("Groovy") }
                                }
                                createChildTransition {
                                    when {
                                        it >= noTypeSafetyCrossedOut -> buildAnnotatedString {
                                            append(BULLET_1)
                                            withStyle(SpanStyle(textDecoration = LineThrough, color = Color.DarkGray)) { append("No type safety") }
                                        }
                                        else -> "$BULLET_1 No type safety".annotate()
                                    }
                                }.MagicAnnotatedString()

                                Text("$BULLET_1 Imperative code")
                                Text("$BULLET_1 Cross configuration")
                                Text("$BULLET_1 Mixed concerns")
                            }
                        }
                    }
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
    mongodb-driver-sync = "org.springframework.boot:spring-boot-starter-web:3.5.6"
    
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
    val nonTypesafePlugin by tag()
    val typesafePlugin by tag()
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
        ${nonTypesafePlugin}id("org.jetbrains.kotlin.jvm")${nonTypesafePlugin}${typesafePlugin}alias(libs.plugins.kotlin.jvm)${typesafePlugin}
    }${declarativePlugin}
    
    dependencies {
        ${nonTypesafeConfiguration1}"implementation"${nonTypesafeConfiguration1}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeProjectDependency}project(":first-library")${nonTypesafeProjectDependency}${typesafeProjectDependency}projects.firstLibrary${typesafeProjectDependency})
        ${nonTypesafeConfiguration2}"implementation"${nonTypesafeConfiguration2}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeExternalDependency}"org.springframework.boot:spring-boot-starter-web:3.5.6"${nonTypesafeExternalDependency}${typesafeExternalDependency}libs.spring.boot.web${typesafeExternalDependency})
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(declarativePlugin, typesafePlugin, typesafeConfiguration1, typesafeConfiguration2, typesafeProjectDependency, typesafeExternalDependency) }
        .then { focusNoScroll(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { unfocus() }
        .then { focus(imperativePlugin) }
        .then { unfocus().hide(imperativePlugin).reveal(declarativePlugin) }
        .then { focusNoScroll(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { hide(nonTypesafeConfiguration1, nonTypesafeConfiguration2).reveal(typesafeConfiguration1, typesafeConfiguration2).unfocus() }
        .then { focusNoScroll(nonTypesafeProjectDependency) }
        .switchTo("settings.gradle.kts")
        .then { this }
        .then { hide(nonTypesafeProjectDependency).reveal(typesafeProjectDependency).unfocus() }
        .then { focusNoScroll(nonTypesafePlugin, nonTypesafeExternalDependency) }
        .switchTo(".gradle/libs.versions.toml")
        .then { this }
        .then { hide(nonTypesafePlugin, nonTypesafeExternalDependency).reveal(typesafePlugin, typesafeExternalDependency).unfocus() }
        .then { unfocus() }
}