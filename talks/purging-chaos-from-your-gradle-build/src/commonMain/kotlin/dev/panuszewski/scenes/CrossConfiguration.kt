package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
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
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.EMPTY_SAMPLE
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideOutToBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "lib1" to DIRECTORY.initiallyHidden(),
        "lib1/build.gradle.kts" to EMPTY_SAMPLE.initiallyHidden(),
        "lib2" to DIRECTORY.initiallyHidden(),
        "lib2/build.gradle.kts" to EMPTY_SAMPLE.initiallyHidden(),
        "app1" to DIRECTORY.initiallyHidden(),
        "libre-office-installer" to DIRECTORY.initiallyHidden(),
        "buildSrc" to DIRECTORY.initiallyHidden(),
        "buildSrc/src/main/kotlin" to DIRECTORY.initiallyHidden(),
        "buildSrc/src/main/kotlin/wtf-lib.gradle.kts" to WTF_LIB_GRADLE_KTS.initiallyHidden(),
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
                        Box(Modifier.height(200.dp)) {
                            val rootProjectColor = MaterialTheme.colors.primary
                            val libraryColor = NICE_BLUE
                            val appColor = MaterialTheme.colors.secondary

                            val tree = when {
                                panelState.currentState >= 6 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                    }
                                }
                                panelState.currentState >= 5 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                        node("app1", appColor)
                                        node("libre-office-installer", appColor)
                                    }
                                }
                                panelState.currentState >= 4 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                        node("app1", appColor)
                                    }
                                }
                                panelState.currentState >= 3 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("lib1", libraryColor)
                                        node("lib2", libraryColor)
                                    }
                                }
                                panelState.currentState >= 2 -> buildTree {
                                    node("root-project", rootProjectColor) {
                                        node("lib1", libraryColor)
                                    }
                                }
                                panelState.currentState >= 1 -> buildTree {
                                    node("root-project", rootProjectColor)
                                }
                                else -> buildTree {}
                            }
                            AnimatedHorizontalTree(tree) { node ->
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
                                                it >= 7 && node.value.startsWith("lib") -> """
                                                plugins {
                                                    `wtf-lib`
                                                }
                                                """
                                                    .trimIndent()
                                                    .toCode(language = Language.KotlinDsl)
                                                it == 5 && node.value.startsWith("lib") -> buildAnnotatedString {
                                                    withColor(NICE_ORANGE) { append("lib") }
                                                    withColor(Color.White) { append(node.value.substringAfter("lib")) }
                                                }
                                                else -> buildAnnotatedString {
                                                    withColor(Color.White) { append(node.value) }
                                                }
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

private val BUILD_GRADLE_KTS = buildCodeSamples {
    val subprojectsBlock by tag()
    val javaLibrary by tag()
    val mavenPublish by tag()
    val publication by tag()
    val subprojectsFilter by tag()
    val subprojectsForEach by tag()
    val subprojectsIndent1 by tag()
    val subprojectsIndent2 by tag()
    val subprojectsIndent3 by tag()
    val subprojectsIndent4 by tag()
    val subprojectsIndent5 by tag()
    val subprojectsClosingBrace by tag()
    val commonConfig by tag()

    """
    ${subprojectsBlock}subprojects${subprojectsFilter}
        .filter { it.name.startsWith("lib")${subprojectsFilter}${subprojectsForEach}
        .forEach${subprojectsForEach} { ${javaLibrary}${commonConfig}
        ${subprojectsIndent1}    it.${subprojectsIndent1}apply(plugin = "java-library")
    ${javaLibrary}${mavenPublish}    ${subprojectsIndent2}    it.${subprojectsIndent2}apply(plugin = "maven-publish")${publication}
    
        ${subprojectsIndent3}    ${subprojectsIndent3}publishing.publications.create<MavenPublication>("library") {
        ${subprojectsIndent4}    ${subprojectsIndent4}    from(components["java"])
        ${subprojectsIndent5}    ${subprojectsIndent5}}${publication}${commonConfig}${subprojectsClosingBrace}
        }${subprojectsClosingBrace}
    ${mavenPublish}}${subprojectsBlock}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(javaLibrary, mavenPublish, publication, subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace) }
        .openPanel("tree")
        .pass()
        .revealFile("lib1")
        .revealFile("lib2")
        .closePanel("tree")
        .then { reveal(javaLibrary) }
        .then { reveal(mavenPublish) }
        .then { reveal(publication) }
        .openPanel("tree")
        .revealFile("app1")
        .closePanel("tree")
        .then { reveal(subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace).focus(subprojectsFilter) }
        .openPanel("tree")
        .revealFile("libre-office-installer")
        .then { unfocus().closePanel("tree") }
        .openInRightPane("buildSrc/src/main/kotlin/wtf-lib.gradle.kts", switchTo = true)
        .then { focus(commonConfig) }
        .then { hide(commonConfig) }
        .then { hide(subprojectsBlock) }
}

private val WTF_LIB_GRADLE_KTS = buildCodeSamples {
    val config by tag()
    val todo by tag()

    """
    ${config}plugins {
        `java-library`    
        `maven-publish`
    }
    
    publishing.publications.create<MavenPublication>("library") {
        from(components["java"])
    }
    
    ${config}${todo}// TODO${todo}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(config) }
        .hideFileTree()
        .thenTogetherWith("build.gradle.kts") { this }
        .thenTogetherWith("build.gradle.kts") { reveal(config) }
        .thenTogetherWith("build.gradle.kts") { hide(todo) }
        .then { showFileTree().closeLeftPane().hideFile("app1").hideFile("libre-office-installer").resumePanel("tree") }
        .openPanel("tree")
        .pass()
        .closePanel("tree")
        .openAgenda()
        .pass()
        .closeAgenda()
}
