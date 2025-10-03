package dev.panuszewski.template.components

import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.withStateTransition

@Composable
fun SceneScope<Int>.IdeLayout(
    topPanelOpenAt: IntRange,
    topPanel: ComposableLambda,
    leftPanelOpenAt: IntRange,
    leftPanel: ComposableLambda,
) = IdeLayout(
    topPanelOpenAt = topPanelOpenAt.toList(),
    topPanel = topPanel,
    leftPanelOpenAt = leftPanelOpenAt.toList(),
    leftPanel = leftPanel,
)

@Composable
fun SceneScope<Int>.IdeLayout(
    topPanelOpenAt: List<Int>,
    leftPanelOpenAt: List<Int>,
    topPanel: ComposableLambda,
    leftPanel: ComposableLambda,
) {
    withStateTransition {
        val ideTopPadding by animateDp { if (it in topPanelOpenAt) 260.dp else 0.dp }
        val ideStartPadding by animateDp { if (it in leftPanelOpenAt) 260.dp else 0.dp }
        val fileTreeWidth by animateDp { if (it in leftPanelOpenAt) 0.dp else 275.dp }

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.align(Alignment.TopCenter)) {
                topPanel()
            }

            Box(Modifier.align(Alignment.TopStart)) {
                leftPanel()
            }

            IDE(
                ideState = IDE_STATE.copy(fileTreeWidth = fileTreeWidth),
                modifier = Modifier.padding(top = ideTopPadding, start = ideStartPadding),
            )
        }
    }
}