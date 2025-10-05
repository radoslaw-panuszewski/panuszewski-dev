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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.withStateTransition
import kotlinx.coroutines.delay

class IdeLayoutScope internal constructor() {
    var ideState: IdeState? = null
    var topPanelOpenAt: List<Int> = emptyList()
    var topPanelContent: ComposableLambda? = null
    var leftPanelOpenAt: List<Int> = emptyList()
    var leftPanelContent: ComposableLambda? = null
    var centerEmojiVisibleAt: List<Int> = emptyList()
    var centerEmojiContent: ComposableLambda? = null

    fun topPanel(openAt: List<Int>, content: ComposableLambda) {
        topPanelOpenAt = openAt
        topPanelContent = content
    }

    fun topPanel(openAt: Int, content: ComposableLambda) {
        topPanel(listOf(openAt), content)
    }

    fun topPanel(openAt: IntRange, content: ComposableLambda) {
        topPanel(openAt.toList(), content)
    }

    fun leftPanel(openAt: List<Int>, content: ComposableLambda) {
        leftPanelOpenAt = openAt
        leftPanelContent = content
    }

    fun leftPanel(openAt: Int, content: ComposableLambda) {
        leftPanel(listOf(openAt), content)
    }

    fun leftPanel(openAt: IntRange, content: ComposableLambda) {
        leftPanel(openAt.toList(), content)
    }

    fun centerEmoji(visibleAt: List<Int>, content: ComposableLambda) {
        centerEmojiVisibleAt = visibleAt
        centerEmojiContent = content
    }

    fun centerEmoji(visibleAt: Int, content: ComposableLambda) {
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
    val errorText: String? = null
)

fun buildFileStateMapping(
    initialFile: String,
    allCodeSamples: Map<String, List<CodeSample>>
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
    
    mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
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
        
        if (chainedOps != null) {
            globalState++
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
                }
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (advanceTogetherMarker != null) {
            globalState++
            val otherFile = advanceTogetherMarker.fileName
            if (otherFile in fileStates) {
                fileStates[otherFile] = (fileStates[otherFile] ?: 0) + 1
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (switchMarker != null) {
            globalState++
            val previousFile = currentFile
            currentFile = switchMarker.fileName
            if (currentFile !in fileStates) {
                fileStates[currentFile] = 0
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
            
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
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
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
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
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeLeftMarker) {
            globalState++
            leftPaneFile = null
            if (rightPaneFile != null) {
                currentFile = rightPaneFile!!
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (closeRightMarker) {
            globalState++
            rightPaneFile = null
            if (leftPaneFile != null) {
                currentFile = leftPaneFile!!
            }
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else if (hideFileTreeMarker) {
            globalState++
            fileTreeHidden = true
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (showFileTreeMarker) {
            globalState++
            fileTreeHidden = false
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (openErrorMarker != null) {
            globalState++
            errorText = openErrorMarker.text
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (closeErrorMarker) {
            globalState++
            errorText = null
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (showEmojiMarker != null) {
            globalState++
            currentEmoji = showEmojiMarker.emoji
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else if (hideEmojiMarker) {
            globalState++
            currentEmoji = null
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
            fileStates[currentFile] = currentFileState + 1
        } else {
            globalState++
            mappings.add(FileStateMapping(currentFile, fileStates.toMap(), emoji = currentEmoji, leftPaneFile = leftPaneFile, rightPaneFile = rightPaneFile, fileTreeHidden = fileTreeHidden, errorText = errorText))
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
    
    val mapping = buildFileStateMapping(primaryFilePath, allCodeSamples)
    return mapping.size
}

@Composable
fun Transition<Int>.buildIdeState(vararg files: Pair<String, Any>) =
    buildIdeState(files.toList())

@Composable
fun Transition<Int>.buildIdeState(
    files: List<Pair<String, Any>>
): IdeState {
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
        buildFileStateMapping(primaryFilePath, allCodeSamples)
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
    
    val selectedFile = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].selectedFile
    }.targetState
    
    val emoji = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].emoji
    }.targetState
    
    val leftPaneFile = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].leftPaneFile
    }.targetState
    
    val rightPaneFile = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].rightPaneFile
    }.targetState
    
    val fileTreeHidden = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].fileTreeHidden
    }.targetState
    
    val errorText = createChildTransition { globalState ->
        val clampedState = globalState.coerceIn(0, mapping.lastIndex)
        mapping[clampedState].errorText
    }.targetState
    
    return IdeState(
        files = allFiles,
        selectedFile = selectedFile,
        emoji = emoji,
        leftPaneFile = leftPaneFile,
        rightPaneFile = rightPaneFile,
        fileTreeHidden = fileTreeHidden,
        errorText = errorText
    )
}

@Composable
fun SceneScope<Int>.IdeLayout(
    builder: IdeLayoutScope.() -> Unit
) =
    withStateTransition {
        val scope = IdeLayoutScope().apply(builder)

        if (scope.ideState != null) {
            IDE_STATE = scope.ideState!!
        }

        val ideTopPadding by animateDp { if (it in scope.topPanelOpenAt) 260.dp else 0.dp }
        val ideStartPadding by animateDp { if (it in scope.leftPanelOpenAt) 260.dp else 0.dp }
        val fileTreeWidthOverride by animateDp { 
            if (it in scope.leftPanelOpenAt) 0.dp else 275.dp
        }
        
        val fileTreeHiddenWidth by androidx.compose.animation.core.animateDpAsState(
            targetValue = if (IDE_STATE.fileTreeHidden) 0.dp else 275.dp
        )
        
        val fileTreeWidth = if (fileTreeWidthOverride == 0.dp) 0.dp else fileTreeHiddenWidth

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.align(Alignment.TopCenter)) {
                FadeInOutAnimatedVisibility({ it in scope.topPanelOpenAt }) {
                    scope.topPanelContent?.invoke()
                }
            }

            Box(Modifier.align(Alignment.TopStart)) {
                FadeInOutAnimatedVisibility({ it in scope.leftPanelOpenAt }) {
                    scope.leftPanelContent?.invoke()
                }
            }

            IDE(
                ideState = IDE_STATE.copy(fileTreeWidth = fileTreeWidth),
                modifier = Modifier.padding(top = ideTopPadding, start = ideStartPadding),
            )

            Box(Modifier.align(Alignment.Center)) {
                FadeInOutAnimatedVisibility({ IDE_STATE.emoji != null }) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        Text(IDE_STATE.emoji ?: "")
                    }
                }
            }
        }
    }