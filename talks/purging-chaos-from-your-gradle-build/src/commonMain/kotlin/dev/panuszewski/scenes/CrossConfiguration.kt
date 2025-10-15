package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.Agenda
import dev.panuszewski.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.SlideOutToBottomAnimatedVisibility
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.NICE_PINK
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to APP_BUILD_GRADLE_KTS
    )

    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        val intTransition = transition.createChildTransition { it.toState() }

        val ideState = intTransition.buildIdeState(
            files = files,
            initialTitle = "Cross configuration"
        )

        TitleScaffold(ideState.currentState.title) {

            transition.SlideOutToBottomAnimatedVisibility({ it != Frame.End }) {
                ideState.IdeLayout {
                    adaptiveTopPanel("tree") { panelState ->
                        val panelHeight by panelState.animateDp { if (it < 3) 200.dp else 400.dp }

                        Box(Modifier.height(panelHeight)) {
                            val rootProjectColor = MaterialTheme.colors.primary
                            val libraryColor = NICE_BLUE
                            val appColor = MaterialTheme.colors.secondary
                            val buildSrcColor = NICE_PINK
                            val buildLogicColor = NICE_GREEN
                            val highlightColor = NICE_ORANGE
                            val neutralColor = Color.Gray

                            val mainBuildTree = when {
                                panelState.currentState >= 15 -> buildTree {
                                    val wtfLib = reusableNode("lib-convention", libraryColor)

                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                        node("lib1", libraryColor) {
                                            node(wtfLib)
                                        }
                                        node("lib2", libraryColor) {
                                            node(wtfLib)
                                        }
                                        node("librus")
                                    }
                                }
                                panelState.currentState == 14 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                        node("librus")
                                    }
                                }
                                panelState.currentState == 13 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                        node("lib1", highlightColor)
                                        node("lib2", highlightColor)
                                        node("librus", highlightColor)
                                    }
                                }
                                panelState.currentState == 12 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                        node("lib1", highlightColor)
                                        node("lib2", highlightColor)
                                    }
                                }
                                panelState.currentState == 10 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", highlightColor)
                                        node("lib1", highlightColor)
                                        node("lib2", highlightColor)
                                    }
                                }
                                panelState.currentState >= 5 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                    }
                                }
                                panelState.currentState >= 3 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("app", appColor)
                                    }
                                }
                                panelState.currentState >= 1 -> buildTree {
                                    node("app", rootProjectColor)
                                }
                                else -> buildTree {}
                            }

                            AnimatedHorizontalTree(mainBuildTree) { node ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(width = 2.dp, color = node.color ?: Color.Unspecified, shape = RoundedCornerShape(8.dp))
                                        .background(LocalIdeColors.current.paneBackground)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.animateContentSize(tween(durationMillis = 300, delayMillis = 300))
                                    ) {
                                        panelState.createChildTransition {
                                            when {
                                                it >= 22 && node.value == "lib-convention" -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }
                                                it == 21 && node.value.matches("""lib\d+""".toRegex()) -> LIB_BUILD_GRADLE_KTS[2].String()
                                                it >= 20 && node.value == "root-project" -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }
                                                it >= 19 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[8].String()
                                                it >= 18 && node.value == "lib-convention" -> LIB_CONVENTION[2].String()
                                                it >= 18 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[7].String()
                                                it >= 17 && node.value == "lib-convention" -> LIB_CONVENTION[1].String()
                                                it >= 17 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[6].String()
                                                it == 16 && node.value == "lib-convention" -> LIB_CONVENTION[0].String()
                                                it >= 14 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[5].String()
                                                it >= 12 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[4].String()
                                                it == 11 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[3].String()
                                                it == 10 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[2].String()
                                                it in 8 until 10 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[1].String()
                                                it == 7 && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[0].String()
                                                it == 10 && node.value == "app" -> "app ❌".annotate()
                                                it == 13 && node.value == "librus" -> "librus ❌".annotate()
                                                it in listOf(10, 12, 13) && node.value.matches("""lib\d+""".toRegex()) -> "${node.value} ✅".annotate()
                                                it == 8 && node.value.contains("lib") -> LIB_BUILD_GRADLE_KTS[1].String()
                                                it in 6 until 8 && node.value.contains("""lib\d+""".toRegex()) -> LIB_BUILD_GRADLE_KTS[0].String()
                                                it in 2 until 4 && node.value == "app" -> APP_BUILD_GRADLE_KTS[4].String()
                                                else -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }
                                            }
                                        }.MagicAnnotatedString(Modifier.padding(8.dp), split = { it.splitByChars() })
                                    }
                                }
                            }
                        }
                    }

                    leftPanel("agenda") { panelState ->
                        panelState.Agenda {
                            item("Groovy", crossedOutSince = 0)
                            item("No type safety", crossedOutSince = 0)
                            item("Imperative code", crossedOutSince = 0)
                            item("Cross configuration", crossedOutSince = 1)
                            item("Mixed concerns")
                        }
                    }
                }
            }
        }
    }
}

private val ROOT_BUILD_GRADLE_KTS = buildCodeSamples {
    val subprojectsBlock by tag()
    val subprojectsFilter by tag()
    val subprojectsForEach by tag()
    val subprojectsIndent1 by tag()
    val subprojectsIndent2 by tag()
    val subprojectsIndent3 by tag()
    val subprojectsIndent4 by tag()
    val subprojectsIndent5 by tag()
    val subprojectsClosingBrace by tag()
    val subprojectsKeyword by tag()
    val subprojectsConfig by tag()
    val todo by tag()
    val nothingLeft by tag()

    """
    ${subprojectsBlock}${subprojectsKeyword}subprojects${subprojectsKeyword}${subprojectsFilter}
        .filter { it.name.startsWith("lib")${subprojectsFilter}${subprojectsForEach}
        .forEach${subprojectsForEach} { ${subprojectsConfig}
        ${subprojectsIndent1}    it.${subprojectsIndent1}apply(plugin = "java-library")
        ${subprojectsIndent2}    it.${subprojectsIndent2}apply(plugin = "maven-publish")
    
        ${subprojectsIndent3}    ${subprojectsIndent3}publishing.publications.create("lib") {
        ${subprojectsIndent4}    ${subprojectsIndent4}    from(components["java"])
        ${subprojectsIndent5}    ${subprojectsIndent5}}${subprojectsConfig}${todo}
        // TODO          ${todo}${subprojectsClosingBrace}
        }${subprojectsClosingBrace}
    }${subprojectsBlock}${nothingLeft}// nothing left to configure here :)${nothingLeft}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(nothingLeft, subprojectsConfig, subprojectsFilter, subprojectsForEach, subprojectsClosingBrace, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5) }
        .then { hide(todo).reveal(subprojectsConfig) }
        .then { focus(subprojectsKeyword) }
        .then { unfocus() }
        .then { reveal(subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace).focus(subprojectsFilter) }
        .then { unfocus() }
        .then { focus(subprojectsConfig) }
        .then { unfocus().hide(subprojectsConfig) }
        .then { hide(subprojectsBlock).reveal(nothingLeft) }
}

private val APP_BUILD_GRADLE_KTS = buildCodeSamples {
    val emptyLine by tag()

    """
    plugins {
        `app-convention`
    }${emptyLine}
    ${emptyLine}
    dependencies {
        implementation(projects.subProject)
        implementation(libs.spring.boot.web)
    }
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
        .openPanel("tree")
        .pass()
        .hideIde()
        .then { hide(emptyLine) }
        .pass(19)
}

private val LIB_BUILD_GRADLE_KTS = buildCodeSamples {
    val libConfig by tag()
    val restOfTheConfig by tag()
    val conventionPluginUsage by tag()

    """
    ${libConfig}plugins {
        `java-library`
        `maven-publish`
    }
    publishing.publications.create("lib") {
        from(components["java"])
    }
    ${libConfig}${conventionPluginUsage}plugins {
        `lib-convention`
    }${conventionPluginUsage}${restOfTheConfig}// rest of the config...${restOfTheConfig}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(conventionPluginUsage) }
        .then { hide(libConfig) }
        .then { hide(restOfTheConfig).reveal(conventionPluginUsage) }
}

private val LIB_CONVENTION = buildCodeSamples {
    val libConfig by tag()
    val todo by tag()

    """
    ${libConfig}plugins {
        `java-library`
        `maven-publish`
    }
    publishing.publications.create("lib") {
        from(components["java"])
    }${libConfig}${todo}// TODO ${todo}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(libConfig) }
        .then { hide(todo).reveal(libConfig).focus(libConfig) }
        .then { unfocus() }
}
