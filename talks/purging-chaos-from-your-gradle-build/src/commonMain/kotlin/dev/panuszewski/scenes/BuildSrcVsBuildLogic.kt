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
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.NICE_PINK
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.BuildSrcVsBuildLogic() {
    scene(100) {
        val rootColor = MaterialTheme.colors.primary
        val libColor = NICE_BLUE
        val appColor = MaterialTheme.colors.secondary
        val buildSrcColor = NICE_PINK
        val buildLogicColor = NICE_GREEN
        val highlightColor = NICE_ORANGE
        val neutralColor = Color.Gray

        withStateTransition {
            val title = when {
                currentState >= 9 -> buildAnnotatedString { append("Making changes: "); withColor(buildLogicColor) { append("build-logic") } }
                currentState >= 4 -> buildAnnotatedString { append("Making changes: "); withColor(buildSrcColor) { append("buildSrc") } }
                else -> "Making changes".annotate()
            }

            TitleScaffold(title) {
                val tree = when {
                    currentState >= 14 -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("wtf-app", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app1", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= 13 -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app1", highlightColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= 12 -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app1", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= 11 -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app1", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= 10 -> buildTree {
                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                        }
                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                            node("wtf-app", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogicLib) }
                            node("lib2", libColor) { node(buildLogicLib) }
                            node("app1", appColor) { node(buildLogicApp) }
                        }
                    }
                    currentState >= 9 -> buildTree {
                        val buildLogic = reusableNode("build-logic", buildLogicColor) {
                            node("wtf-lib", buildLogicColor)
                            node("wtf-app", buildLogicColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildLogic) }
                            node("lib2", libColor) { node(buildLogic) }
                            node("app1", appColor) { node(buildLogic) }
                        }
                    }
                    currentState >= 8 -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("wtf-lib", buildSrcColor)
                            node("wtf-app", buildSrcColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app1", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= 7 -> buildTree {
                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                            node("wtf-lib", buildSrcColor)
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", highlightColor) { node(buildSrc) }
                            node("lib2", highlightColor) { node(buildSrc) }
                            node("app1", highlightColor) { node(buildSrc) }
                        }
                    }
                    currentState >= 6 -> buildTree {
                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                            node("wtf-lib", buildSrcColor)
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app1", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= 5 -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("wtf-lib", buildSrcColor)
                            node("wtf-app", highlightColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app1", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= 4 -> buildTree {
                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                            node("wtf-lib", buildSrcColor)
                            node("wtf-app", buildSrcColor)
                        }
                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(buildSrc) }
                            node("lib2", libColor) { node(buildSrc) }
                            node("app1", appColor) { node(buildSrc) }
                        }
                    }
                    currentState >= 3 -> buildTree {
                        val wtfLib = reusableNode("wtf-lib", neutralColor)
                        val wtfApp = reusableNode("wtf-app", neutralColor)

                        node("root-project", rootColor) {
                            node("lib1", libColor) { node(wtfLib) }
                            node("lib2", libColor) { node(wtfLib) }
                            node("app1", appColor) { node(wtfApp) }
                        }
                    }
                    currentState >= 1 -> buildTree {
                        node("root-project", rootColor) {
                            node("lib1", libColor)
                            node("lib2", libColor)
                            node("app1", appColor)
                        }
                    }
                    else -> buildTree {
                        node("root-project", rootColor)
                    }
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
                                    it >= 2 && node.value.startsWith("lib") -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        withColor(libColor) { appendLine("    `wtf-lib`") }
                                        append("}")
                                    }
                                    it >= 2 && node.value.startsWith("app") -> buildAnnotatedString {
                                        appendLine("plugins {")
                                        withColor(appColor) { appendLine("    `wtf-app`") }
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