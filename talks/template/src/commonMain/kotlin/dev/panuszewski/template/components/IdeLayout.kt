package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource

typealias PanelContent = @Composable (Transition<Int>) -> Unit

data class PanelDefinition(
    val content: PanelContent,
    val adaptive: Boolean = false,
    val openAt: List<Int> = emptyList()
)

class IdeLayoutScope internal constructor() {
    internal val topPanels = mutableMapOf<String, PanelDefinition>()
    internal val leftPanels = mutableMapOf<String, PanelDefinition>()
    var centerEmojiVisibleAt: List<Int> = emptyList()
    var centerEmojiContent: PanelContent? = null

    fun topPanel(name: String, content: PanelContent) {
        topPanels[name] = PanelDefinition(content = content, adaptive = false)
    }

    fun topPanel(name: String, openAt: List<Int>, content: PanelContent) {
        topPanels[name] = PanelDefinition(content = content, adaptive = false, openAt = openAt)
    }

    fun topPanel(name: String, openAt: Int, content: PanelContent) {
        topPanel(name, listOf(openAt), content)
    }

    fun topPanel(name: String, openAt: IntRange, content: PanelContent) {
        topPanel(name, openAt.toList(), content)
    }

    fun topPanel(content: PanelContent) {
        topPanels["default"] = PanelDefinition(content = content, adaptive = false)
    }

    fun topPanel(openAt: List<Int>, content: PanelContent) {
        topPanels["default"] = PanelDefinition(content = content, adaptive = false, openAt = openAt)
    }

    fun topPanel(openAt: Int, content: PanelContent) {
        topPanel(openAt = listOf(openAt), content)
    }

    fun topPanel(openAt: IntRange, content: PanelContent) {
        topPanel(openAt = openAt.toList(), content)
    }

    fun adaptiveTopPanel(name: String, content: PanelContent) {
        topPanels[name] = PanelDefinition(content = content, adaptive = true)
    }

    fun adaptiveTopPanel(name: String, openAt: List<Int>, content: PanelContent) {
        topPanels[name] = PanelDefinition(content = content, adaptive = true, openAt = openAt)
    }

    fun adaptiveTopPanel(name: String, openAt: Int, content: PanelContent) {
        adaptiveTopPanel(name, listOf(openAt), content)
    }

    fun adaptiveTopPanel(name: String, openAt: IntRange, content: PanelContent) {
        adaptiveTopPanel(name, openAt.toList(), content)
    }

    fun leftPanel(name: String, content: PanelContent) {
        leftPanels[name] = PanelDefinition(content = content, adaptive = false)
    }

    fun leftPanel(name: String, openAt: List<Int>, content: PanelContent) {
        leftPanels[name] = PanelDefinition(content = content, adaptive = false, openAt = openAt)
    }

    fun leftPanel(name: String, openAt: Int, content: PanelContent) {
        leftPanel(name, listOf(openAt), content)
    }

    fun leftPanel(name: String, openAt: IntRange, content: PanelContent) {
        leftPanel(name, openAt.toList(), content)
    }

    fun leftPanel(content: PanelContent) {
        leftPanels["default"] = PanelDefinition(content = content, adaptive = false)
    }

    fun leftPanel(openAt: List<Int>, content: PanelContent) {
        leftPanels["default"] = PanelDefinition(content = content, adaptive = false, openAt = openAt)
    }

    fun leftPanel(openAt: Int, content: PanelContent) {
        leftPanel(openAt = listOf(openAt), content)
    }

    fun leftPanel(openAt: IntRange, content: PanelContent) {
        leftPanel(openAt = openAt.toList(), content)
    }

    fun adaptiveLeftPanel(name: String, content: PanelContent) {
        leftPanels[name] = PanelDefinition(content = content, adaptive = true)
    }

    fun adaptiveLeftPanel(name: String, openAt: List<Int>, content: PanelContent) {
        leftPanels[name] = PanelDefinition(content = content, adaptive = true, openAt = openAt)
    }

    fun adaptiveLeftPanel(name: String, openAt: Int, content: PanelContent) {
        adaptiveLeftPanel(name, listOf(openAt), content)
    }

    fun adaptiveLeftPanel(name: String, openAt: IntRange, content: PanelContent) {
        adaptiveLeftPanel(name, openAt.toList(), content)
    }

    fun centerEmoji(visibleAt: List<Int>, content: PanelContent) {
        centerEmojiVisibleAt = visibleAt
        centerEmojiContent = content
    }

    fun centerEmoji(visibleAt: Int, content: PanelContent) {
        centerEmoji(listOf(visibleAt), content)
    }
}

data class FileStateMapping(
    val selectedFile: String,
    val fileStates: Map<String, Int>,
    val emoji: String? = null,
    val image: DrawableResource? = null,
    val leftPaneFile: String? = null,
    val rightPaneFile: String? = null,
    val fileTreeHidden: Boolean = false,
    val errorText: String? = null,
    val openPanels: Set<String> = emptySet(),
    val panelStates: Map<String, Int> = emptyMap(),
    val pausedPanels: Set<String> = emptySet(),
    val activePanels: Set<String> = emptySet(),
    val title: String? = null,
    val fileRenames: Map<String, String> = emptyMap(),
    val revealedFiles: Set<String> = emptySet(),
    val enlargedFile: String? = null
)

private fun getAllParentPaths(path: String): List<String> {
    val parents = mutableListOf<String>()
    var currentPath = path
    while (currentPath.contains('/')) {
        currentPath = currentPath.substringBeforeLast('/')
        parents.add(currentPath)
    }
    return parents
}

fun buildFileStateMapping(
    initialFile: String,
    allCodeSamples: Map<String, List<CodeSample>>,
    title: String?,
    initiallyHiddenDirectories: Set<String> = emptySet()
): List<FileStateMapping> {
    val mappings = mutableListOf<FileStateMapping>()
    var currentFile = initialFile
    val fileStates = mutableMapOf<String, Int>().apply {
        allCodeSamples.keys.forEach { put(it, 0) }
    }
    var globalState = 0
    var currentEmoji: String? = null
    var currentImage: DrawableResource? = null
    var leftPaneFile: String? = null
    var rightPaneFile: String? = null
    var fileTreeHidden = false
    var errorText: String? = null
    val openPanels = mutableSetOf<String>()
    val panelStates = mutableMapOf<String, Int>()
    val pausedPanels = mutableSetOf<String>()
    val activePanels = mutableSetOf<String>()
    var currentTitle: String? = title
    val fileRenames = mutableMapOf<String, String>()
    val revealedFiles = mutableSetOf<String>()
    var enlargedFile: String? = null

    mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
    fileStates[currentFile] = 1

    while (true) {
        val currentFileSamples = allCodeSamples[currentFile] ?: break
        val currentFileState = fileStates[currentFile] ?: 0

        if (currentFileState >= currentFileSamples.size) break

        val sample = currentFileSamples.getOrNull(currentFileState)
        val chainedOps = sample?.data as? ChainedOperations
        val switchMarker = sample?.data as? SwitchToFile
        val showEmojiMarker = sample?.data as? ShowEmoji
        val hideEmojiMarker = sample?.data === HideEmoji
        val showImageMarker = sample?.data as? ShowImage
        val hideImageMarker = sample?.data === HideImage
        val openLeftMarker = sample?.data as? OpenInLeftPane
        val openRightMarker = sample?.data as? OpenInRightPane
        val closeLeftMarker = sample?.data === CloseLeftPane
        val closeRightMarker = sample?.data === CloseRightPane
        val hideFileTreeMarker = sample?.data === HideFileTree
        val showFileTreeMarker = sample?.data === ShowFileTree
        val openErrorMarker = sample?.data as? OpenErrorWindow
        val closeErrorMarker = sample?.data === CloseErrorWindow
        val advanceTogetherMarker = sample?.data as? AdvanceTogetherWith
        val openPanelMarker = sample?.data as? OpenNamedPanel
        val closePanelMarker = sample?.data as? CloseNamedPanel
        val pausePanelMarker = sample?.data as? PauseNamedPanel
        val resumePanelMarker = sample?.data as? ResumeNamedPanel
        val changeTitleMarker = sample?.data as? ChangeTitle
        val renameFileMarker = sample?.data as? RenameSelectedFile
        val revealFileMarker = sample?.data as? RevealFile
        val hideFileMarker = sample?.data as? HideFile
        val enlargeSelectedFileMarker = sample?.data === EnlargeSelectedFile
        val shrinkSelectedFileMarker = sample?.data === ShrinkSelectedFile

        if (chainedOps != null) {
            globalState++
            val panelsOpenedInThisFrame = mutableSetOf<String>()
            for (operation in chainedOps.operations) {
                when (operation) {
                    CloseLeftPane -> {
                        leftPaneFile = null
                        if (rightPaneFile != null) {
                            currentFile = rightPaneFile!!
                        }
                    }
                    CloseRightPane -> {
                        rightPaneFile = null
                        if (leftPaneFile != null) {
                            currentFile = leftPaneFile!!
                        }
                    }
                    HideFileTree -> {
                        fileTreeHidden = true
                    }
                    ShowFileTree -> {
                        fileTreeHidden = false
                    }
                    is ShowEmoji -> {
                        currentEmoji = operation.emoji
                    }
                    HideEmoji -> {
                        currentEmoji = null
                    }
                    is ShowImage -> {
                        currentImage = operation.imageResource
                    }
                    HideImage -> {
                        currentImage = null
                    }
                    is OpenInLeftPane -> {
                        leftPaneFile = operation.fileName
                        rightPaneFile = currentFile
                        if (leftPaneFile !in fileStates) {
                            fileStates[leftPaneFile!!] = 0
                        }
                        revealedFiles.add(operation.fileName)
                        getAllParentPaths(operation.fileName).forEach { parentPath ->
                            if (parentPath in initiallyHiddenDirectories) {
                                revealedFiles.add(parentPath)
                            }
                        }
                        if (operation.switchTo) {
                            currentFile = operation.fileName
                        }
                    }
                    is OpenInRightPane -> {
                        leftPaneFile = currentFile
                        rightPaneFile = operation.fileName
                        if (rightPaneFile !in fileStates) {
                            fileStates[rightPaneFile!!] = 0
                        }
                        revealedFiles.add(operation.fileName)
                        getAllParentPaths(operation.fileName).forEach { parentPath ->
                            if (parentPath in initiallyHiddenDirectories) {
                                revealedFiles.add(parentPath)
                            }
                        }
                        if (operation.switchTo) {
                            currentFile = operation.fileName
                        }
                    }
                    is OpenErrorWindow -> {
                        errorText = operation.text
                    }
                    CloseErrorWindow -> {
                        errorText = null
                    }
                    is OpenNamedPanel -> {
                        val wasNewlyOpened = operation.name !in panelStates
                        openPanels.add(operation.name)
                        activePanels.add(operation.name)
                        if (wasNewlyOpened) {
                            panelStates[operation.name] = 0
                            panelsOpenedInThisFrame.add(operation.name)
                        }
                    }
                    is CloseNamedPanel -> {
                        openPanels.remove(operation.name)
                        activePanels.remove(operation.name)
                    }
                    is PauseNamedPanel -> {
                        pausedPanels.add(operation.name)
                        activePanels.remove(operation.name)
                    }
                    is ResumeNamedPanel -> {
                        pausedPanels.remove(operation.name)
                        activePanels.add(operation.name)
                        if (operation.name !in panelStates) {
                            panelStates[operation.name] = 0
                        }
                    }
                    is ChangeTitle -> {
                        currentTitle = operation.title
                    }
                    is RenameSelectedFile -> {
                        fileRenames[currentFile] = operation.newName
                    }
                    is RevealFile -> {
                        revealedFiles.add(operation.fileName)
                        getAllParentPaths(operation.fileName).forEach { parentPath ->
                            if (parentPath in initiallyHiddenDirectories) {
                                revealedFiles.add(parentPath)
                            }
                        }
                    }
                    is HideFile -> {
                        revealedFiles.remove(operation.fileName)
                        getAllParentPaths(operation.fileName).forEach { parentPath ->
                            if (parentPath in initiallyHiddenDirectories) {
                                val hasOtherVisibleChildren = revealedFiles.any { it != operation.fileName && it.startsWith("$parentPath/") }
                                if (!hasOtherVisibleChildren) {
                                    revealedFiles.remove(parentPath)
                                }
                            }
                        }
                    }
                    EnlargeSelectedFile -> {
                        enlargedFile = currentFile
                    }
                    ShrinkSelectedFile -> {
                        enlargedFile = null
                    }
                }
            }
            for (panelName in activePanels) {
                if (panelName !in panelsOpenedInThisFrame) {
                    panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
                }
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (advanceTogetherMarker != null) {
            globalState++
            val otherFile = advanceTogetherMarker.fileName
            if (otherFile in fileStates) {
                fileStates[otherFile] = (fileStates[otherFile] ?: 0) + 1
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (switchMarker != null) {
            globalState++
            val previousFile = currentFile
            currentFile = switchMarker.fileName
            if (currentFile !in fileStates) {
                fileStates[currentFile] = 0
            }
            
            revealedFiles.add(currentFile)
            getAllParentPaths(currentFile).forEach { parentPath ->
                if (parentPath in initiallyHiddenDirectories) {
                    revealedFiles.add(parentPath)
                }
            }

            if (previousFile == leftPaneFile) {
                leftPaneFile = currentFile
            } else if (previousFile == rightPaneFile) {
                rightPaneFile = currentFile
            }

            fileStates[previousFile] = currentFileState + 1

            while (true) {
                val newFileState = fileStates[currentFile] ?: 0
                val newFileSamples = allCodeSamples[currentFile] ?: break
                if (newFileState >= newFileSamples.size) break

                val newSample = newFileSamples.getOrNull(newFileState)
                val newSwitchMarker = newSample?.data as? SwitchToFile

                if (newSwitchMarker != null) {
                    fileStates[currentFile] = newFileState + 1
                } else {
                    break
                }
            }

            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (openLeftMarker != null) {
            globalState++
            leftPaneFile = openLeftMarker.fileName
            rightPaneFile = currentFile
            if (leftPaneFile !in fileStates) {
                fileStates[leftPaneFile!!] = 0
            }
            revealedFiles.add(openLeftMarker.fileName)
            getAllParentPaths(openLeftMarker.fileName).forEach { parentPath ->
                if (parentPath in initiallyHiddenDirectories) {
                    revealedFiles.add(parentPath)
                }
            }
            if (openLeftMarker.switchTo) {
                currentFile = openLeftMarker.fileName
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (openRightMarker != null) {
            globalState++
            leftPaneFile = currentFile
            rightPaneFile = openRightMarker.fileName
            if (rightPaneFile !in fileStates) {
                fileStates[rightPaneFile!!] = 0
            }
            revealedFiles.add(openRightMarker.fileName)
            getAllParentPaths(openRightMarker.fileName).forEach { parentPath ->
                if (parentPath in initiallyHiddenDirectories) {
                    revealedFiles.add(parentPath)
                }
            }
            if (openRightMarker.switchTo) {
                currentFile = openRightMarker.fileName
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeLeftMarker) {
            globalState++
            leftPaneFile = null
            if (rightPaneFile != null) {
                currentFile = rightPaneFile!!
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeRightMarker) {
            globalState++
            rightPaneFile = null
            if (leftPaneFile != null) {
                currentFile = leftPaneFile!!
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (hideFileTreeMarker) {
            globalState++
            fileTreeHidden = true
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (showFileTreeMarker) {
            globalState++
            fileTreeHidden = false
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (openErrorMarker != null) {
            globalState++
            errorText = openErrorMarker.text
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (closeErrorMarker) {
            globalState++
            errorText = null
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (showEmojiMarker != null) {
            globalState++
            currentEmoji = showEmojiMarker.emoji
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (hideEmojiMarker) {
            globalState++
            currentEmoji = null
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (showImageMarker != null) {
            globalState++
            currentImage = showImageMarker.imageResource
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (hideImageMarker) {
            globalState++
            currentImage = null
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (openPanelMarker != null) {
            globalState++
            openPanels.add(openPanelMarker.name)
            activePanels.add(openPanelMarker.name)
            if (openPanelMarker.name !in panelStates) {
                panelStates[openPanelMarker.name] = 0
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (closePanelMarker != null) {
            globalState++
            openPanels.remove(closePanelMarker.name)
            activePanels.remove(closePanelMarker.name)
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (pausePanelMarker != null) {
            globalState++
            pausedPanels.add(pausePanelMarker.name)
            activePanels.remove(pausePanelMarker.name)
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (resumePanelMarker != null) {
            globalState++
            pausedPanels.remove(resumePanelMarker.name)
            activePanels.add(resumePanelMarker.name)
            if (resumePanelMarker.name !in panelStates) {
                panelStates[resumePanelMarker.name] = 0
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (changeTitleMarker != null) {
            globalState++
            currentTitle = changeTitleMarker.title
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (renameFileMarker != null) {
            globalState++
            fileRenames[currentFile] = renameFileMarker.newName
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (revealFileMarker != null) {
            globalState++
            revealedFiles.add(revealFileMarker.fileName)
            getAllParentPaths(revealFileMarker.fileName).forEach { parentPath ->
                if (parentPath in initiallyHiddenDirectories) {
                    revealedFiles.add(parentPath)
                }
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (hideFileMarker != null) {
            globalState++
            revealedFiles.remove(hideFileMarker.fileName)
            getAllParentPaths(hideFileMarker.fileName).forEach { parentPath ->
                if (parentPath in initiallyHiddenDirectories) {
                    val hasOtherVisibleChildren = revealedFiles.any { it != hideFileMarker.fileName && it.startsWith("$parentPath/") }
                    if (!hasOtherVisibleChildren) {
                        revealedFiles.remove(parentPath)
                    }
                }
            }
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (enlargeSelectedFileMarker) {
            globalState++
            enlargedFile = currentFile
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else if (shrinkSelectedFileMarker) {
            globalState++
            enlargedFile = null
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        } else {
            globalState++
            for (panelName in activePanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, image = currentImage, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), pausedPanels = pausedPanels.toSet(), activePanels = activePanels.toSet(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet(), enlargedFile = enlargedFile))
            fileStates[currentFile] = currentFileState + 1
        }

        if (globalState > 100) break
    }

    return mappings
}

fun calculateTotalStates(files: List<Pair<String, Any>>): Int {
    require(files.isNotEmpty()) { "files list must not be empty" }

    val primaryFilePath = files.first().first
    val allCodeSamples = files
        .mapNotNull { (path, value) ->
            when (value) {
                is List<*> -> path to (value as List<CodeSample>)
                is InitiallyHiddenFile -> path to value.codeSamples
                is Directory -> null
                else -> null
            }
        }
        .toMap()

    val mapping = buildFileStateMapping(primaryFilePath, allCodeSamples, null)
    return mapping.size
}

@Composable
fun Transition<Int>.buildIdeState(vararg files: Pair<String, Any>) =
    buildIdeState(files.toList())

@Composable
fun Transition<Int>.buildIdeState(
    files: List<Pair<String, Any>>,
    initialTitle: String? = null
): Transition<IdeState> {
    require(files.isNotEmpty()) { "files list must not be empty" }

    val primaryFilePath = files.first().first
    val initiallyHiddenFilesMap = files
        .mapNotNull { (path, value) ->
            if (value is InitiallyHiddenFile) path to value.codeSamples else null
        }
        .toMap()
    
    val initiallyHiddenDirectories = files
        .filter { (_, value) -> value is Directory && value.isInitiallyHidden }
        .map { (path, _) -> path }
        .toSet()

    val allCodeSamples = files
        .mapNotNull { (path, value) ->
            when (value) {
                is List<*> -> path to value as List<CodeSample>
                is InitiallyHiddenFile -> path to value.codeSamples
                is Directory -> null
                else -> null
            }
        }
        .toMap()

    val mapping = remember(allCodeSamples, initiallyHiddenDirectories) {
        buildFileStateMapping(primaryFilePath, allCodeSamples, initialTitle, initiallyHiddenDirectories)
    }

    val fileVisibilityMap = remember(initiallyHiddenFilesMap, mapping) {
        val visibilityMap = mutableMapOf<String, Int>()
        for ((index, fileMapping) in mapping.withIndex()) {
            val selectedFile = fileMapping.selectedFile
            if (selectedFile in initiallyHiddenFilesMap.keys && selectedFile !in visibilityMap) {
                visibilityMap[selectedFile] = index
            }

            val leftPaneFile = fileMapping.leftPaneFile
            if (leftPaneFile != null && leftPaneFile in initiallyHiddenFilesMap.keys && leftPaneFile !in visibilityMap) {
                visibilityMap[leftPaneFile] = index
            }

            val rightPaneFile = fileMapping.rightPaneFile
            if (rightPaneFile != null && rightPaneFile in initiallyHiddenFilesMap.keys && rightPaneFile !in visibilityMap) {
                visibilityMap[rightPaneFile] = index
            }

            for (revealedFile in fileMapping.revealedFiles) {
                if (revealedFile in initiallyHiddenFilesMap.keys && revealedFile !in visibilityMap) {
                    visibilityMap[revealedFile] = index
                }
            }
        }
        visibilityMap
    }

    val directoryVisibilityMap = remember(fileVisibilityMap, files, mapping) {
        val dirMap = mutableMapOf<String, Int>()
        val hiddenDirectories = files
            .filter { (_, value) -> value is Directory && value.isInitiallyHidden }
            .map { (path, _) -> path }
            .toSet()

        for ((filePath, appearAt) in fileVisibilityMap) {
            var currentPath = filePath
            while (currentPath.contains('/')) {
                val parentPath = currentPath.substringBeforeLast('/')
                if (parentPath in hiddenDirectories) {
                    if (parentPath !in dirMap || appearAt < dirMap[parentPath]!!) {
                        dirMap[parentPath] = appearAt
                    }
                }
                currentPath = parentPath
            }
        }

        for ((index, fileMapping) in mapping.withIndex()) {
            for (revealedFile in fileMapping.revealedFiles) {
                if (revealedFile in hiddenDirectories) {
                    if (revealedFile !in dirMap || index < dirMap[revealedFile]!!) {
                        dirMap[revealedFile] = index
                    }
                }
                
                var currentPath = revealedFile
                while (currentPath.contains('/')) {
                    val parentPath = currentPath.substringBeforeLast('/')
                    if (parentPath in hiddenDirectories) {
                        if (parentPath !in dirMap || index < dirMap[parentPath]!!) {
                            dirMap[parentPath] = index
                        }
                    }
                    currentPath = parentPath
                }
            }
        }

        dirMap
    }

    val allFiles = files.mapNotNull { (filePath, value) ->
        when {
            value is InitiallyHiddenFile -> {
                val appearAtState = fileVisibilityMap[filePath] ?: return@mapNotNull null
                val visibilityTransition = createChildTransition { globalState ->
                    val clampedState = globalState.coerceIn(0, mapping.lastIndex)
                    val fileStateMap = mapping[clampedState]
                    filePath in fileStateMap.revealedFiles ||
                        fileStateMap.selectedFile == filePath ||
                        fileStateMap.leftPaneFile == filePath ||
                        fileStateMap.rightPaneFile == filePath
                }

                val keepVisible = remember { mutableStateOf(false) }
                val hasAppeared = remember { mutableStateOf(false) }

                LaunchedEffect(visibilityTransition.targetState, visibilityTransition.currentState) {
                    if (visibilityTransition.targetState && !hasAppeared.value) {
                        hasAppeared.value = false
                        delay(50)
                        hasAppeared.value = true
                    } else if (!visibilityTransition.targetState) {
                        if (visibilityTransition.currentState) {
                            keepVisible.value = true
                        } else {
                            delay(350)
                            keepVisible.value = false
                        }
                    }
                }

                val shouldInclude = visibilityTransition.targetState || visibilityTransition.currentState || keepVisible.value

                if (!shouldInclude) {
                    return@mapNotNull null
                }

                val delayedVisibilityTransition = createChildTransition {
                    visibilityTransition.targetState && hasAppeared.value
                }

                val fileTransition = createChildTransition { globalState ->
                    val clampedState = globalState.coerceIn(0, mapping.lastIndex)
                    val fileStateMap = mapping[clampedState]
                    val mappedState = fileStateMap.fileStates[filePath] ?: 0
                    mappedState.coerceIn(0, value.codeSamples.lastIndex.coerceAtLeast(0))
                }

                ProjectFile(
                    name = filePath.substringAfterLast('/'),
                    path = filePath,
                    content = fileTransition.createChildTransition { state ->
                        value.codeSamples.safeGet(state)
                    } as Transition<CodeSample>?,
                    visibilityTransition = delayedVisibilityTransition
                )
            }
            value is Directory -> {
                val appearAtState = directoryVisibilityMap[filePath]

                if (appearAtState != null) {
                    val visibilityTransition = createChildTransition { globalState ->
                        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
                        val fileStateMap = mapping[clampedState]
                        
                        val isExplicitlyRevealed = filePath in fileStateMap.revealedFiles
                        
                        val hasVisibleDescendant = listOfNotNull(
                            fileStateMap.selectedFile,
                            fileStateMap.leftPaneFile,
                            fileStateMap.rightPaneFile
                        ).plus(fileStateMap.revealedFiles).any { visiblePath ->
                            visiblePath.startsWith("$filePath/")
                        }
                        
                        isExplicitlyRevealed || hasVisibleDescendant
                    }

                    val keepVisible = remember { mutableStateOf(false) }
                    val hasAppeared = remember { mutableStateOf(false) }

                    LaunchedEffect(visibilityTransition.targetState, visibilityTransition.currentState) {
                        if (visibilityTransition.targetState && !hasAppeared.value) {
                            hasAppeared.value = false
                            delay(50)
                            hasAppeared.value = true
                        } else if (!visibilityTransition.targetState) {
                            if (visibilityTransition.currentState) {
                                keepVisible.value = true
                            } else {
                                delay(350)
                                keepVisible.value = false
                            }
                        }
                    }

                    val shouldInclude = visibilityTransition.targetState || visibilityTransition.currentState || keepVisible.value

                    if (!shouldInclude) {
                        return@mapNotNull null
                    }

                    val delayedVisibilityTransition = createChildTransition {
                        visibilityTransition.targetState && hasAppeared.value
                    }

                    ProjectFile(
                        name = filePath.substringAfterLast('/'),
                        path = filePath,
                        isDirectory = true,
                        visibilityTransition = delayedVisibilityTransition
                    )
                } else if (value.isInitiallyHidden) {
                    return@mapNotNull null
                } else {
                    ProjectFile(
                        name = filePath.substringAfterLast('/'),
                        path = filePath,
                        isDirectory = true
                    )
                }
            }
            value is List<*> -> {
                val codeSamples = value as List<CodeSample>
                val fileTransition = createChildTransition { globalState ->
                    val clampedState = globalState.coerceIn(0, mapping.lastIndex)
                    val fileStateMap = mapping[clampedState]
                    val mappedState = fileStateMap.fileStates[filePath] ?: 0
                    mappedState.coerceIn(0, codeSamples.lastIndex.coerceAtLeast(0))
                }

                ProjectFile(
                    name = filePath.substringAfterLast('/'),
                    path = filePath,
                    content = fileTransition.createChildTransition { state ->
                        codeSamples.safeGet(state)
                    } as Transition<CodeSample>?
                )
            }
            else -> null
        }
    }

    return createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        val currentFileRenames = mapping[clampedState].fileRenames
        IdeState(
            files = allFiles,
            selectedFile = mapping[clampedState].selectedFile,
            emoji = mapping[clampedState].emoji,
            image = mapping[clampedState].image,
            leftPaneFile = mapping[clampedState].leftPaneFile,
            rightPaneFile = mapping[clampedState].rightPaneFile,
            fileTreeHidden = mapping[clampedState].fileTreeHidden,
            errorText = mapping[clampedState].errorText,
            openPanels = mapping[clampedState].openPanels,
            panelStates = mapping[clampedState].panelStates,
            state = clampedState,
            title = mapping[clampedState].title,
            fileRenames = currentFileRenames,
            enlargedFile = mapping[clampedState].enlargedFile
        )
    }
}

@Composable
fun Transition<IdeState>.IdeLayout(
    builder: IdeLayoutScope.() -> Unit
) {
    val scope = IdeLayoutScope().apply(builder)

    // Check if any panel should be open
    val isTopPanelOpen = createChildTransition { state ->
        scope.topPanels.any { (name, panel) ->
            (state.state in panel.openAt) || (name in state.openPanels)
        }
    }
    val isLeftPanelOpen = createChildTransition { state ->
        scope.leftPanels.any { (name, panel) ->
            (state.state in panel.openAt) || (name in state.openPanels)
        }
    }
    val isEmojiVisible = createChildTransition { it.emoji != null }
    val isImageVisible = createChildTransition { it.image != null }

    var topPanelHeight by remember { mutableIntStateOf(0) }
    var leftPanelWidth by remember { mutableIntStateOf(0) }

    val ideTopPadding by animateDp { ideState ->
        val anyPanelOpen = scope.topPanels.any { (name, panel) ->
            (ideState.state in panel.openAt) || (name in ideState.openPanels)
        }
        if (anyPanelOpen) {
            val currentOpenPanel = scope.topPanels.entries.lastOrNull { (name, panel) ->
                (ideState.state in panel.openAt) || (name in ideState.openPanels)
            }
            val isAdaptive = currentOpenPanel?.value?.adaptive ?: false
            if (isAdaptive && topPanelHeight > 0) {
                (topPanelHeight / 2).dp
            } else {
                260.dp
            }
        } else {
            0.dp
        }
    }

    val ideStartPadding by animateDp { ideState ->
        val anyPanelOpen = scope.leftPanels.any { (name, panel) ->
            (ideState.state in panel.openAt) || (name in ideState.openPanels)
        }
        if (anyPanelOpen) {
            val currentOpenPanel = scope.leftPanels.entries.lastOrNull { (name, panel) ->
                (ideState.state in panel.openAt) || (name in ideState.openPanels)
            }
            val isAdaptive = currentOpenPanel?.value?.adaptive ?: false
            if (isAdaptive && leftPanelWidth > 0) {
                (leftPanelWidth / 2).dp
            } else {
                260.dp
            }
        } else {
            0.dp
        }
    }

    val fileTreeWidth by animateDp { if (it.fileTreeHidden) 0.dp else 225.dp }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    // If any panel is adaptive, update the height when Box has content
                    val hasAdaptivePanel = scope.topPanels.values.any { it.adaptive }
                    if (hasAdaptivePanel && size.height > 0) {
                        topPanelHeight = size.height
                    }
                }
        ) {
            // Render all panels - only one will be visible at a time
            scope.topPanels.forEach { (name, panel) ->
                val isPanelOpen = createChildTransition { state ->
                    (state.state in panel.openAt) || (name in state.openPanels)
                }
                
                isPanelOpen.SlideFromBottomAnimatedVisibility {
                    val panelState = createChildTransition {
                        it.panelStates[name] ?: 0
                    }
                    panel.content.invoke(panelState)
                }
            }
        }

        Box(
            Modifier
                .align(Alignment.TopStart)
                .onSizeChanged { size ->
                    val hasAdaptivePanel = scope.leftPanels.values.any { it.adaptive }
                    if (hasAdaptivePanel && size.width > 0) {
                        leftPanelWidth = size.width
                    }
                }
        ) {
            scope.leftPanels.forEach { (name, panel) ->
                val isPanelOpen = createChildTransition { state ->
                    (state.state in panel.openAt) || (name in state.openPanels)
                }
                
                isPanelOpen.FadeInOutAnimatedVisibility {
                    val panelState = createChildTransition {
                        it.panelStates[name] ?: 0
                    }
                    panel.content.invoke(panelState)
                }
            }
        }

        IDE(
            ideState = currentState.copy(fileTreeWidth = fileTreeWidth),
            modifier = Modifier.padding(top = ideTopPadding, start = ideStartPadding),
        )

        Box(Modifier.align(Alignment.Center)) {
            isEmojiVisible.FadeInOutAnimatedVisibility {
                ProvideTextStyle(MaterialTheme.typography.h1) {
                    Text(currentState.emoji ?: "")
                }
            }
            
            isImageVisible.FadeInOutAnimatedVisibility {
                val imageRes = currentState.image
                if (imageRes != null) {
                    Box(Modifier.clip(CircleShape).border(2.dp, MaterialTheme.colors.primary, CircleShape)) {
                        ResourceImage(imageRes, modifier = Modifier.width(300.dp))
                    }
                }
            }
        }
    }
}

val EMPTY_SAMPLE = buildCodeSamples {
    ""
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { this }
}