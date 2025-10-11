package dev.panuszewski.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "first-library" to DIRECTORY.initiallyHidden(),
        "first-library/build.gradle.kts" to FIRST_LIBRARY_BUILD_GRADLE_KTS.initiallyHidden(),
        "second-library" to DIRECTORY.initiallyHidden(),
        "second-library/build.gradle.kts" to SECOND_LIBRARY_BUILD_GRADLE_KTS.initiallyHidden(),
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withStateTransition {

            val ideState = buildIdeState(
                files = files,
                initialTitle = "Cross configuration"
            )

            TitleScaffold(ideState.currentState.title) {

                ideState.IdeLayout {
                    topPanel("tree") { panelState ->
                        val primaryColor = MaterialTheme.colors.primary
                        val secondaryColor = MaterialTheme.colors.secondary

                        val tree = when {
                            panelState.currentState >= 4 -> buildTree {
                                node("root-project", primaryColor) {
                                    node("library-1")
                                    node("library-2")
                                    node("app")
                                }
                            }
                            panelState.currentState >= 3 -> buildTree {
                                node("root-project", primaryColor) {
                                    node("library-1")
                                    node("library-2")
                                }
                            }
                            panelState.currentState >= 2 -> buildTree {
                                node("root-project", primaryColor) {
                                    node("library-1")
                                }
                            }
                            panelState.currentState >= 1 -> buildTree {
                                node("root-project", primaryColor)
                            }
                            else -> buildTree {}
                        }
                        AnimatedHorizontalTree(tree) { node ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(node.color ?: MaterialTheme.colors.secondary)
                            ) {
                                ProvideTextStyle(TextStyle(color = Color.White)) {
                                    Text(text = node.value, modifier = Modifier.padding(8.dp))
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
    val pluginsBlock by tag()
    val subprojectsBlock by tag()
    val javaLibrary by tag()
    val kotlinJvmBlock by tag()

    """
    ${pluginsBlock}plugins {
        alias(libs.plugins.kotlin.jvm)
    }
        
    ${pluginsBlock}${subprojectsBlock}subprojects { ${javaLibrary}
        apply(plugin = "java-library")
    ${javaLibrary}${kotlinJvmBlock}    apply(plugin = libs.plugins.kotlin.jvm.get().pluginId)
    ${kotlinJvmBlock}}${subprojectsBlock}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(pluginsBlock, subprojectsBlock, javaLibrary, kotlinJvmBlock) }
        .openPanel("tree")
        .pass(3)
        .closePanel("tree")
        .then { reveal(subprojectsBlock) }
        .then { reveal(javaLibrary) }
        .then { reveal(kotlinJvmBlock).reveal(pluginsBlock) }
        .openPanel("tree")
        .pass()
        .closePanel("tree")
}

private val FIRST_LIBRARY_BUILD_GRADLE_KTS = buildCodeSamples {
    """
    
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}

private val SECOND_LIBRARY_BUILD_GRADLE_KTS = buildCodeSamples {
    """
        
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}