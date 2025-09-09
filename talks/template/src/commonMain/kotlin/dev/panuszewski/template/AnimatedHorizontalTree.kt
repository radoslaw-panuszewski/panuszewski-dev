package dev.panuszewski.template

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlin.collections.set

@Composable
fun <T : Any> AnimatedHorizontalTree(
    roots: List<T>,
    getChildren: (node: T) -> Collection<T>,
    content: @Composable (node: T) -> Unit,
) {
    val treeState = TreeState(roots, getChildren)

    SharedTransitionLayout {
        AnimatedContent(
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            targetState = treeState
        ) { targetTreeState ->

            var offset by remember { mutableStateOf(Offset.Zero) }
            val placements = remember { mutableStateMapOf<T, Rect>() }

            Box(
                contentAlignment = BiasAlignment(0f, -0.5f),
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalTree(
                    roots = targetTreeState.roots,
                    getChildren = targetTreeState.getChildren,
                    modifier = Modifier.onPlaced { offset = it.positionInParent() },
                ) { item ->
                    Box(
                        Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(item),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .onPlaced { placements[item] = it.boundsInParent() },
                    ) {
                        content(item)
                    }
                }
            }

            for ((parent, parentRect) in placements) {
                for (child in getChildren(parent)) {
                    val childRect = placements[child] ?: continue

                    Connection(
                        parentRect = parentRect.translate(offset),
                        childRect = childRect.translate(offset),
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("$parent-$child"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    )
                }
            }
        }
    }
}

private data class TreeState<T>(
    val roots: List<T>,
    val getChildren: (node: T) -> Collection<T>,
)