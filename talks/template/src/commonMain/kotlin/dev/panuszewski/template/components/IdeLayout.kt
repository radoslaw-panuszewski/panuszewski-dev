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
    var topPanelOpenAt: IntRange? = null
    var topPanelContent: ComposableLambda? = null
    var leftPanelOpenAt: IntRange? = null
    var leftPanelContent: ComposableLambda? = null
    var centerEmojiVisibleAt: List<Int>? = null
    var centerEmojiContent: ComposableLambda? = null

    fun topPanel(openAt: IntRange, content: ComposableLambda) {
        topPanelOpenAt = openAt
        topPanelContent = content
    }

    fun leftPanel(openAt: IntRange, content: ComposableLambda) {
        leftPanelOpenAt = openAt
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

        val ideTopPadding by animateDp { if (it in scope.topPanelOpenAt?.toList().orEmpty()) 260.dp else 0.dp }
        val ideStartPadding by animateDp { if (it in scope.leftPanelOpenAt?.toList().orEmpty()) 260.dp else 0.dp }
        val fileTreeWidth by animateDp { if (it in scope.leftPanelOpenAt?.toList().orEmpty()) 0.dp else 275.dp }

        Box(Modifier.fillMaxSize()) {
            Box(Modifier.align(Alignment.TopCenter)) {
                scope.topPanelContent?.invoke()
            }

            Box(Modifier.align(Alignment.TopStart)) {
                scope.leftPanelContent?.invoke()
            }

            IDE(
                ideState = IDE_STATE.copy(fileTreeWidth = fileTreeWidth),
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