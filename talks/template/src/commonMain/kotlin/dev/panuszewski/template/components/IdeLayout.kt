package dev.panuszewski.template.components

import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.ComposableLambda
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.withStateTransition

@Composable
fun SceneScope<Int>.IdeLayout(
    topPanelOpenAt: IntRange? = null,
    topPanel: ComposableLambda? = null,
    leftPanelOpenAt: IntRange? = null,
    leftPanel: ComposableLambda? = null,
    centerEmojiVisibleAt: List<Int>? = null,
    centerEmoji: ComposableLambda? = null,
) = IdeLayout(
    topPanelOpenAt = topPanelOpenAt?.toList(),
    topPanel = topPanel,
    leftPanelOpenAt = leftPanelOpenAt?.toList(),
    leftPanel = leftPanel,
    centerEmojiVisibleAt = centerEmojiVisibleAt,
    centerEmoji = centerEmoji,
)

@Composable
fun SceneScope<Int>.IdeLayout(
    topPanelOpenAt: List<Int>? = null,
    leftPanelOpenAt: List<Int>? = null,
    topPanel: ComposableLambda? = null,
    leftPanel: ComposableLambda? = null,
    centerEmojiVisibleAt: List<Int>? = null,
    centerEmoji: ComposableLambda? = null,
) {
    withStateTransition {
        val ideTopPadding by animateDp { if (it in topPanelOpenAt.orEmpty()) 260.dp else 0.dp }
        val ideStartPadding by animateDp { if (it in leftPanelOpenAt.orEmpty()) 260.dp else 0.dp }
        val fileTreeWidth by animateDp { if (it in leftPanelOpenAt.orEmpty()) 0.dp else 275.dp }

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.align(Alignment.TopCenter)) {
                topPanel?.invoke()
            }

            Box(Modifier.align(Alignment.TopStart)) {
                leftPanel?.invoke()
            }

            IDE(
                ideState = IDE_STATE.copy(fileTreeWidth = fileTreeWidth),
                modifier = Modifier.padding(top = ideTopPadding, start = ideStartPadding),
            )

            Box(Modifier.align(Alignment.Center)) {
                FadeInOutAnimatedVisibility({ it in centerEmojiVisibleAt.orEmpty() }) {
                    ProvideTextStyle(MaterialTheme.typography.h1) {
                        centerEmoji?.invoke()
                    }
                }
            }
        }
    }
}