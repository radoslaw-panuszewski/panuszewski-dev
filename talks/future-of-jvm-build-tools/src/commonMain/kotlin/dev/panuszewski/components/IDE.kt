package dev.panuszewski.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Transition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.MagicCodeSample
import dev.panuszewski.template.body2
import dev.panuszewski.template.code2
import dev.panuszewski.template.toCode

@Composable
fun IDE(
    files: List<ProjectFile>,
    initialOpenFile: ProjectFile? = null,
    modifier: Modifier = Modifier
) {
    var currentOpenFile by remember { mutableStateOf(initialOpenFile ?: files.firstOrNull { !it.isFolder }) }
    var previousOpenFile by remember { mutableStateOf<ProjectFile?>(null) }

    // Build the file tree
    val fileTree = remember(files) { buildFileTree(files) }

    // Track expanded state of folders
    val expandedFolders = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = modifier
            .border(
                color = Color.Black,
                width = 1.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxSize()
    ) {
        // IDE toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E2E2))
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

        Divider(Modifier.background(Color(0xFFA6A7A6)))

        // Main content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFEFFFE))
        ) {
            // Project files panel (left)
            Box(
                modifier = Modifier
                    .width(275.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFF3F3F3))
                    .border(width = 1.dp, color = Color(0xFFDDDDDD))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Render the file tree
                    fileTree.forEach { node ->
                        item {
                            FileTreeItem(
                                node = node,
                                depth = 0,
                                expandedFolders = expandedFolders,
                                currentOpenFile = currentOpenFile,
                                onFileSelected = { file ->
                                    if (!file.isFolder && file != currentOpenFile) {
                                        previousOpenFile = currentOpenFile
                                        currentOpenFile = file
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Code display panel (center)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (currentOpenFile != null && previousOpenFile != null) {
                    AnimatedContent(
                        targetState = currentOpenFile!!,
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) { file ->
                        CodePanel(file = file)
                    }
                } else if (currentOpenFile != null) {
                    CodePanel(file = currentOpenFile!!)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No file selected")
                    }
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
    onFileSelected: (ProjectFile) -> Unit
) {
    val isExpanded = expandedFolders[node.path] ?: true
    val isSelected = node.file == currentOpenFile

    // Render this node
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFFD2E4FF) else Color.Transparent)
            .clickable {
                if (node.isFolder) {
                    // Toggle folder expansion
                    expandedFolders[node.path] = !(expandedFolders[node.path] ?: true)
                } else {
                    // Select file
                    onFileSelected(node.file)
                }
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indentation based on depth
        Spacer(modifier = Modifier.width((depth * 16).dp))

        // Folder expansion indicator
        if (node.isFolder) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "▼" else "▶",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        } else {
            Spacer(modifier = Modifier.width(16.dp))
        }

        // File/folder icon
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when {
                        node.isFolder -> Color(0xFFFFC107) // Folder color
                        node.file.language == Language.Kotlin -> Color(0xFF2196F3) // Kotlin file color
                        else -> Color(0xFF9E9E9E) // Other file color
                    },
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // File/folder name with different styling based on type
        body2 {
            Text(
                text = node.name,
                fontWeight = when {
                    node.isFolder -> FontWeight.Medium
                    else -> FontWeight.Normal
                },
                color = when {
                    isSelected -> Color(0xFF2C5BB7)
                    node.isFolder -> Color(0xFF6F5502)
                    else -> Color.Black
                }
            )
        }
    }

    // Render children if expanded
    if (node.isFolder && isExpanded) {
        node.children.forEach { childNode ->
            FileTreeItem(
                node = childNode,
                depth = depth + 1,
                expandedFolders = expandedFolders,
                currentOpenFile = currentOpenFile,
                onFileSelected = onFileSelected
            )
        }
    }
}


@Composable
private fun CodePanel(file: ProjectFile) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        code2 {
            file.content?.MagicCodeSample()
        }
    }
}

// Data structures for representing files and their content
data class ProjectFile(
    val name: String,
    val path: String,
    val isFolder: Boolean = false,
    val content: Transition<CodeSample>? = null,
    val language: Language = Language.Kotlin,
    val children: List<ProjectFile> = emptyList()
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
    val folders = files.filter { it.isFolder }

    // Process all files in the original order they're provided
    for (file in files) {
        // Create a node for this file/folder
        val isFolder = file.isFolder

        // Determine the node name for files - include unmatched path parts if any
        val nodeName = if (!isFolder) {
            val parentPath = findParentFolder(file.path, folders)
            if (parentPath != null) {
                // Extract the part of the path that doesn't match the parent folder
                val unmatchedPath = file.path.substring(parentPath.length + 1) // +1 to skip the slash
                if (unmatchedPath.contains("/")) {
                    // If there are unmatched path parts, include them in the node name
                    unmatchedPath
                } else {
                    // Otherwise, just use the file name
                    file.name
                }
            } else {
                // No parent folder, use the file path as the node name
                file.path
            }
        } else {
            // For folders, use the name as is
            file.name
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