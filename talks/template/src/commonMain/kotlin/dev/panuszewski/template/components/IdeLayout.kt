package dev.panuszewski.template.components

import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
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

@Composable
fun SceneScope<Int>.IdeLayout(
    builder: IdeLayoutScope.() -> Unit
) =
    withStateTransition {
        val scope = IdeLayoutScope().apply(builder)

        if (scope.ideState != null) {
            IDE_STATE = scope.ideState!!
        }

        val currentState = transition.targetState
        val selectedFile = IDE_STATE.files.find { file ->
            file.content?.targetState?.data is SwitchToFile == false
        }?.let { file ->
            val codeSample = file.content?.targetState
            val switchMarker = codeSample?.data as? SwitchToFile
            if (switchMarker != null) {
                switchMarker.fileName
            } else {
                IDE_STATE.selectedFile
            }
        } ?: IDE_STATE.selectedFile

        val actualSelectedFile = IDE_STATE.files.firstOrNull { it.content?.targetState?.data is SwitchToFile }
            ?.let { (it.content?.targetState?.data as? SwitchToFile)?.fileName }
            ?: IDE_STATE.selectedFile

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
                ideState = IDE_STATE.copy(
                    fileTreeWidth = fileTreeWidth,
                    selectedFile = actualSelectedFile
                ),
                modifier = Modifier.padding(top = ideTopPadding, start = ideStartPadding),
            )

            Box(Modifier.align(Alignment.Center)) {
                FadeInOutAnimatedVisibility({ it in scope.centerEmojiVisibleAt.orEmpty() }) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        scope.centerEmojiContent?.invoke()
                    }
                }
            }
        }
    }