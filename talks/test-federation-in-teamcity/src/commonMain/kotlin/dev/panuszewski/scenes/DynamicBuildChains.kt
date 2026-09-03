package dev.panuszewski.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.ArrowDirection
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.DynamicBuildChains() {
    val (
        initialState,
        titleSlides,
        treeAppears,
        treeExpands
    ) = subsequentNumbers()

    scene(stateCount = 10) {
        withIntTransition {
            SlidingTitleScaffold("Dynamic Build Chains") {contentVisible ->
                contentVisible.SlideFromBottomAnimatedVisibility {
                    val tree = when {
                        currentState >= treeExpands -> buildTree {
                            node("Generator Build") {
                                node("Final Build", arrow = ArrowDirection.FORWARD)
                            }
                        }
                        else -> buildTree {
                            node("Generator Build")
                        }
                    }

                    AnimatedHorizontalTree(tree) { node ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(width = if (palette.darkMode) 2.dp else 3.dp, color = node.color ?: Color.Gray, shape = RoundedCornerShape(8.dp))
                                .background(palette.treeNodeBackground)
                                .padding(8.dp)
                        ) {
                            Text(text = node.value)
                        }
                    }
                }
            }
        }
    }
}