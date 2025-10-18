package dev.panuszewski.scenes

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.Agenda
import dev.panuszewski.extensions.JustName
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.SlideFromLeftAnimatedVisibility
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
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

    val (
        ideShrinks,
        treeAppears,
        appExpands,
        appMovesUnderRootProject,
        appShrinks,
        libsAppear,
        libsExpand,
        rootProjectExpands,
        crossConfigAddedToRootProject,
        libsShrink,
        subprojectsHighlighted,
        resetAfterSubprojectsHighlighted,
        subprojectsFilterHighlighted,
        librusAppears,
        resetAfterSubprojectsFilterHighlighted,
        titleChangesToSolution,
        libConventionAppears,
        commonConfigMovedToLibConvention,
        rootProjectChangedToNothingToConfigure,
        rootProjectShrinks,
        libConventionShrinks,
        libAppliesConvention,
        appConventionAppears,
        appAppliesConvention,
        agendaOpens,
        agendaItemCrossedOut,
        agendaCloses,
        totalStates
    ) = subsequentNumbers()

    scene(totalStates) {
        withIntTransition {
            val ideState = buildIdeState(files)

            val title = when {
                currentState >= titleChangesToSolution -> "Solution: Convention plugins again!"
                else -> "Issue: Cross configuration"
            }

            TitleScaffold(title) {

                ideState.IdeLayout {
                    adaptiveTopPanel("tree") {
                        Box(Modifier.height(if (currentState < 3) 200.dp else 800.dp)) {
                            Row {
                                LookaheadScope {
                                    SlideFromLeftAnimatedVisibility({ it in agendaOpens until agendaCloses }) {
                                        Agenda(Modifier.width(225.dp)) {
                                            item("Groovy", crossedOutSince = 0)
                                            item("No type safety", crossedOutSince = 0)
                                            item("Imperative code", crossedOutSince = 0)
                                            item("Cross configuration", crossedOutSince = agendaItemCrossedOut)
                                            item("Mixed concerns")
                                        }
                                    }

                                    val rootProjectColor = MaterialTheme.colors.primary
                                    val libraryColor = NICE_BLUE
                                    val appColor = MaterialTheme.colors.secondary
                                    val buildSrcColor = NICE_PINK
                                    val buildLogicColor = NICE_GREEN
                                    val highlightColor = NICE_ORANGE
                                    val neutralColor = Color.Gray

                                    val mainBuildTree = when {
                                        currentState >= appConventionAppears -> buildTree {
                                            val appConvention = reusableNode("app-convention", appColor)
                                            val libraryConvention = reusableNode("lib-convention", libraryColor)

                                            node("root-project", rootProjectColor) {
                                                node("app", appColor) {
                                                    node(appConvention)
                                                }
                                                node("lib1", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                                node("lib2", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                            }
                                        }
                                        currentState >= rootProjectShrinks -> buildTree {
                                            val libraryConvention = reusableNode("lib-convention", libraryColor)

                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                                node("lib2", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                            }
                                        }
                                        currentState >= libConventionAppears -> buildTree {
                                            val libraryConvention = reusableNode("lib-convention", libraryColor)

                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                                node("lib2", libraryColor) {
                                                    node(libraryConvention)
                                                }
                                                node("librus")
                                            }
                                        }
                                        currentState >= resetAfterSubprojectsFilterHighlighted -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", libraryColor)
                                                node("lib2", libraryColor)
                                                node("librus")
                                            }
                                        }
                                        currentState >= librusAppears -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", highlightColor)
                                                node("lib2", highlightColor)
                                                node("librus", highlightColor)
                                            }
                                        }
                                        currentState >= subprojectsFilterHighlighted -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", highlightColor)
                                                node("lib2", highlightColor)
                                            }
                                        }
                                        currentState >= resetAfterSubprojectsHighlighted -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", libraryColor)
                                                node("lib2", libraryColor)
                                            }
                                        }
                                        currentState >= subprojectsHighlighted -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", highlightColor)
                                                node("lib1", highlightColor)
                                                node("lib2", highlightColor)
                                            }
                                        }
                                        currentState >= libsAppear -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                                node("lib1", libraryColor)
                                                node("lib2", libraryColor)
                                            }
                                        }
                                        currentState >= appMovesUnderRootProject -> buildTree {
                                            node("root-project", rootProjectColor) {
                                                node("app", appColor)
                                            }
                                        }
                                        currentState >= treeAppears -> buildTree {
                                            node("app", rootProjectColor)
                                        }
                                        else -> buildTree {}
                                    }

                                    AnimatedHorizontalTree(
                                        tree = mainBuildTree,
                                        excludeSharedChildrenFromLayout = true,
                                        modifier = Modifier.animateBounds(this)
                                    ) { node ->
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
                                                createChildTransition {
                                                    when {
                                                        it >= appAppliesConvention && node.value == "app" -> APP_BUILD_GRADLE_KTS[5].String()

                                                        it >= libAppliesConvention && node.value.matches("""lib\d+""".toRegex()) -> LIB_BUILD_GRADLE_KTS[2].String()

                                                        it >= libConventionShrinks && node.value == "lib-convention" -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }

                                                        it >= rootProjectShrinks && node.value == "root-project" -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }

                                                        it >= rootProjectChangedToNothingToConfigure && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[8].String()

                                                        it >= commonConfigMovedToLibConvention && node.value == "lib-convention" -> LIB_CONVENTION[2].String()
                                                        it >= commonConfigMovedToLibConvention && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[7].String()

                                                        it >= libConventionAppears && node.value == "lib-convention" -> JustName(node)

                                                        it >= resetAfterSubprojectsFilterHighlighted && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[5].String()
                                                        it >= resetAfterSubprojectsFilterHighlighted && node.value == "librus" -> JustName(node)
                                                        it >= resetAfterSubprojectsFilterHighlighted && node.value.matches("""lib\d+""".toRegex()) -> JustName(node)

                                                        it >= librusAppears && node.value == "librus" -> "librus ❌".annotate()

                                                        it >= subprojectsFilterHighlighted && node.value.matches("""lib\d+""".toRegex()) -> "${node.value} ✅".annotate()
                                                        it >= subprojectsFilterHighlighted && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[4].String()

                                                        it >= resetAfterSubprojectsHighlighted && node.value == "app" -> JustName(node)
                                                        it >= resetAfterSubprojectsHighlighted && node.value.matches("""lib\d+""".toRegex()) -> JustName(node)
                                                        it >= resetAfterSubprojectsHighlighted && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[3].String()

                                                        it >= subprojectsHighlighted && node.value == "app" -> "app ❌".annotate()
                                                        it >= subprojectsHighlighted && node.value.matches("""lib\d+""".toRegex()) -> "${node.value} ✅".annotate()
                                                        it >= subprojectsHighlighted && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[2].String()

                                                        it >= libsShrink && node.value.contains("lib") -> JustName(node)
                                                        it >= crossConfigAddedToRootProject && node.value.contains("lib") -> LIB_BUILD_GRADLE_KTS[1].String()
                                                        it >= crossConfigAddedToRootProject && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[1].String()
                                                        it >= rootProjectExpands && node.value == "root-project" -> ROOT_BUILD_GRADLE_KTS[0].String()
                                                        it >= libsExpand && node.value.contains("""lib\d+""".toRegex()) -> LIB_BUILD_GRADLE_KTS[0].String()
                                                        it >= appShrinks && node.value == "app" -> JustName(node)
                                                        it >= appExpands && node.value == "app" -> APP_BUILD_GRADLE_KTS[4].String()
                                                        else -> buildAnnotatedString { withColor(Color.White) { append(node.value) } }
                                                    }
                                                }.MagicAnnotatedString(Modifier.padding(8.dp), split = { it.splitByChars() })
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
    val movedToConvention by tag()
    val nothingLeft by tag()
    val todo by tag()

    """
    ${subprojectsBlock}${subprojectsKeyword}subprojects${subprojectsKeyword}${subprojectsFilter}
        .filter { it.name.startsWith("lib")${subprojectsFilter}${subprojectsForEach}
        .forEach${subprojectsForEach} { ${subprojectsConfig}
        ${subprojectsIndent1}    it.${subprojectsIndent1}apply(plugin = "java-library")
        ${subprojectsIndent2}    it.${subprojectsIndent2}apply(plugin = "maven-publish")
    
        ${subprojectsIndent3}    ${subprojectsIndent3}publishing.publications.create("lib") {
        ${subprojectsIndent4}    ${subprojectsIndent4}    from(components["java"])
        ${subprojectsIndent5}    ${subprojectsIndent5}}${subprojectsConfig}${movedToConvention}
            // moved to convention${movedToConvention}${todo}
        // TODO          ${todo}${subprojectsClosingBrace}
        }${subprojectsClosingBrace}
    }${subprojectsBlock}${nothingLeft}// nothing left to configure here :)${nothingLeft}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(movedToConvention, nothingLeft, subprojectsConfig, subprojectsFilter, subprojectsForEach, subprojectsClosingBrace, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5) }
        .then { hide(todo).reveal(subprojectsConfig) }
        .then { focus(subprojectsKeyword) }
        .then { unfocus() }
        .then { reveal(subprojectsFilter, subprojectsForEach, subprojectsIndent1, subprojectsIndent2, subprojectsIndent3, subprojectsIndent4, subprojectsIndent5, subprojectsClosingBrace).focus(subprojectsFilter) }
        .then { unfocus() }
        .then { focus(subprojectsConfig) }
        .then { unfocus().hide(subprojectsConfig).reveal(movedToConvention) }
        .then { hide(subprojectsBlock).reveal(nothingLeft) }
}

private val APP_BUILD_GRADLE_KTS = buildCodeSamples {
    val emptyLine by tag()
    val dependencies by tag()

    """
    plugins {
        `app-convention`
    }${emptyLine}
    ${emptyLine}${dependencies}
    dependencies {
        implementation(projects.subProject)
        implementation(libs.spring.boot.web)
    }${dependencies}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
        .openPanel("tree")
        .pass()
        .hideIde()
        .then { hide(emptyLine) }
        .then { hide(dependencies) }
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
