package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.withStateTransition

class IdeLayoutScope internal constructor() {
    var ideState: IdeState? = null
    var topPanelOpenAt: List<Int> = emptyList()
    var topPanelContent: ComposableLambda? = null
    var leftPanelOpenAt: List<Int> = emptyList()
    var leftPanelContent: ComposableLambda? = null
    var centerEmojiVisibleAt: List<Int> = emptyList()
    var centerEmojiContent: ComposableLambda? = null

    fun topPanel(openAt: IntRange, content: ComposableLambda) {
        topPanelOpenAt = openAt.toList()
        topPanelContent = content
    }

    fun leftPanel(openAt: IntRange, content: ComposableLambda) {
        leftPanelOpenAt = openAt.toList()
        leftPanelContent = content
    }

    fun centerEmoji(visibleAt: Int, content: ComposableLambda) {
        centerEmojiVisibleAt = listOf(visibleAt)
        centerEmojiContent = content
    }

    fun centerEmoji(visibleAt: List<Int>, content: ComposableLambda) {
        centerEmojiVisibleAt = visibleAt
        centerEmojiContent = content
    }
}

data class FileStateMapping(
    val selectedFile: String,
    val fileStates: Map<String, Int>
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
    
    mappings.add(FileStateMapping(currentFile, fileStates.toMap()))
    fileStates[currentFile] = 1
    
    while (true) {
        val currentFileSamples = allCodeSamples[currentFile] ?: break
        val currentFileState = fileStates[currentFile] ?: 0
        
        if (currentFileState >= currentFileSamples.size) break
        
        val sample = currentFileSamples.getOrNull(currentFileState)
        val switchMarker = sample?.data as? SwitchToFile
        
        if (switchMarker != null) {
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
            
            mappings.add(FileStateMapping(currentFile, fileStates.toMap()))
            fileStates[currentFile] = (fileStates[currentFile] ?: 0) + 1
        } else {
            globalState++
            mappings.add(FileStateMapping(currentFile, fileStates.toMap()))
            fileStates[currentFile] = currentFileState + 1
        }
        
        if (globalState > 100) break
    }
    
    return mappings
}

@Composable
fun Transition<Int>.buildIdeStateWithMapping(
    files: List<Pair<String, List<CodeSample>>>
): IdeState {
    require(files.isNotEmpty()) { "files list must not be empty" }
    
    val primaryFilePath = files.first().first
    val allCodeSamples = files
        .filter { it.second !== DIRECTORY }
        .associate { it.first to it.second }
    
    val mapping = remember(allCodeSamples) {
        buildFileStateMapping(primaryFilePath, allCodeSamples)
    }
    
    val allFiles = files.map { (filePath, codeSamples) ->
        if (codeSamples === DIRECTORY) {
            ProjectFile(
                name = filePath.substringAfterLast('/'),
                path = filePath,
                isDirectory = true
            )
        } else {
            val fileTransition = createChildTransition { globalState ->
                val fileStateMap = mapping.getOrNull(globalState)
                val mappedState = fileStateMap?.fileStates?.get(filePath) ?: 0
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
    }
    
    val selectedFile = createChildTransition { globalState ->
        mapping.getOrNull(globalState)?.selectedFile ?: primaryFilePath
    }.targetState
    
    return IdeState(
        files = allFiles,
        selectedFile = selectedFile
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
        val fileTreeWidth by animateDp { if (it in scope.leftPanelOpenAt) 0.dp else 275.dp }

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
                FadeInOutAnimatedVisibility({ it in scope.centerEmojiVisibleAt }) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        scope.centerEmojiContent?.invoke()
                    }
                }
            }
        }
    }