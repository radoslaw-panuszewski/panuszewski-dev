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

fun StoryboardBuilder.SkipTags() {
    val (
        titleOnly,
        paramExplained,
        catchExplained,
        chainAppears,
        coordinatorAppears,
        variantsSkipped,
        prosAppear,
        prosSimpleConfig,
        prosFilterOnly,
        prosTrigger,
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

            SectionScaffold("(4.3) Skip tags + REST trigger") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = paramExplained, until = prosAppear) {
                            annotatedStringItem {
                                appendAsCode("teamcity.build.chain.skipTags")
                                append(" — a comma-separated list of build IDs to skip in the chain")
                            }
                            annotatedStringItem {
                                append("The catch: it must be set ")
                                appendWithColor(palette.orange, "before the chain is queued")
                                append(" — once the Aggregate is triggered, it's too late")
                            }
                        }
                    }

                    transition.FadeInOutAnimatedVisibility({ it < prosAppear }) {
                        Box(Modifier.height(210.dp)) {
                            val tree = when {
                                currentState >= variantsSkipped -> buildTree {
                                    node("SafeMergeCoordinator", color = palette.turquoise, arrow = FORWARD) {
                                        node("Aggregate") {
                                            node(smokeTests("Foo"), color = palette.green, arrow = FORWARD)
                                            node("❌ ${contractTests("Foo")}", color = palette.gray, arrow = FORWARD)
                                            node("❌ ${allTests("Foo")}", color = palette.gray, arrow = FORWARD)
                                        }
                                    }
                                }

                                currentState >= coordinatorAppears -> buildTree {
                                    node("SafeMergeCoordinator", color = palette.turquoise, arrow = FORWARD) {
                                        node("Aggregate") {
                                            node(smokeTests("Foo"), arrow = FORWARD)
                                            node(contractTests("Foo"), arrow = FORWARD)
                                            node(allTests("Foo"), arrow = FORWARD)
                                        }
                                    }
                                }

                                else -> buildTree {
                                    node("Aggregate") {
                                        node(smokeTests("Foo"), arrow = FORWARD)
                                        node(contractTests("Foo"), arrow = FORWARD)
                                        node(allTests("Foo"), arrow = FORWARD)
                                    }
                                }
                            }

                            transition.SlideFromBottomAnimatedVisibility({ it >= chainAppears }) {
                                AnimatedHorizontalTree(tree) { node ->
                                    RoundedTreeNode(node)
                                }
                            }
                        }
                    }

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = coordinatorAppears, until = prosAppear) {
                            annotatedStringItem {
                                appendAsCode("POST /buildQueue { buildType: Aggregate, skipTags: Foo_CONTRACT,Foo_FULL }")
                            }
                        }

                        RevealSequentially(since = prosAppear, textStyle = MaterialTheme.typography.body1) {
                            annotatedStringItem {
                                appendWithColor(palette.green, "🟢 built-in feature we already use")
                                append(" for compiler benchmarks")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.green, "🟢 very simple configuration")
                                append(" — we only filter out dependencies")
                            }
                            annotatedStringItem {
                                appendWithColor(palette.yellow, "🔴🟢 needs a separate build to trigger the Aggregate")
                                append(" — but ")
                                appendAsCode("SafeMergeCoordinator")
                                append(" already does this")
                            }
                            annotatedStringItem {
                                append("Designs possible to implement: ")
                                appendDesign(VIRTUAL_CONFIGS, "❌")
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
