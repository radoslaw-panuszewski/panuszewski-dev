package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.easel.overview.OverviewCurrentItemKey
import dev.bnorm.storyboard.easel.overview.StoryOverview
import dev.bnorm.storyboard.easel.overview.StoryOverviewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StoryEasel(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    val holder = rememberSaveableStateHolder()

    val storyboard = storyState.storyboard
    val storyOverviewState = remember(storyboard) { StoryOverviewState.of(storyboard) }
    var overviewVisible by remember { mutableStateOf(false) } // TODO support initial visibility?

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp && event.key == Key.Escape) {
            storyOverviewState.jumpToIndex(storyState.currentIndex)
            overviewVisible = true
            return true
        }
        return false
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = overviewVisible,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
            ) { isOverview ->
                if (isOverview) {
                    StoryOverview(
                        storyState = storyState,
                        storyOverviewState = storyOverviewState,
                        onExitOverview = {
                            job?.cancel()
                            job = coroutineScope.launch {
                                storyState.jumpTo(it)
                                job = null
                                overviewVisible = false
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    holder.SaveableStateProvider(storyboard) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            StoryOverlay(overlay = overlay) {
                                Story(
                                    storyState = storyState,
                                    modifier = Modifier
                                        .sharedElement(
                                            rememberSharedContentState(OverviewCurrentItemKey),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                        .requestFocus()
                                        .onStoryNavigation(storyState = storyState)
                                        .onKeyEvent { handleKeyEvent(it) }
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
internal fun Modifier.onStoryNavigation(storyState: StoryState): Modifier {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    fun handle(event: KeyEvent): Boolean {
        when (event.type) {
            KeyEventType.KeyDown -> {
                when (event.key) {
                    Key.DirectionRight,
//                    Key.DirectionDown,
//                    Key.Enter,
//                    Key.Spacebar,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyState.advance(AdvanceDirection.Forward)
                            job = null
                        }
                        return true
                    }

                    Key.DirectionLeft,
//                    Key.DirectionUp,
//                    Key.Backspace,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyState.advance(AdvanceDirection.Backward)
                            job = null
                        }
                        return true
                    }
                }
            }
        }

        return false
    }

    return onKeyEvent { handle(it) }
}
