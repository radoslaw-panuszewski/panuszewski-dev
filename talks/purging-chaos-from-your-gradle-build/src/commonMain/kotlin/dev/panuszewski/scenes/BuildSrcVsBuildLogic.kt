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
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.withColor

fun StoryboardBuilder.BuildSrcVsBuildLogic() {
    scene(100) {
        withStateTransition {
            TitleScaffold("buildSrc vs build-logic") {

                val buildSrcColor = MaterialTheme.colors.primary
                val libColor = NICE_BLUE
                val appColor = MaterialTheme.colors.secondary

                val tree = when {
                    currentState >= 2 -> buildTree {
                        node("buildSrc", buildSrcColor) {
                            node("wtf-lib", libColor) {
                                node("lib1", libColor)
                                node("lib2", libColor)
                            }
                            node("wtf-app", appColor) {
                                node("app1", appColor)
                            }
                        }
                    }
                    currentState >= 1 -> buildTree {
                        node("buildSrc", buildSrcColor) {
                            node("wtf-lib", libColor)
                            node("wtf-app", appColor)
                        }
                    }
                    else -> buildTree {
                        node("buildSrc", buildSrcColor)
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
                                    it == 3 && node.value.startsWith("lib") -> """
                                        plugins {
                                            `wtf-lib`
                                        }
                                        """
                                        .trimIndent()
                                        .toCode(language = Language.KotlinDsl)
                                    it == 3 && node.value.startsWith("app") -> """
                                        plugins {
                                            `wtf-app`
                                        }
                                        """
                                        .trimIndent()
                                        .toCode(language = Language.KotlinDsl)
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