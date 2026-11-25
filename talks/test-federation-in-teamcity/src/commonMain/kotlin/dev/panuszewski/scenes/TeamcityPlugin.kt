package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import androidx.compose.material.MaterialTheme
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Design.ANNOTATIONS
import dev.panuszewski.components.Design.VIRTUAL_CONFIGS
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.components.allTests
import dev.panuszewski.components.appendDesign
import dev.panuszewski.components.contractTests
import dev.panuszewski.components.smokeTests
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.ArrowDirection.FORWARD
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.RoundedTreeNode
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.TeamcityPlugin() {
    val (
        titleOnly,
        promotionAppears,
        freezeTimeExplained,
        processorRuns,
        promotionModified,
        detailsAppear,
        detailsPrecedent,
        prosAppear,
        consAppear,
        designsAppear,
        lastState
    ) = subsequentNumbers()

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        val palette = palette

        withIntTransition {
            val transition = this

            SectionScaffold("(4.1) TeamCity plugin") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = freezeTimeExplained, until = detailsAppear) {
                            annotatedStringItem {
                                append("The chain is rewritten at ")
                                appendWithColor(palette.orange, "freeze time")
                                append(" — after the Aggregate is queued, but before it starts")
                            }
                            annotatedStringItem {
                                appendAsCode("TestFederationProcessor")
                                append(" mutates the ")
                                appendAsCode("BuildPromotion")
                                append(", turning Foo and Bar into composite builds")
                            }
                        }
                    }

                    transition.FadeInOutAnimatedVisibility({ it < detailsAppear }) {
                        Box(Modifier.height(240.dp)) {
                            val tree = when {
                                currentState >= promotionModified -> buildTree {
                                    node("Aggregate") {
                                        node("Foo", color = palette.turquoise, arrow = FORWARD) {
                                            node(smokeTests("Foo"), color = palette.purple, arrow = FORWARD)
                                            node(contractTests("Foo"), color = palette.purple, arrow = FORWARD)
                                        }
                                        node("Bar", color = palette.turquoise, arrow = FORWARD) {
                                            node(allTests("Bar"), color = palette.purple, arrow = FORWARD)
                                        }
                                    }
                                }

                                currentState >= processorRuns -> buildTree {
                                    node("Aggregate", color = palette.orange) {
                                        node("Foo", color = palette.orange, arrow = FORWARD)
                                        node("Bar", color = palette.orange, arrow = FORWARD)
                                    }
                                }

                                else -> buildTree {
                                    node("Aggregate") {
                                        node("Foo", arrow = FORWARD)
                                        node("Bar", arrow = FORWARD)
                                    }
                                }
                            }

                            transition.SlideFromBottomAnimatedVisibility({ it >= promotionAppears }) {
                                AnimatedHorizontalTree(tree) { node ->
                                    RoundedTreeNode(node)
                                }
                            }
                        }
                    }

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = detailsAppear, textStyle = MaterialTheme.typography.body1) {
                            annotatedStringItem {
                                append("Powered by ")
                                appendWithColor(palette.purple, "virtual configurations")
                                append(" — the same mechanism behind Matrix and Parallel Tests")
                            }
                            annotatedStringItem {
                                append("We implement ")
                                appendAsCode("ProcessVirtualConfigurations.freeze()")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.green, "🟢 full flexibility")
                                append(" — everything is possible")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.red, "🔴🔴 needs a custom plugin installed on the buildserver")
                            }
                            annotatedStringItem {
                                append("Designs possible to implement: ")
                                appendDesign(VIRTUAL_CONFIGS, "✅")
                                append(",  ")
                                appendDesign(ANNOTATIONS, "✅")
                            }
                        }
                    }
                }
            }
        }
    }
}
