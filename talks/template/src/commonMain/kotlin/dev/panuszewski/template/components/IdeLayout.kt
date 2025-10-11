package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.safeGet
import kotlinx.coroutines.delay

typealias PanelContent = @Composable (Transition<Int>) -> Unit

class IdeLayoutScope internal constructor() {
    var topPanelOpenAt: List<Int> = emptyList()
    var topPanelContent: PanelContent? = null
    var topPanelName: String? = null
    var topPanelAdaptive: Boolean = false
    var leftPanelOpenAt: List<Int> = emptyList()
    var leftPanelContent: PanelContent? = null
    var leftPanelName: String? = null
    var centerEmojiVisibleAt: List<Int> = emptyList()
    var centerEmojiContent: PanelContent? = null

    fun topPanel(name: String, content: PanelContent) {
        topPanelContent = content
        topPanelName = name
        topPanelAdaptive = false
    }

    fun topPanel(name: String, openAt: List<Int>, content: PanelContent) {
        topPanelOpenAt = openAt
        topPanelContent = content
        topPanelName = name
        topPanelAdaptive = false
    }

    fun topPanel(name: String, openAt: Int, content: PanelContent) {
        topPanel(name, listOf(openAt), content)
    }

    fun topPanel(name: String, openAt: IntRange, content: PanelContent) {
        topPanel(name, openAt.toList(), content)
    }

    fun topPanel(content: PanelContent) {
        topPanelContent = content
        topPanelName = "default"
        topPanelAdaptive = false
    }

    fun topPanel(openAt: List<Int>, content: PanelContent) {
        topPanelOpenAt = openAt
        topPanelContent = content
        topPanelName = "default"
        topPanelAdaptive = false
    }

    fun topPanel(openAt: Int, content: PanelContent) {
        topPanel(openAt = listOf(openAt), content)
    }

    fun topPanel(openAt: IntRange, content: PanelContent) {
        topPanel(openAt = openAt.toList(), content)
    }

    fun adaptiveTopPanel(name: String, content: PanelContent) {
        topPanelContent = content
        topPanelName = name
        topPanelAdaptive = true
    }

    fun adaptiveTopPanel(name: String, openAt: List<Int>, content: PanelContent) {
        topPanelOpenAt = openAt
        topPanelContent = content
        topPanelName = name
        topPanelAdaptive = true
    }

    fun adaptiveTopPanel(name: String, openAt: Int, content: PanelContent) {
        adaptiveTopPanel(name, listOf(openAt), content)
    }

    fun adaptiveTopPanel(name: String, openAt: IntRange, content: PanelContent) {
        adaptiveTopPanel(name, openAt.toList(), content)
    }

    fun leftPanel(name: String, content: PanelContent) {
        leftPanelContent = content
        leftPanelName = name
    }

    fun leftPanel(name: String, openAt: List<Int>, content: PanelContent) {
        leftPanelOpenAt = openAt
        leftPanelContent = content
        leftPanelName = name
    }

    fun leftPanel(name: String, openAt: Int, content: PanelContent) {
        leftPanel(name, listOf(openAt), content)
    }

    fun leftPanel(name: String, openAt: IntRange, content: PanelContent) {
        leftPanel(name, openAt.toList(), content)
    }

    fun leftPanel(content: PanelContent) {
        leftPanelContent = content
        leftPanelName = "default"
    }

    fun leftPanel(openAt: List<Int>, content: PanelContent) {
        leftPanelOpenAt = openAt
        leftPanelContent = content
        leftPanelName = "default"
    }

    fun leftPanel(openAt: Int, content: PanelContent) {
        leftPanel(openAt = listOf(openAt), content)
    }

    fun leftPanel(openAt: IntRange, content: PanelContent) {
        leftPanel(openAt = openAt.toList(), content)
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
    val leftPaneFile: String? = null,
    val rightPaneFile: String? = null,
    val fileTreeHidden: Boolean = false,
    val errorText: String? = null,
    val openPanels: Set<String> = emptySet(),
    val panelStates: Map<String, Int> = emptyMap(),
    val title: String? = null,
    val fileRenames: Map<String, String> = emptyMap(),
    val revealedFiles: Set<String> = emptySet()
)

fun buildFileStateMapping(
    initialFile: String,
    allCodeSamples: Map<String, List<CodeSample>>,
    title: String?,
): List<FileStateMapping> {
    val mappings = mutableListOf<FileStateMapping>()
    var currentFile = initialFile
    val fileStates = mutableMapOf<String, Int>().apply {
        allCodeSamples.keys.forEach { put(it, 0) }
    }
    var globalState = 0
    var currentEmoji: String? = null
    var leftPaneFile: String? = null
    var rightPaneFile: String? = null
    var fileTreeHidden = false
    var errorText: String? = null
    val openPanels = mutableSetOf<String>()
    val panelStates = mutableMapOf<String, Int>()
    var currentTitle: String? = title
    val fileRenames = mutableMapOf<String, String>()
    val revealedFiles = mutableSetOf<String>()

    mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
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
        val changeTitleMarker = sample?.data as? ChangeTitle
        val renameFileMarker = sample?.data as? RenameSelectedFile
        val revealFileMarker = sample?.data as? RevealFile

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
                    is OpenInLeftPane -> {
                        leftPaneFile = operation.fileName
                        rightPaneFile = currentFile
                        if (leftPaneFile !in fileStates) {
                            fileStates[leftPaneFile!!] = 0
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
                        if (wasNewlyOpened) {
                            panelStates[operation.name] = 0
                            panelsOpenedInThisFrame.add(operation.name)
                        }
                    }
                    is CloseNamedPanel -> {
                        openPanels.remove(operation.name)
                    }
                    is ChangeTitle -> {
                        currentTitle = operation.title
                    }
                    is RenameSelectedFile -> {
                        fileRenames[currentFile] = operation.newName
                    }
                    is RevealFile -> {
                        revealedFiles.add(operation.fileName)
                    }
                }
            }
            for (panelName in openPanels) {
                if (panelName !in panelsOpenedInThisFrame) {
                    panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
                }
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (advanceTogetherMarker != null) {
            globalState++
            val otherFile = advanceTogetherMarker.fileName
            if (otherFile in fileStates) {
                fileStates[otherFile] = (fileStates[otherFile] ?: 0) + 1
            }
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (switchMarker != null) {
            globalState++
            val previousFile = currentFile
            currentFile = switchMarker.fileName
            if (currentFile !in fileStates) {
                fileStates[currentFile] = 0
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

            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (openLeftMarker != null) {
            globalState++
            leftPaneFile = openLeftMarker.fileName
            rightPaneFile = currentFile
            if (leftPaneFile !in fileStates) {
                fileStates[leftPaneFile!!] = 0
            }
            if (openLeftMarker.switchTo) {
                currentFile = openLeftMarker.fileName
            }
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (openRightMarker != null) {
            globalState++
            leftPaneFile = currentFile
            rightPaneFile = openRightMarker.fileName
            if (rightPaneFile !in fileStates) {
                fileStates[rightPaneFile!!] = 0
            }
            if (openRightMarker.switchTo) {
                currentFile = openRightMarker.fileName
            }
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeLeftMarker) {
            globalState++
            leftPaneFile = null
            if (rightPaneFile != null) {
                currentFile = rightPaneFile!!
            }
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeRightMarker) {
            globalState++
            rightPaneFile = null
            if (leftPaneFile != null) {
                currentFile = leftPaneFile!!
            }
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (hideFileTreeMarker) {
            globalState++
            fileTreeHidden = true
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (showFileTreeMarker) {
            globalState++
            fileTreeHidden = false
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (openErrorMarker != null) {
            globalState++
            errorText = openErrorMarker.text
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (closeErrorMarker) {
            globalState++
            errorText = null
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (showEmojiMarker != null) {
            globalState++
            currentEmoji = showEmojiMarker.emoji
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (hideEmojiMarker) {
            globalState++
            currentEmoji = null
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (openPanelMarker != null) {
            globalState++
            openPanels.add(openPanelMarker.name)
            if (openPanelMarker.name !in panelStates) {
                panelStates[openPanelMarker.name] = 0
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (closePanelMarker != null) {
            globalState++
            openPanels.remove(closePanelMarker.name)
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (changeTitleMarker != null) {
            globalState++
            currentTitle = changeTitleMarker.title
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (renameFileMarker != null) {
            globalState++
            fileRenames[currentFile] = renameFileMarker.newName
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else if (revealFileMarker != null) {
            globalState++
            revealedFiles.add(revealFileMarker.fileName)
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
            fileStates[currentFile] = currentFileState + 1
        } else {
            globalState++
            for (panelName in openPanels) {
                panelStates[panelName] = (panelStates[panelName] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText, openPanels = openPanels.toSet(), panelStates = panelStates.toMap(), title = currentTitle, fileRenames = fileRenames.toMap(), revealedFiles = revealedFiles.toSet()))
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

    val mapping = remember(allCodeSamples) {
        buildFileStateMapping(primaryFilePath, allCodeSamples, initialTitle)
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

    val directoryVisibilityMap = remember(fileVisibilityMap, files) {
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
        dirMap
    }

    val allFiles = files.mapNotNull { (filePath, value) ->
        when {
            value is InitiallyHiddenFile -> {
                val appearAtState = fileVisibilityMap[filePath] ?: return@mapNotNull null
                val visibilityTransition = createChildTransition { globalState ->
                    globalState >= appearAtState
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
                        globalState >= appearAtState
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
            leftPaneFile = mapping[clampedState].leftPaneFile,
            rightPaneFile = mapping[clampedState].rightPaneFile,
            fileTreeHidden = mapping[clampedState].fileTreeHidden,
            errorText = mapping[clampedState].errorText,
            openPanels = mapping[clampedState].openPanels,
            panelStates = mapping[clampedState].panelStates,
            state = clampedState,
            title = mapping[clampedState].title,
            fileRenames = currentFileRenames
        )
    }
}

@Composable
fun Transition<IdeState>.IdeLayout(
    builder: IdeLayoutScope.() -> Unit
) {
    val scope = IdeLayoutScope().apply(builder)

    val isTopPanelOpen = createChildTransition { (it.state in scope.topPanelOpenAt) || (scope.topPanelName != null && scope.topPanelName in it.openPanels) }
    val isLeftPanelOpen = createChildTransition { (it.state in scope.leftPanelOpenAt)  || (scope.leftPanelName != null && scope.leftPanelName in it.openPanels) }
    val isEmojiVisible = createChildTransition { it.emoji != null }

    var topPanelHeight by remember { mutableIntStateOf(0) }

    val ideTopPadding by isTopPanelOpen.animateDp {
        if (it) {
            if (scope.topPanelAdaptive && topPanelHeight > 0) {
                (topPanelHeight / 2).dp
            } else {
                260.dp
            }
        } else {
            0.dp
        }
    }

    val ideStartPadding by isLeftPanelOpen.animateDp { if (it) 260.dp else 0.dp }

    val fileTreeWidth by animateDp { if (it.fileTreeHidden) 0.dp else 225.dp }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    if (scope.topPanelAdaptive) {
                        topPanelHeight = size.height
                    }
                }
        ) {
            isTopPanelOpen.SlideFromBottomAnimatedVisibility {
                val panelState = createChildTransition {
                    val panelName = scope.topPanelName
                    if (panelName != null) it.panelStates[panelName] ?: 0 else 0
                }
                scope.topPanelContent?.invoke(panelState)
            }
        }

        Box(Modifier.align(Alignment.TopStart)) {
            isLeftPanelOpen.FadeInOutAnimatedVisibility {
                val panelState = createChildTransition {
                    val panelName = scope.leftPanelName
                    if (panelName != null) it.panelStates[panelName] ?: 0 else 0
                }
                scope.leftPanelContent?.invoke(panelState)
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
        }
    }
}