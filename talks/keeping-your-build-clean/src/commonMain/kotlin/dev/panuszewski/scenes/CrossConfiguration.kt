package dev.panuszewski.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
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
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.CrossConfiguration() {
    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "lib1" to DIRECTORY.initiallyHidden(),
        "lib2" to DIRECTORY.initiallyHidden(),
        "app1" to DIRECTORY.initiallyHidden(),
        "libre-office-installer" to DIRECTORY.initiallyHidden(),
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
                    adaptiveTopPanel("tree") { panelState ->
                        Box(Modifier.height(200.dp)) {
                            val rootProjectColor = MaterialTheme.colors.primary
                            val libraryColor = NICE_BLUE
                            val appColor = MaterialTheme.colors.secondary

                            val tree = when {
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
                                        .background(node.color ?: Color.Unspecified)
                                ) {
                                    ProvideTextStyle(TextStyle(color = Color.White)) {
                                        Text(Modifier.padding(8.dp)) {
                                            if (panelState.currentState >= 5 && node.value.startsWith("lib")) {
                                                withColor(NICE_ORANGE) { append("lib") }
                                                append(node.value.substringAfter("lib"))
                                            } else {
                                                append(node.value)
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

    """
    ${subprojectsBlock}subprojects${subprojectsFilter}
        .filter { it.name.startsWith("lib")${subprojectsFilter}${subprojectsForEach}
        .forEach${subprojectsForEach} { ${javaLibrary}
        ${subprojectsIndent1}    it.${subprojectsIndent1}apply(plugin = "java-library")
    ${javaLibrary}${mavenPublish}    ${subprojectsIndent2}    it.${subprojectsIndent2}apply(plugin = "maven-publish")${publication}
    
        ${subprojectsIndent3}    ${subprojectsIndent3}publishing.publications.create<MavenPublication>("library") {
        ${subprojectsIndent4}    ${subprojectsIndent4}    from(components["java"])
        ${subprojectsIndent5}    ${subprojectsIndent5}}${publication}${subprojectsClosingBrace}
        }${subprojectsClosingBrace}
    ${mavenPublish}}${subprojectsBlock}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(subprojectsBlock, javaLibrary, mavenPublish, publication, subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace) }
        .openPanel("tree")
        .pass()
        .revealFile("lib1")
        .revealFile("lib2")
        .closePanel("tree")
        .then { reveal(subprojectsBlock) }
        .then { reveal(javaLibrary) }
        .then { reveal(mavenPublish) }
        .then { reveal(publication) }
        .openPanel("tree")
        .revealFile("app1")
        .closePanel("tree")
        .then { reveal(subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace).focus(subprojectsFilter) }
        .then { unfocus().openPanel("tree") }
        .revealFile("libre-office-installer")
        .then { focus(subprojectsFilter) }
        .then { unfocus().closePanel("tree") }
}