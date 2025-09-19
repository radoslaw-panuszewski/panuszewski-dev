package dev.panuszewski.template

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
import kotlin.collections.set

@Composable
fun <T : Any> AnimatedHorizontalTree(
    tree: List<TreeElement<T>>,
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
                contentAlignment = BiasAlignment(0f, -0.5f),
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalTree(
                    roots = roots,
                    getChildren = { it.children },
                    modifier = Modifier.onPlaced { offset = it.positionInParent() },
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

            for ((parent, parentRect) in placements) {
                for (child in parent.children) {
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

data class TreeElement<T : Any>(
    val value: T,
    val color: Color? = null,
    val children: List<TreeElement<T>> = emptyList(),
    val isRoot: Boolean,
)

fun <T : Any> buildTree(block: TreeBuilder<T>.() -> Unit): List<TreeElement<T>> {
    val builder = TreeBuilder<T>(isRoot = true)
    builder.apply(block)
    return builder.nodes
}

class TreeBuilder<T : Any>(
    val isRoot: Boolean = false
) {
    val nodes = mutableListOf<TreeElement<T>>()

    fun reusableNode(value: T, color: Color? = null, block: TreeBuilder<T>.() -> Unit = {}): TreeElement<T> {
        val element = TreeElement(
            value = value,
            color = color,
            children = TreeBuilder<T>().apply(block).nodes,
            isRoot = isRoot
        )
        return element
    }

    fun node(value: T, color: Color? = null, block: TreeBuilder<T>.() -> Unit = {}): TreeElement<T> {
        val element = reusableNode(value, color, block)
        nodes.add(element)
        return element
    }

    fun node(element: TreeElement<T>) =
        nodes.add(element)
}