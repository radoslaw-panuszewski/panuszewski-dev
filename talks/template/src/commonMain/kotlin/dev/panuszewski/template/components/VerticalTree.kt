package dev.panuszewski.template.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private enum class VerticalTreeSubSlot {
    CONTENT,
    CONNECTION,
}

private class VerticalTreeNode<T>(
    val value: T,
    val depth: Int,
    val children: List<VerticalTreeNode<T>>,
) {
    var parent: VerticalTreeNode<T>? = null
    var curve: Placeable? = null

    // Additional parents for nodes with multiple parents
    val additionalParents: MutableList<VerticalTreeNode<T>> = mutableListOf()
    val additionalCurves: MutableList<Placeable?> = mutableListOf()

    lateinit var placeable: Placeable
    var x = 0
    var y = 0

    var minWidth = 0

    // Helper function to get all parents (primary + additional)
    fun getAllParents(): List<VerticalTreeNode<T>> {
        return listOfNotNull(parent) + additionalParents
    }
}

/**
 * Overloaded version of VerticalTree that accepts a single root node.
 * This function is provided for backward compatibility.
 */
@Composable
fun <T> VerticalTree(
    root: T,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(64.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    verticalMinimumSpacing: Dp = 0.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    horizontalMinimumSpacing: Dp = 0.dp,
    excludeSharedChildrenFromLayout: Boolean = false,
    connection: @Composable (parent: T, parentRect: Rect, child: T, childRect: Rect) -> Unit = { _, _, _, _ -> },
    content: @Composable (node: T) -> Unit,
) {
    VerticalTree(
        roots = listOf(root),
        getChildren = getChildren,
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        verticalAlignment = verticalAlignment,
        verticalMinimumSpacing = verticalMinimumSpacing,
        horizontalArrangement = horizontalArrangement,
        horizontalAlignment = horizontalAlignment,
        horizontalMinimumSpacing = horizontalMinimumSpacing,
        excludeSharedChildrenFromLayout = excludeSharedChildrenFromLayout,
        connection = connection,
        content = content
    )
}

/**
 * A vertical tree layout that supports multiple root nodes.
 * Root nodes are displayed at the top of the tree.
 */
@Composable
fun <T> VerticalTree(
    roots: Collection<T>,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,

    // How the nodes are spaced out vertically.
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(64.dp),
    // How child nodes are aligned with each other.
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    // Ensures the size of the tree will - at minimum - support this spacing between vertical nodes.
    verticalMinimumSpacing: Dp = 0.dp,

    // How nodes are spaced out horizontally.
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    // How parent nodes are aligned to their children.
    // Also, how children with a wider parent are aligned.
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    // Ensures the size of the tree will - at minimum - support this spacing between vertical nodes.
    horizontalMinimumSpacing: Dp = 0.dp,

    // When true, shared children (nodes with multiple parents) are excluded from width calculations
    // and only positioned once during the initial layout pass. They are then repositioned in post-processing.
    // When false, shared children are included in all calculations and may be positioned multiple times.
    excludeSharedChildrenFromLayout: Boolean = false,

    connection: @Composable (parent: T, parentRect: Rect, child: T, childRect: Rect) -> Unit = { _, _, _, _ -> },
    content: @Composable (node: T) -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        // Map to store unique nodes by their value
        val uniqueNodes = mutableMapOf<T, VerticalTreeNode<T>>()

        fun collect(sink: MutableList<VerticalTreeNode<T>>, value: T, depth: Int, parentNode: VerticalTreeNode<T>? = null): VerticalTreeNode<T> {
            // Check if we've already seen this node value
            val existingNode = uniqueNodes[value]

            if (existingNode != null) {
                // If this node already exists and has a parent, add the new parent as an additional parent
                if (parentNode != null) {
                    if (existingNode.parent == null) {
                        existingNode.parent = parentNode
                    } else if (existingNode.parent != parentNode) {
                        existingNode.additionalParents.add(parentNode)
                    }
                }
                return existingNode
            }

            // Process children first to ensure all nodes at this level are properly connected
            val children = getChildren(value).map { collect(sink, it, depth + 1, null) }

            // Create a new node
            val node = VerticalTreeNode(value, depth, children)
            uniqueNodes[value] = node

            // Set parent-child relationships
            if (parentNode != null) {
                node.parent = parentNode
            }

            for (child in children) {
                if (child.parent == null) {
                    child.parent = node
                } else if (child.parent != node) {
                    child.additionalParents.add(node)
                }
            }

            sink.add(node)
            return node
        }

        val nodes = mutableListOf<VerticalTreeNode<T>>()
        val rootNodes = mutableListOf<VerticalTreeNode<T>>()

        // Process each root node
        for (rootValue in roots) {
            val rootNode = collect(nodes, rootValue, 0)
            rootNodes.add(rootNode)
        }

        // If no roots were provided, return an empty layout
        if (rootNodes.isEmpty()) {
            return@SubcomposeLayout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val byDepth = Array<MutableList<VerticalTreeNode<T>>>(nodes.maxOf { it.depth } + 1) { mutableListOf() }
        for (node in nodes) {
            byDepth[node.depth].add(node)
        }

        subcompose(VerticalTreeSubSlot.CONTENT) {
            for (node in nodes) {
                content(node.value)
            }
        }.forEachIndexed { index, measurable ->
            nodes[index].placeable = measurable.measure(Constraints())
        }

        // ====================================
        // Vertical arrangement and alignment
        // ====================================
        val ySpacing = maxOf(verticalArrangement.spacing, verticalMinimumSpacing).roundToPx()

        val ySizes = IntArray(byDepth.size) { byDepth[it].maxOf { it.placeable.height } }
        val minHeight = maxOf(ySizes.sumOf { it + ySpacing } - ySpacing, constraints.minHeight)

        val yPositions = IntArray(byDepth.size)
        with(verticalArrangement) { arrange(minHeight, ySizes, yPositions) }
        for ((i, nodes) in byDepth.withIndex()) {
            val size = ySizes[i]
            for (node in nodes) {
                node.y = yPositions[i] + verticalAlignment.align(node.placeable.height, size)
            }
        }

        // ==================================
        // Horizontal arrangement and alignment
        // ==================================

        val xSpacing = maxOf(horizontalArrangement.spacing, horizontalMinimumSpacing).roundToPx()

        fun widthUp(node: VerticalTreeNode<T>): Int {
            var childWidth = -xSpacing
            for (child in node.children) {
                // When excludeSharedChildrenFromLayout is true, skip shared children in width calculation
                if (excludeSharedChildrenFromLayout && child.getAllParents().size > 1) {
                    continue
                }
                childWidth += widthUp(child) + xSpacing
            }
            node.minWidth = maxOf(node.placeable.width, childWidth)
            return node.minWidth
        }

        fun widthDown(node: VerticalTreeNode<T>, width: Int) {
            val original = node.minWidth
            node.minWidth = width

            val childSpacing = xSpacing * (node.children.size - 1)
            for (child in node.children) {
                val childWidth = (child.minWidth - childSpacing) * width / original + childSpacing
                child.minWidth = childWidth
                widthDown(child, childWidth)
            }
        }

        // Track which shared nodes have been positioned during initial pass
        val positionedSharedNodes = mutableSetOf<VerticalTreeNode<T>>()
        
        fun alignChildren(children: List<VerticalTreeNode<T>>, xOffset: Int, width: Int) {
            if (children.isEmpty()) return

            // When excludeSharedChildrenFromLayout is true, filter out shared nodes that have already been positioned
            val childrenToPosition = if (excludeSharedChildrenFromLayout) {
                children.filter { child ->
                    child.getAllParents().size <= 1 || child !in positionedSharedNodes
                }
            } else {
                children
            }
            
            if (childrenToPosition.isEmpty()) return

            val xSizes = IntArray(childrenToPosition.size) { childrenToPosition[it].minWidth }
            val xPositions = IntArray(childrenToPosition.size)
            with(horizontalArrangement) { arrange(width, xSizes, layoutDirection, xPositions) }

            val childrenOffset = if (horizontalArrangement.spacing.value > 0f) {
                var min = width
                var max = 0
                for (i in childrenToPosition.indices) {
                    min = minOf(min, xPositions[i])
                    max = maxOf(max, xPositions[i] + xSizes[i])
                }
                horizontalAlignment.align(max - min, width, layoutDirection)
            } else {
                0
            }

            for (i in xPositions.indices) {
                val child = childrenToPosition[i]
                val xPosition = xOffset + xPositions[i]

                val childOffset = if (child.placeable.width < child.minWidth) {
                    horizontalAlignment.align(child.placeable.width, child.minWidth, layoutDirection)
                } else {
                    0
                }

                child.x = xPosition + childrenOffset + childOffset
                
                // Mark shared nodes as positioned when excludeSharedChildrenFromLayout is true
                if (excludeSharedChildrenFromLayout && child.getAllParents().size > 1) {
                    positionedSharedNodes.add(child)
                }

                // Calculate the horizontal center of the child node
                val childCenterX = child.x + child.placeable.width / 2

                // If the child has children, center them around the child's center
                if (child.children.isNotEmpty()) {
                    val childrenWidth = xSizes[i]
                    val childrenStartX = childCenterX - childrenWidth / 2
                    alignChildren(child.children, childrenStartX, xSizes[i])
                }
            }
        }

        // Calculate total width including spacing between root nodes
        var totalWidth = 0

        // First calculate the width of each root node
        for (rootNode in rootNodes) {
            widthUp(rootNode)
        }

        // Then calculate total width with consistent spacing
        if (rootNodes.isNotEmpty()) {
            // Sum up the widths of all root nodes
            val rootNodesWidth = rootNodes.sumOf { it.minWidth }

            // Add spacing between root nodes (spacing * (number of nodes - 1))
            val spacingBetweenRoots = if (rootNodes.size > 1) xSpacing * (rootNodes.size - 1) else 0

            // Total width is the sum of root node widths plus spacing between them
            totalWidth = rootNodesWidth + spacingBetweenRoots
        }

        // Apply width adjustments if needed
        if (horizontalArrangement.spacing.value <= 0f) {
            totalWidth = maxOf(totalWidth, constraints.minWidth)
            for (rootNode in rootNodes) {
                widthDown(rootNode, rootNode.minWidth)
            }
        }

        // Align all root nodes and their children
        alignChildren(rootNodes, 0, totalWidth)

        // Post-processing step to adjust positioning for nodes with multiple parents
        fun repositionSharedNodeAndDescendants(node: VerticalTreeNode<T>, deltaX: Int) {
            // Adjust this node's position
            node.x += deltaX
            
            // Recursively adjust all children (both shared and non-shared)
            for (child in node.children) {
                if (child.getAllParents().size <= 1) {
                    // Child has only one parent (this node), so adjust it
                    repositionSharedNodeAndDescendants(child, deltaX)
                }
            }
        }

        for (node in nodes) {
            val allParents = node.getAllParents()
            if (allParents.size > 1) {
                // Sort parents by their x position
                val sortedParents = allParents.sortedBy { it.x }

                // Calculate the center position between first and last parent centers
                val firstParentCenterX = sortedParents.first().x + sortedParents.first().placeable.width / 2
                val lastParentCenterX = sortedParents.last().x + sortedParents.last().placeable.width / 2
                val parentsCenterX = (firstParentCenterX + lastParentCenterX) / 2

                // Calculate how much we need to move this node
                val targetX = parentsCenterX - node.placeable.width / 2
                val deltaX = targetX - node.x

                // Reposition the node and all its descendants by the same delta
                repositionSharedNodeAndDescendants(node, deltaX)
            }
        }

        // Create a list of all parent-child connections
        val connections = mutableListOf<Pair<VerticalTreeNode<T>, VerticalTreeNode<T>>>()
        for (child in nodes) {
            // Add primary parent connection if it exists
            child.parent?.let { parent ->
                connections.add(parent to child)
            }

            // Add additional parent connections
            for (additionalParent in child.additionalParents) {
                connections.add(additionalParent to child)
            }
        }

        subcompose(VerticalTreeSubSlot.CONNECTION) {
            // Draw connections for all parent-child pairs
            for ((parent, child) in connections) {
                val offset = Offset(minOf(parent.x, child.x).toFloat(), parent.y.toFloat())
                val parentRect = Rect(
                    left = parent.x.toFloat(),
                    top = parent.y.toFloat(),
                    right = (parent.x + parent.placeable.width).toFloat(),
                    bottom = (parent.y + parent.placeable.height).toFloat(),
                )
                val childRect = Rect(
                    left = child.x.toFloat(),
                    top = child.y.toFloat(),
                    right = (child.x + child.placeable.width).toFloat(),
                    bottom = (child.y + child.placeable.height).toFloat(),
                )
                connection(parent.value, parentRect.translate(-offset), child.value, childRect.translate(-offset))
            }
        }.forEachIndexed { index, measurable ->
            val (parent, child) = connections[index]

            val xStart = minOf(parent.x, child.x)
            val xEnd = maxOf(parent.x + parent.placeable.width, child.x + child.placeable.width)
            val yStart = parent.y
            val yEnd = child.y + child.placeable.height
            val constraints = Constraints.fixed(width = abs(xEnd - xStart), height = abs(yEnd - yStart))

            // Store the curve in the appropriate place
            if (child.parent == parent) {
                child.curve = measurable.measure(constraints)
            } else {
                // This is an additional parent, store the curve in additionalCurves
                child.additionalCurves.add(measurable.measure(constraints))
            }
        }

        layout(maxOf(totalWidth, constraints.minWidth), minHeight) {
            for (node in nodes) {
                node.placeable.place(x = node.x, y = node.y)

                // Place curve for primary parent
                val parent = node.parent
                val curve = node.curve
                if (parent != null && curve != null) {
                    curve.place(x = minOf(parent.x, node.x), y = parent.y)
                }

                // Place curves for additional parents
                for (i in node.additionalParents.indices) {
                    val additionalParent = node.additionalParents[i]
                    val additionalCurve = if (i < node.additionalCurves.size) node.additionalCurves[i] else null

                    if (additionalCurve != null) {
                        additionalCurve.place(x = minOf(additionalParent.x, node.x), y = additionalParent.y)
                    }
                }
            }
        }
    }
}
