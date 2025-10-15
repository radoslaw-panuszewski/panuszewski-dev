package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.LocalCodeStyle
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.NICE_PINK
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.BuildSrcVsBuildLogic() {
    val (
        rootProjectAppears,
        subprojectsAppear,
        subprojectsExpand,
        conventionsAppear,
        buildSrcAppears,
        wtfAppModifiedInBuildSrc,
        buildSrcModified,
        allSubprojectsReconfigured,
        resetAfterBuildSrcChange,
        buildLogicAppears,
        conventionsChangeToNonTypesafe,
        buildLogicSplitsIntoSubprojects,
        wtfAppModifiedInBuildLogic,
        buildLogicAppModified,
        onlyAppReconfigured,
        resetAfterBuildLogicChange,
    ) = subsequentNumbers()

    val totalStates = resetAfterBuildLogicChange + 1

    scene(totalStates) {
        val rootColor = MaterialTheme.colors.primary
        val libColor = NICE_BLUE
        val appColor = MaterialTheme.colors.secondary
        val buildSrcColor = NICE_PINK
        val buildLogicColor = NICE_GREEN
        val highlightColor = NICE_ORANGE
        val neutralColor = Color.Gray

        withIntTransition {
            val title = when {
                currentState >= buildLogicAppears -> buildAnnotatedString { append("Making changes: "); withColor(buildLogicColor) { append("build-logic") } }
                currentState >= buildSrcAppears -> buildAnnotatedString { append("Making changes: "); withColor(buildSrcColor) { append("buildSrc") } }
                else -> "Making changes".annotate()
            }

            TitleScaffold(title) {
                val tree = when {
                    currentState >= resetAfterBuildLogicChange -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("app-convention", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= onlyAppReconfigured -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app", highlightColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= buildLogicAppModified -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= wtfAppModifiedInBuildLogic -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= buildLogicSplitsIntoSubprojects -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("app-convention", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= buildLogicAppears -> buildTree {
                        val buildLogic = reusableNode("build-logic", buildLogicColor) {
                            node("lib-convention", buildLogicColor)
                            node("app-convention", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogic) }
                            node("lib2", libColor) { node(buildLogic) }
                            node("app", appColor) { node(buildLogic) }
                        }
                    }
                    currentState >= resetAfterBuildSrcChange -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("lib-convention", buildSrcColor)
                            node("app-convention", buildSrcColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= allSubprojectsReconfigured -> buildTree {
                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                            node("lib-convention", buildSrcColor)
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", highlightColor) { node(buildSrc) }
                            node("lib2", highlightColor) { node(buildSrc) }
                            node("app", highlightColor) { node(buildSrc) }
                        }
                    }
                    currentState >= buildSrcModified -> buildTree {
                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                            node("lib-convention", buildSrcColor)
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= wtfAppModifiedInBuildSrc -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("lib-convention", buildSrcColor)
                            node("app-convention", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= buildSrcAppears -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("lib-convention", buildSrcColor)
                            node("app-convention", buildSrcColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= conventionsAppear -> buildTree {
                        val wtfLib = reusableNode("lib-convention", neutralColor)
                        val wtfApp = reusableNode("app-convention", neutralColor)

                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(wtfLib) }
                            node("lib2", libColor) { node(wtfLib) }
                            node("app", appColor) { node(wtfApp) }
                        }
                    }
                    currentState >= subprojectsAppear -> buildTree {
                        node("root-project", rootColor) {
                            node("lib1", libColor)
                            node("lib2", libColor)
                            node("app", appColor)
                        }
                    }
                    currentState >= rootProjectAppears -> buildTree {
                        node("root-project", rootColor)
                    }
                    else -> emptyList()
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
                            createChildTransition {
                                when {
                                    it >= conventionsChangeToNonTypesafe && node.value.matches("""lib\d+""".toRegex()) -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        append("    id(")
                                        withColor(LocalCodeStyle.current.string.color) { append("\"lib-convention\"") }
                                        appendLine(")")
                                        append("}")
                                    }
                                    it >= conventionsChangeToNonTypesafe && node.value =="app" -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        append("    id(")
                                        withColor(LocalCodeStyle.current.string.color) { append("\"app-convention\"") }
                                        appendLine(")")
                                        append("}")
                                    }
                                    it >= subprojectsExpand && node.value.matches("""lib\d+""".toRegex()) -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        withColor(libColor) { appendLine("    `lib-convention`") }
                                        append("}")
                                    }
                                    it >= subprojectsExpand && node.value =="app" -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        withColor(appColor) { appendLine("    `app-convention`") }
                                        append("}")
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
    }
}