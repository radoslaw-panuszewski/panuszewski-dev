package dev.panuszewski.template.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent

/**
 * Animated vertical tree layout with shared element transitions.
 * Similar to [AnimatedHorizontalTree], but nodes are positioned vertically (top to bottom)
 * instead of horizontally (left to right).
 */
@Composable
fun <T : Any> AnimatedVerticalTree(
    tree: List<TreeElement<T>>,
    modifier: Modifier = Modifier,
    excludeSharedChildrenFromLayout: Boolean = false,
    content: @Composable (node: TreeElement<T>) -> Unit,
) {
    SharedTransitionLayout {
        AnimatedContent(
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            targetState = tree
        ) { roots ->

            var offset by remember { mutableStateOf(Offset.Zero) }
            val placements = remember { mutableStateMapOf<TreeElement<T>, Rect>() }

            Box(
                contentAlignment = BiasAlignment(-0.5f, 0f),
                modifier = modifier.fillMaxSize()
            ) {
                for ((parent, parentRect) in placements) {
                    for (child in parent.children) {
                        val childRect = placements[child] ?: continue

                        VerticalConnection(
                            parentRect = parentRect.translate(offset),
                            childRect = childRect.translate(offset),
                            color = Color.Gray,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState("$parent-$child"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        )
                    }
                }

                VerticalTree(
                    roots = roots,
                    getChildren = { it.children },
                    modifier = Modifier.onPlaced { offset = it.positionInParent() },
                    excludeSharedChildrenFromLayout = excludeSharedChildrenFromLayout,
                ) { item ->
                    Box(
                        Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(item.value),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .onPlaced { placements[item] = it.boundsInParent() },
                    ) {
                        content(item)
                    }
                }
            }
        }
    }
}
