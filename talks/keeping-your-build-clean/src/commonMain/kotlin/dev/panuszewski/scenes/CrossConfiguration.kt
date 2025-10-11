package dev.panuszewski.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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
import dev.panuszewski.template.theme.NICE_ORANGE

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
                    adaptiveTopPanel("tree") { panelState ->
                        Box(Modifier.height(150.dp)) {
                            val primaryColor = MaterialTheme.colors.primary
                            val secondaryColor = MaterialTheme.colors.secondary

                            val tree = when {
                                panelState.currentState >= 4 -> buildTree {
                                    node("root-project", primaryColor) {
                                        node("first-library")
                                        node("second-library")
                                        node("app", NICE_ORANGE)
                                    }
                                }
                                panelState.currentState >= 3 -> buildTree {
                                    node("root-project", primaryColor) {
                                        node("first-library")
                                        node("second-library")
                                    }
                                }
                                panelState.currentState >= 2 -> buildTree {
                                    node("root-project", primaryColor) {
                                        node("first-library")
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
                                        .background(node.color ?: secondaryColor)
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
        .filter { it.name.endsWith("-library")${subprojectsFilter}${subprojectsForEach}
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
        .pass(3)
        .closePanel("tree")
        .then { reveal(subprojectsBlock) }
        .then { reveal(javaLibrary) }
        .then { reveal(mavenPublish) }
        .then { reveal(publication) }
        .openPanel("tree")
        .pass()
        .closePanel("tree")
        .then { reveal(subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace).focus(subprojectsFilter) }
        .then { unfocus() }
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