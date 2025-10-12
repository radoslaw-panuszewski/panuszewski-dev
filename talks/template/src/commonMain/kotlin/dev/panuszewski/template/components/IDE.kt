package dev.panuszewski.template.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.MagicText
import dev.panuszewski.template.extensions.code2
import dev.panuszewski.template.extensions.code3
import dev.panuszewski.template.extensions.endBorder
import dev.panuszewski.template.theme.IdeColorScheme
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.withColor
import kotlinx.coroutines.delay

data class IdeState(
    val files: List<ProjectFile>,
    val selectedFile: String? = null,
    val leftPaneFile: String? = null,
    val rightPaneFile: String? = null,
    val fileTreeHidden: Boolean = false,
    val enlargedFile: String? = null,
    val highlightedFile: String? = null,
    val fileTreeWidth: Dp? = null,
    val emoji: String? = null,
    val image: DrawableResource? = null,
    val errorText: String? = null,
    val openPanels: Set<String> = emptySet(),
    val panelStates: Map<String, Int> = emptyMap(),
    val state: Int = 0,
    val title: String? = null,
    val fileRenames: Map<String, String> = emptyMap()
)

@Composable
fun IDE(ideState: IdeState, modifier: Modifier = Modifier) {
    val ideColors = LocalIdeColors.current
    with(ideState) {
        val selectedFile = files.find { it.path == selectedFile }
        val enlargedFile = files.find { it.path == enlargedFile }
        val highlightedFile = files.find { it.path == highlightedFile }

        // Find files in left and right panes
        var leftPaneFile = files.find { it.path == leftPaneFile }
        val rightPaneFile = files.find { it.path == rightPaneFile }

        if (leftPaneFile == null && rightPaneFile == null) {
            leftPaneFile = selectedFile
        }
        

        // Determine the current mode
        val isSplitPaneMode = leftPaneFile != null || rightPaneFile != null
        val isBothPanesMode = leftPaneFile != null && rightPaneFile != null

        // Build the file tree
        val fileTree = remember(files) { buildFileTree(files) }
        
        val allFilePaths = remember(files) { files.map { it.path }.toSet() }
        val previousFilePaths = remember { mutableStateOf(setOf<String>()) }
        
        val newlyAddedPaths = remember(allFilePaths) {
            allFilePaths - previousFilePaths.value
        }
        
        val removedPaths = remember(allFilePaths) {
            previousFilePaths.value - allFilePaths
        }
        
        val visiblePaths = remember { mutableStateOf(setOf<String>()) }

        LaunchedEffect(allFilePaths) {
            if (newlyAddedPaths.isNotEmpty()) {
                visiblePaths.value = allFilePaths - newlyAddedPaths
                delay(50)
                visiblePaths.value = allFilePaths
            } else if (removedPaths.isNotEmpty()) {
                visiblePaths.value = allFilePaths
            } else {
                visiblePaths.value = allFilePaths
            }
            previousFilePaths.value = allFilePaths
        }

        // Track expanded state of folders
        val expandedFolders = remember { mutableStateMapOf<String, Boolean>() }


        // Determine if the file is moving to left or right pane
        val isMovingToLeftPane = selectedFile != null && selectedFile == leftPaneFile
        val isMovingToRightPane = selectedFile != null && selectedFile == rightPaneFile

        // Animation for code panel position
        val codePanelOffset = animateFloatAsState(
            targetValue = when {
                isMovingToLeftPane -> -0.25f // Move to left
                isMovingToRightPane -> 0.25f // Move to right
                else -> 0f // Center
            },
            animationSpec = tween(durationMillis = 300),
            label = "codePanelOffset"
        )

        Column(
            modifier = modifier
                .border(
                    color = ideColors.border,
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
        ) {
            // IDE toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ideColors.toolbar)
                    .padding(vertical = 6.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 6.dp)
                        .width(10.dp)
                        .height(10.dp)
                        .background(Color(0xFFFF605C), shape = RoundedCornerShape(50))
                )
                Box(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .width(10.dp)
                        .height(10.dp)
                        .background(Color(0xFFFFBD44), shape = RoundedCornerShape(50))
                )
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .background(Color(0xFF00CA4E), shape = RoundedCornerShape(50))
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Divider(Modifier.background(ideColors.toolbarBorder))

            // File tree animated visibility synchronized with scene timing
            val showFileTree = !fileTreeHidden

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(ideColors.background)
                ) {
                // File tree panel with scene-controlled animated width
                val actualFileTreeWidth = fileTreeWidth ?: (if (showFileTree) 200.dp else 0.dp)

                Box(
                    modifier = Modifier
                        .width(actualFileTreeWidth)
                        .fillMaxHeight()
                        .clipToBounds()
                ) {
                    if (actualFileTreeWidth > 0.dp) {
                        Box(
                            modifier = Modifier
                                .width(275.dp)
                                .fillMaxHeight()
                                .background(ideColors.fileTreeBackground)
                                .endBorder(width = 1.dp, color = ideColors.fileTreeBorder)
                        ) {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(
                                    items = fileTree,
                                    key = { node -> node.path }
                                ) { node ->
                                    val useVisibilityTransition = node.file?.visibilityTransition != null && !node.isFolder
                                    if (useVisibilityTransition) {
                                        val isVisible = node.file!!.visibilityTransition!!.targetState
                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = isVisible,
                                            enter = expandVertically(
                                                animationSpec = tween(durationMillis = 300)
                                            ) + fadeIn(
                                                animationSpec = tween(durationMillis = 300)
                                            ),
                                            exit = shrinkVertically(
                                                animationSpec = tween(durationMillis = 300)
                                            ) + fadeOut(
                                                animationSpec = tween(durationMillis = 300)
                                            )
                                        ) {
                                            FileTreeItem(
                                                node = node,
                                                depth = 0,
                                                expandedFolders = expandedFolders,
                                                currentOpenFile = selectedFile,
                                                enlargedFile = enlargedFile,
                                                highlightedFile = highlightedFile,
                                                ideColors = ideColors,
                                                visiblePathsState = visiblePaths,
                                                fileRenames = fileRenames
                                            )
                                        }
                                    } else {
                                        FileTreeItem(
                                            node = node,
                                            depth = 0,
                                            expandedFolders = expandedFolders,
                                            currentOpenFile = selectedFile,
                                            enlargedFile = enlargedFile,
                                            highlightedFile = highlightedFile,
                                            ideColors = ideColors,
                                            visiblePathsState = visiblePaths,
                                            fileRenames = fileRenames
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Code display area
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CodeDisplayArea(
                        isSplitPaneMode = isSplitPaneMode,
                        leftPaneFile = leftPaneFile,
                        rightPaneFile = rightPaneFile,
                        selectedFile = selectedFile,
                        codePanelOffset = codePanelOffset,
                        ideColors = ideColors,
                        fileRenames = fileRenames
                    )
                }
            }

            Divider(Modifier.background(ideColors.toolbarBorder))

            AnimatedVisibility(
                visible = errorText != null,
                enter = expandVertically(tween(300), expandFrom = Alignment.Bottom),
                exit = shrinkVertically(tween(300), shrinkTowards = Alignment.Bottom),
                modifier = Modifier.fillMaxWidth()
            ) {
                val contentAlpha = remember { mutableStateOf(0f) }
                val currentErrorText = remember { mutableStateOf<String?>(null) }
                
                LaunchedEffect(errorText) {
                    if (errorText != null) {
                        currentErrorText.value = errorText
                        contentAlpha.value = 0f
                        delay(300)
                        contentAlpha.value = 1f
                    } else {
                        contentAlpha.value = 1f
                        delay(300)
                        contentAlpha.value = 0f
                    }
                }
                
                val animatedAlpha by animateFloatAsState(
                    targetValue = contentAlpha.value,
                    animationSpec = tween(200),
                    label = "contentAlpha"
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ideColors.codePanelBackground)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        currentErrorText.value?.let {
                            Text(
                                text = it,
                                color = Color(0xFFFF6B68),
                                modifier = Modifier.graphicsLayer { alpha = animatedAlpha }
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun CodeDisplayArea(
    isSplitPaneMode: Boolean,
    leftPaneFile: ProjectFile?,
    rightPaneFile: ProjectFile?,
    selectedFile: ProjectFile?,
    codePanelOffset: State<Float>,
    ideColors: IdeColorScheme,
    fileRenames: Map<String, String> = emptyMap()
) {
    val leftPaneWeight by animateFloatAsState(
        targetValue = if (leftPaneFile != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "leftPaneWeight"
    )
    
    val rightPaneWeight by animateFloatAsState(
        targetValue = if (rightPaneFile != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "rightPaneWeight"
    )
    
    val displayLeftPaneFile = remember { mutableStateOf(leftPaneFile) }
    val displayRightPaneFile = remember { mutableStateOf(rightPaneFile) }
    
    LaunchedEffect(leftPaneFile) {
        if (leftPaneFile != null) {
            displayLeftPaneFile.value = leftPaneFile
        } else {
            delay(300)
            displayLeftPaneFile.value = null
        }
    }
    
    LaunchedEffect(rightPaneFile) {
        if (rightPaneFile != null) {
            displayRightPaneFile.value = rightPaneFile
        } else {
            delay(300)
            displayRightPaneFile.value = null
        }
    }
    
    if (isSplitPaneMode) {
        // Split-pane mode
        Row(
            modifier = Modifier
                .fillMaxSize()
                    ) {
                        // Left pane
                        if (leftPaneWeight > 0f) {
                            Box(
                                modifier = Modifier
                                    .weight(leftPaneWeight)
                                    .fillMaxHeight()
                                    .clip(RectangleShape)
                                    .graphicsLayer { alpha = leftPaneWeight }
                            ) {
                                if (displayLeftPaneFile.value != null) {
                                    Column {
                                        // Tab for left pane
                                        Box(
                                            modifier = Modifier
                                                .background(ideColors.tabBackground)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            code3 {
                                                val displayName = fileRenames[displayLeftPaneFile.value!!.path] ?: displayLeftPaneFile.value!!.name
                                                Text(
                                                    text = displayName,
                                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                                    color = ideColors.textPrimary
                                                )
                                            }
                                        }

                                        Divider()

                                        // File content
                                        AnimatedContent(
                                            targetState = displayLeftPaneFile.value,
                                            transitionSpec = { fadeIn() togetherWith fadeOut() }
                                        ) { file ->
                                            if (file != null) {
                                                CodePanel(file = file, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp))
                                            }
                                        }
                                    }
                                } else {
                                    // Gray out the empty pane
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(ideColors.paneBackground),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No file in left pane", color = ideColors.textPrimary)
                                    }
                                }
                            }
                        }

                        // Vertical separator
                        if (leftPaneWeight > 0f && rightPaneWeight > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                                    .background(ideColors.paneSeparator)
                                    .padding(vertical = 8.dp)
                            )
                        }

                        // Right pane
                        if (rightPaneWeight > 0f) {
                            Box(
                                modifier = Modifier
                                    .weight(rightPaneWeight)
                                    .fillMaxHeight()
                                    .clip(RectangleShape)
                                    .graphicsLayer { alpha = rightPaneWeight }
                            ) {
                                if (displayRightPaneFile.value != null) {
                                    Column {
                                        // Tab for right pane
                                        Box(
                                            modifier = Modifier
                                                .background(ideColors.tabBackground)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            code3 {
                                                val displayName = fileRenames[displayRightPaneFile.value!!.path] ?: displayRightPaneFile.value!!.name
                                                Text(
                                                    text = displayName,
                                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                                    color = ideColors.textPrimary
                                                )
                                            }
                                        }

                                        Divider()

                                        // File content
                                        AnimatedContent(
                                            targetState = displayRightPaneFile.value,
                                            transitionSpec = { fadeIn() togetherWith fadeOut() }
                                        ) { file ->
                                            if (file != null) {
                                                CodePanel(file = file, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp))
                                            }
                                        }
                                    }
                                } else {
                                    // Gray out the empty pane
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(ideColors.paneBackground),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No file in right pane", color = ideColors.textPrimary)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Normal mode - single code panel with animation
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RectangleShape)
                    ) {
                        if (selectedFile != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    // Apply offset animation when transitioning to split-pane mode
                                    .padding(
                                        start = (16.dp * (1 + codePanelOffset.value)).coerceAtLeast(0.dp),
                                        end = (16.dp * (1 - codePanelOffset.value)).coerceAtLeast(0.dp)
                                    )
                            ) {
                                AnimatedContent(
                                    targetState = selectedFile,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                                ) { file ->
                                    CodePanel(file = file, modifier = Modifier.padding(top = 16.dp))
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No file selected", color = ideColors.textPrimary)
                            }
                        }
                    }
                }
            }

@Composable
private fun FileTreeItem(
    node: FileTreeNode,
    depth: Int,
    expandedFolders: MutableMap<String, Boolean>,
    currentOpenFile: ProjectFile?,
    enlargedFile: ProjectFile?,
    highlightedFile: ProjectFile?,
    ideColors: IdeColorScheme,
    modifier: Modifier = Modifier,
    visiblePathsState: State<Set<String>>,
    fileRenames: Map<String, String> = emptyMap()
) {
    val isExpanded = expandedFolders[node.path] ?: true
    val isSelected = node.file == currentOpenFile
    val isEnlarged = node.file == enlargedFile
    val isHighlighted = node.file == highlightedFile

    val iconSize by animateDpAsState(targetValue = if (isEnlarged) 20.dp else 16.dp)
    val spacerWidth by animateDpAsState(targetValue = if (isEnlarged) 12.dp else 8.dp)
    
    val displayName = fileRenames[node.path] ?: node.name

    val hasVisibilityTransition = node.file?.visibilityTransition != null && node.isFolder
    
    if (hasVisibilityTransition) {
        val isVisible = node.file!!.visibilityTransition!!.targetState
        androidx.compose.animation.AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300)
            ),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            FileTreeItemContent(
                node, depth, isExpanded, isSelected, isEnlarged, isHighlighted,
                iconSize, spacerWidth, expandedFolders, currentOpenFile, enlargedFile,
                highlightedFile, ideColors, modifier, visiblePathsState, displayName, fileRenames
            )
        }
    } else {
        FileTreeItemContent(
            node, depth, isExpanded, isSelected, isEnlarged, isHighlighted,
            iconSize, spacerWidth, expandedFolders, currentOpenFile, enlargedFile,
            highlightedFile, ideColors, modifier, visiblePathsState, displayName, fileRenames
        )
    }
}

@Composable
private fun FileTreeItemContent(
    node: FileTreeNode,
    depth: Int,
    isExpanded: Boolean,
    isSelected: Boolean,
    isEnlarged: Boolean,
    isHighlighted: Boolean,
    iconSize: Dp,
    spacerWidth: Dp,
    expandedFolders: MutableMap<String, Boolean>,
    currentOpenFile: ProjectFile?,
    enlargedFile: ProjectFile?,
    highlightedFile: ProjectFile?,
    ideColors: IdeColorScheme,
    modifier: Modifier = Modifier,
    visiblePathsState: State<Set<String>>,
    displayName: String,
    fileRenames: Map<String, String> = emptyMap()
) {
    Column {
        // Render this node
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    when {
                        isHighlighted -> ideColors.highlightedFileBackground
                        isEnlarged -> Color.Transparent
                        isSelected -> ideColors.selectedFileBackground
                        else -> Color.Transparent
                    }
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indentation based on depth
            Spacer(modifier = Modifier.width((depth * 16).dp))

            Spacer(modifier = Modifier.width(16.dp))

            // File/folder icon
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .background(
                        color = when {
                            node.isFolder -> ideColors.folderIcon
                            node.file.language == Language.Kotlin -> ideColors.kotlinFileIcon
                            else -> ideColors.genericFileIcon
                        },
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.width(spacerWidth))

            // File/folder name with different styling based on type
            val progress by animateFloatAsState(
                targetValue = if (isEnlarged) 1f else 0f,
                label = "textStyleAnimation"
            )
            val textStyle = lerp(
                MaterialTheme.typography.body2,
                MaterialTheme.typography.h6,
                progress
            )

            ProvideTextStyle(textStyle) {
                val textStyle = TextStyle(
                    fontWeight = when {
                        node.isFolder -> FontWeight.Medium
                        else -> FontWeight.Normal
                    },
                    color = when {
                        isEnlarged -> ideColors.textPrimary
                        isSelected -> ideColors.textPrimary
                        node.isFolder -> ideColors.textPrimary
                        else -> ideColors.textPrimary
                    }
                )
                ProvideTextStyle(textStyle) {
                    androidx.compose.runtime.key(node.path) {
                        MagicText(text = buildAnnotatedString {
                            if (displayName.contains(".")) {
                                val beforeExtension = displayName.substringBeforeLast(".")
                                val afterExtension = displayName.substringAfterLast(".")
                                append(beforeExtension)
                                append(".")
                                if (afterExtension == "dcl") {
                                    withColor(Color(0xFFFF8A04)) { append(afterExtension) }
                                } else {
                                    append(afterExtension)
                                }
                            } else {
                                append(displayName)
                            }
                        })
                    }
                }
            }
        }

        // Render children if expanded
        if (node.isFolder && isExpanded) {
            node.children.forEach { childNode ->
                val useVisibilityTransition = childNode.file?.visibilityTransition != null
                val isVisible = if (useVisibilityTransition) {
                    childNode.file!!.visibilityTransition!!.targetState
                } else {
                    childNode.path in visiblePathsState.value
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = isVisible,
                    enter = expandVertically(
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = shrinkVertically(
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    FileTreeItem(
                        node = childNode,
                        depth = depth + 1,
                        expandedFolders = expandedFolders,
                        currentOpenFile = currentOpenFile,
                        enlargedFile = enlargedFile,
                        highlightedFile = highlightedFile,
                        ideColors = ideColors,
                        modifier = modifier,
                        visiblePathsState = visiblePathsState,
                        fileRenames = fileRenames
                    )
                }
            }
        }
    }
}

@Composable
private fun CodePanel(file: ProjectFile, modifier: Modifier = Modifier) {
    val ideColors = LocalIdeColors.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ideColors.codePanelBackground)
    ) {
        code2 {
            file.content?.ScrollableEnhancedMagicCodeSample(
                scrollMargin = 8,
                skipIndentationInWarnings = true,
            ) ?: file.staticContent?.MagicAnnotatedString()
        }
    }
}

// Data structures for representing files and their content
data class ProjectFile(
    val name: String,
    val path: String = name,
    val isDirectory: Boolean = false,
    val content: Transition<CodeSample>? = null,
    val staticContent: Transition<AnnotatedString>? = null,
    val language: Language = Language.Kotlin,
    val children: List<ProjectFile> = emptyList(),
    val visibilityTransition: Transition<Boolean>? = null,
)

// Represents a node in the file tree
data class FileTreeNode(
    val name: String,
    val path: String,
    val isFolder: Boolean,
    val file: ProjectFile,
    val children: MutableList<FileTreeNode> = mutableListOf()
)

// Converts a flat list of ProjectFiles to a hierarchical tree structure
fun buildFileTree(files: List<ProjectFile>): List<FileTreeNode> {
    val rootNodes = mutableListOf<FileTreeNode>()
    val pathMap = mutableMapOf<String, FileTreeNode>()

    // Collect all folders for parent-child relationship lookups
    val folders = files.filter { it.isDirectory }

    // Process all files in the original order they're provided
    for (file in files) {
        // Create a node for this file/folder
        val isFolder = file.isDirectory

        // Determine the node name - include unmatched path parts if any
        val nodeName = run {
            val parentPath = findParentFolder(file.path, folders)
            if (parentPath != null) {
                // Extract the part of the path that doesn't match the parent folder
                val unmatchedPath = file.path.substring(parentPath.length + 1)
                if (unmatchedPath.contains("/")) {
                    unmatchedPath
                } else {
                    file.name
                }
            } else {
                if (!isFolder && file.path.contains("/")) {
                    file.path
                } else {
                    file.name
                }
            }
        }

        val node = FileTreeNode(nodeName, file.path, isFolder, file)
        pathMap[file.path] = node

        // Find parent folder if it exists
        val parentPath = findParentFolder(file.path, folders)
        if (parentPath != null) {
            val parentNode = pathMap[parentPath]
            if (parentNode != null) {
                parentNode.children.add(node)
            } else {
                // Parent folder not found in pathMap yet, add to root
                // This can happen if we process a child before its parent
                rootNodes.add(node)
            }
        } else {
            // No parent folder, add to root
            rootNodes.add(node)
        }
    }

    return rootNodes
}

// Find the parent folder for a given path
private fun findParentFolder(path: String, folders: List<ProjectFile>): String? {
    // If the path doesn't contain a slash, it's a root item
    if (!path.contains("/")) {
        return null
    }

    // Get the directory part of the path
    val lastSlashIndex = path.lastIndexOf('/')
    val parentPath = path.substring(0, lastSlashIndex)

    // Check if this exact path exists as a folder
    if (folders.any { it.path == parentPath }) {
        return parentPath
    }

    // Find the longest matching folder path
    return folders
        .filter { path.startsWith(it.path + "/") }
        .maxByOrNull { it.path.length }
        ?.path
}

fun MutableList<ProjectFile>.addFile(
    name: String,
    path: String = name,
    content: Transition<CodeSample>? = null,
    staticContent: Transition<AnnotatedString>? = null
) {
    val file = ProjectFile(name = name, path = path, content = content, staticContent = staticContent)
    add(file)
}

fun MutableList<ProjectFile>.addDirectory(name: String, path: String = name) {
    val file = ProjectFile(name = name, path = path, isDirectory = true)
    add(file)
}
