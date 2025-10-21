package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.Language.KotlinDsl
import dev.panuszewski.components.Agenda
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.RoundedTreeNode
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.LocalIdeColors

fun StoryboardBuilder.NoTypeSafety() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "settings.gradle.kts" to SETTINGS_GRADLE_KTS.initiallyHidden(),
        ".gradle" to DIRECTORY.initiallyHidden(),
        ".gradle/libs.versions.toml" to LIBS_VERSIONS_TOML.initiallyHidden(),
    )
    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withIntTransition {
            val ideState = buildIdeState(files, initialTitle = "No type safety")

            TitleScaffold(ideState.currentState.title) {
                ideState.IdeLayout {

                    topPanel("plugin-marker") { panelState ->
                        val tree = buildTree {
                            node("plugin-usage") {
                                node("plugin-marker")
                            }
                        }

                        AnimatedHorizontalTree(tree) { node ->
                            RoundedTreeNode(node) {
                                when (it) {
                                    "plugin-usage" -> {
                                        """
                                        plugins {
                                            id("<pluginId>") version "<version>"
                                        }
                                        """.trimIndent().toCode(KotlinDsl)
                                    }
                                    "plugin-marker" -> {
                                        "<pluginId>:<pluginId>.gradle.plugin:<version>".annotate()
                                    }
                                    else -> it.annotate()
                                }
                            }
                        }
                    }

                    leftPanel("agenda") { panelState ->
                        panelState.Agenda {
                            item("Groovy", crossedOutSince = 0)
                            item("No type safety", crossedOutSince = 1)
                            item("Imperative code")
                            item("Cross configuration")
                            item("Mixed concerns")
                        }
                    }
                }
            }
        }
    }
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
        ${nonTypesafePlugin}id("org.jetbrains.kotlin.jvm") version "2.2.20"${nonTypesafePlugin}${typesafePlugin}alias(libs.plugins.kotlin.jvm)${typesafePlugin}
    }${declarativePlugin}

    dependencies {
        ${nonTypesafeConfiguration1}"implementation"${nonTypesafeConfiguration1}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeProjectDependency}project(":sub-project")${nonTypesafeProjectDependency}${typesafeProjectDependency}projects.subProject${typesafeProjectDependency})
        ${nonTypesafeConfiguration2}"implementation"${nonTypesafeConfiguration2}${typesafeConfiguration2}implementation${typesafeConfiguration2}(${nonTypesafeExternalDependency}"org.springframework.boot:spring-boot-starter-web:3.5.6"${nonTypesafeExternalDependency}${typesafeExternalDependency}libs.spring.boot.web${typesafeExternalDependency})
    }
    """
        .trimIndent()
        .toCodeSample(language = KotlinDsl)
        .startWith { hide(declarativePlugin, typesafePlugin, typesafeConfiguration1, typesafeConfiguration2, typesafeProjectDependency, typesafeExternalDependency) }
//        .openPanel("plugin-marker")
//        .pass(10)
        // plugins block
        .then { focusNoScroll(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { focus(imperativePlugin) }
        .then { unfocus().hide(imperativePlugin).reveal(declarativePlugin) }
        .then { focusNoScroll(nonTypesafeConfiguration1, nonTypesafeConfiguration2) }
        .then { hide(nonTypesafeConfiguration1, nonTypesafeConfiguration2).reveal(typesafeConfiguration1, typesafeConfiguration2).unfocus() }
        // version catalog
        .then { focusNoScroll(nonTypesafePlugin, nonTypesafeExternalDependency) }
        .changeTitle("Version catalog")
        .switchTo(".gradle/libs.versions.toml")
        .then { this }
        .then { hide(nonTypesafePlugin, nonTypesafeExternalDependency).reveal(typesafePlugin, typesafeExternalDependency).unfocus() }
        // project accessors
        .then { focusNoScroll(nonTypesafeProjectDependency) }
        .changeTitle("Typesafe project accessors")
        .switchTo("settings.gradle.kts")
        .then { this }
        .then { hide(nonTypesafeProjectDependency).reveal(typesafeProjectDependency).unfocus() }
        .changeTitle("Now we're typesafe!")
        // agenda
        .openAgenda()
        .pass()
        .closeAgenda()
}

private val LIBS_VERSIONS_TOML = buildCodeSamples {
    val todo by tag()
    val catalog by tag()

    """
    ${catalog}[plugins]
    kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version = "2.2.20" }
    
    [libraries]
    spring-boot-web = "org.springframework.boot:spring-boot-starter-web:3.5.6"

    ${catalog}${todo}// TODO${todo}
    """
        .trimIndent()
        .toCodeSample(language = Language.Toml)
        .startWith { hide(catalog) }
        .then { reveal(catalog) }
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
        .then { revealAndFocus(typesafeProjectAccessors) }
        .then { unfocus() }
        .switchTo("build.gradle.kts")
}