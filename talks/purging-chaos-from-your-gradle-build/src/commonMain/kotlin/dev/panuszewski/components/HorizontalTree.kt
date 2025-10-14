package dev.panuszewski.components

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

private enum class TreeSubSlot {
    CONTENT,
    CONNECTION,
}

private class TreeNode<T>(
    val value: T,
    val depth: Int,
    val children: List<TreeNode<T>>,
) {
    var parent: TreeNode<T>? = null
    var curve: Placeable? = null

    // Additional parents for nodes with multiple parents
    val additionalParents: MutableList<TreeNode<T>> = mutableListOf()
    val additionalCurves: MutableList<Placeable?> = mutableListOf()

    lateinit var placeable: Placeable
    var x = 0
    var y = 0

    var minHeight = 0

    // Helper function to get all parents (primary + additional)
    fun getAllParents(): List<TreeNode<T>> {
        return listOfNotNull(parent) + additionalParents
    }
}

/**
 * Overloaded version of HorizontalTree that accepts a single root node.
 * This function is provided for backward compatibility.
 */
@Composable
fun <T> HorizontalTree(
    root: T,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(64.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    horizontalMinimumSpacing: Dp = 0.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    verticalMinimumSpacing: Dp = 0.dp,
    connection: @Composable (parent: T, parentRect: Rect, child: T, childRect: Rect) -> Unit = { _, _, _, _ -> },
    content: @Composable (node: T) -> Unit,
) {
    HorizontalTree(
        roots = listOf(root),
        getChildren = getChildren,
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        horizontalAlignment = horizontalAlignment,
        horizontalMinimumSpacing = horizontalMinimumSpacing,
        verticalArrangement = verticalArrangement,
        verticalAlignment = verticalAlignment,
        verticalMinimumSpacing = verticalMinimumSpacing,
        connection = connection,
        content = content
    )
}

/**
 * A horizontal tree layout that supports multiple root nodes.
 * Root nodes are displayed at the left side of the tree.
 */
@Composable
fun <T> HorizontalTree(
    roots: Collection<T>,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,

    // How the nodes are spaced out horizontally.
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(64.dp),
    // How child nodes are aligned with each other.
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    horizontalMinimumSpacing: Dp = 0.dp,

    // How nodes are spaced out vertically.
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    // How parent nodes are aligned to their children.
    // Also, how children with a taller parent are aligned.
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    verticalMinimumSpacing: Dp = 0.dp,

    // TODO support packing?
    //  - support top/bottom packing nodes at the same depth?
    //  - support left/right pack nodes against their parent? null horizontalArrangement?
    //  - these may need to be mutually-exclusive?

    // TODO support staggering nodes at the same depth?
    //  - more important in a top-down tree

    connection: @Composable (parent: T, parentRect: Rect, child: T, childRect: Rect) -> Unit = { _, _, _, _ -> },
    content: @Composable (node: T) -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        // Map to store unique nodes by their value
        val uniqueNodes = mutableMapOf<T, TreeNode<T>>()

        fun collect(sink: MutableList<TreeNode<T>>, value: T, depth: Int, parentNode: TreeNode<T>? = null): TreeNode<T> {
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
            val node = TreeNode(value, depth, children)
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

        val nodes = mutableListOf<TreeNode<T>>()
        val rootNodes = mutableListOf<TreeNode<T>>()

        // Process each root node
        for (rootValue in roots) {
            val rootNode = collect(nodes, rootValue, 0)
            rootNodes.add(rootNode)
        }

        // If no roots were provided, return an empty layout
        if (rootNodes.isEmpty()) {
            return@SubcomposeLayout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val byDepth = Array<MutableList<TreeNode<T>>>(nodes.maxOf { it.depth } + 1) { mutableListOf() }
        for (node in nodes) {
            byDepth[node.depth].add(node)
        }

        subcompose(TreeSubSlot.CONTENT) {
            for (node in nodes) {
                content(node.value)
            }
        }.forEachIndexed { index, measurable ->
            nodes[index].placeable = measurable.measure(Constraints())
        }

        // ====================================
        // Horizontal arrangement and alignment
        // ====================================
        val xSpacing = maxOf(horizontalArrangement.spacing, horizontalMinimumSpacing).roundToPx()

        val xSizes = IntArray(byDepth.size) { byDepth[it].maxOf { it.placeable.width } }
        val minWidth = maxOf(xSizes.sumOf { it + xSpacing } - xSpacing, constraints.minWidth)

        val xPositions = IntArray(byDepth.size)
        with(horizontalArrangement) { arrange(minWidth, xSizes, layoutDirection, xPositions) }
        for ((i, nodes) in byDepth.withIndex()) {
            val size = xSizes[i]
            for (node in nodes) {
                node.x = xPositions[i] + horizontalAlignment.align(node.placeable.width, size, layoutDirection)
            }
        }

        // ==================================
        // Vertical arrangement and alignment
        // ==================================
        // TODO there's still some weird things go on here:
        //  - SpacedBetween looks weird
        //  - SpaceEvenly results in double space between cousins
        // TODO do we need to use arrangement within minHeight calculation?

        val ySpacing = maxOf(verticalArrangement.spacing, verticalMinimumSpacing).roundToPx()

        fun heightUp(node: TreeNode<T>): Int {
            var childHeight = -ySpacing
            for (child in node.children) {
                // Only include children that don't have multiple parents
                // Shared children will be positioned independently
                if (child.getAllParents().size <= 1) {
                    childHeight += heightUp(child) + ySpacing
                }
            }
            node.minHeight = maxOf(node.placeable.height, childHeight)
            return node.minHeight
        }

        fun heightDown(node: TreeNode<T>, height: Int) {
            val original = node.minHeight
            node.minHeight = height

            val childSpacing = ySpacing * (node.children.size - 1)
            for (child in node.children) {
                val childHeight = (child.minHeight - childSpacing) * height / original + childSpacing
                child.minHeight = childHeight
                heightDown(child, childHeight)
            }
        }

        fun alignChildren(children: List<TreeNode<T>>, yOffset: Int, height: Int) {
            if (children.isEmpty()) return

            // Filter out children with multiple parents - they'll be positioned in post-processing
            val nonSharedChildren = children.filter { it.getAllParents().size <= 1 }
            if (nonSharedChildren.isEmpty()) return

            val ySizes = IntArray(nonSharedChildren.size) { nonSharedChildren[it].minHeight }
            val yPositions = IntArray(nonSharedChildren.size)
            with(verticalArrangement) { arrange(height, ySizes, yPositions) }

            val childrenOffset = if (verticalArrangement.spacing.value > 0f) {
                var min = height
                var max = 0
                for (i in nonSharedChildren.indices) {
                    min = minOf(min, yPositions[i])
                    max = maxOf(max, yPositions[i] + ySizes[i])
                }
                verticalAlignment.align(max - min, height)
            } else {
                0
            }

            for (i in yPositions.indices) {
                val child = nonSharedChildren[i]
                val yPosition = yOffset + yPositions[i]

                val childOffset = if (child.placeable.height < child.minHeight) {
                    verticalAlignment.align(child.placeable.height, child.minHeight)
                } else {
                    0
                }

                child.y = yPosition + childrenOffset + childOffset
                
                // Calculate the vertical center of the child node
                val childCenterY = child.y + child.placeable.height / 2
                
                // If the child has children, center them around the child's center
                if (child.children.isNotEmpty()) {
                    val childrenHeight = ySizes[i]
                    val childrenStartY = childCenterY - childrenHeight / 2
                    alignChildren(child.children, childrenStartY, ySizes[i])
                }
            }
        }

        // Calculate total height including spacing between root nodes
        var totalHeight = 0

        // First calculate the height of each root node
        for (rootNode in rootNodes) {
            heightUp(rootNode)
        }

        // Then calculate total height with consistent spacing
        if (rootNodes.isNotEmpty()) {
            // Sum up the heights of all root nodes
            val rootNodesHeight = rootNodes.sumOf { it.minHeight }

            // Add spacing between root nodes (spacing * (number of nodes - 1))
            val spacingBetweenRoots = if (rootNodes.size > 1) ySpacing * (rootNodes.size - 1) else 0

            // Total height is the sum of root node heights plus spacing between them
            totalHeight = rootNodesHeight + spacingBetweenRoots
        }

        // Apply height adjustments if needed
        if (verticalArrangement.spacing.value <= 0f) {
            totalHeight = maxOf(totalHeight, constraints.minHeight)
            for (rootNode in rootNodes) {
                heightDown(rootNode, rootNode.minHeight)
            }
        }

        // Align all root nodes and their children
        alignChildren(rootNodes, 0, totalHeight)

        // Post-processing step to adjust positioning for nodes with multiple parents
        for (node in nodes) {
            val allParents = node.getAllParents()
            if (allParents.size > 1) {
                // Sort parents by their y position
                val sortedParents = allParents.sortedBy { it.y }
                
                // Calculate the center position between first and last parent centers
                val firstParentCenterY = sortedParents.first().y + sortedParents.first().placeable.height / 2
                val lastParentCenterY = sortedParents.last().y + sortedParents.last().placeable.height / 2
                val parentsCenterY = (firstParentCenterY + lastParentCenterY) / 2
                
                // Position the child node centered with respect to the parent group
                node.y = parentsCenterY - node.placeable.height / 2
                
                // After repositioning the node, reposition its children to be centered around the new position
                if (node.children.isNotEmpty()) {
                    val childrenTotalHeight = node.children.sumOf { it.minHeight } + 
                                             ySpacing * (node.children.size - 1)
                    val newCenterY = node.y + node.placeable.height / 2
                    val childrenStartY = newCenterY - childrenTotalHeight / 2
                    
                    val ySizes = IntArray(node.children.size) { node.children[it].minHeight }
                    val yPositions = IntArray(node.children.size)
                    with(verticalArrangement) { arrange(childrenTotalHeight, ySizes, yPositions) }
                    
                    for (i in node.children.indices) {
                        val child = node.children[i]
                        val childOffset = if (child.placeable.height < child.minHeight) {
                            verticalAlignment.align(child.placeable.height, child.minHeight)
                        } else {
                            0
                        }
                        child.y = childrenStartY + yPositions[i] + childOffset
                    }
                }
            }
        }

        // Create a list of all parent-child connections
        val connections = mutableListOf<Pair<TreeNode<T>, TreeNode<T>>>()
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

        subcompose(TreeSubSlot.CONNECTION) {
            // Draw connections for all parent-child pairs
            for ((parent, child) in connections) {
                val offset = Offset(parent.x.toFloat(), minOf(parent.y, child.y).toFloat())
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

            val xStart = parent.x
            val xEnd = child.x + child.placeable.width
            val yStart = minOf(parent.y, child.y)
            val yEnd = maxOf(parent.y + parent.placeable.height, child.y + child.placeable.height)
            val constraints = Constraints.fixed(width = abs(xEnd - xStart), height = abs(yEnd - yStart))

            // Store the curve in the appropriate place
            if (child.parent == parent) {
                child.curve = measurable.measure(constraints)
            } else {
                // This is an additional parent, store the curve in additionalCurves
                child.additionalCurves.add(measurable.measure(constraints))
            }
        }

        layout(minWidth, maxOf(totalHeight, constraints.minHeight)) {
            for (node in nodes) {
                node.placeable.place(x = node.x, y = node.y)

                // Place curve for primary parent
                val parent = node.parent
                val curve = node.curve
                if (parent != null && curve != null) {
                    curve.place(x = parent.x, y = minOf(parent.y, node.y))
                }

                // Place curves for additional parents
                for (i in node.additionalParents.indices) {
                    val additionalParent = node.additionalParents[i]
                    val additionalCurve = if (i < node.additionalCurves.size) node.additionalCurves[i] else null

                    if (additionalCurve != null) {
                        additionalCurve.place(x = additionalParent.x, y = minOf(additionalParent.y, node.y))
                    }
                }
            }
        }
    }
}
