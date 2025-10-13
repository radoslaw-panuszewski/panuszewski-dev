package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text

@Composable
context(_: SceneScope<Int>)
fun Transition<out Frame<Int>>.RevealSequentially(
    since: Int = 1,
    until: Int = Int.MAX_VALUE,
    textStyle: TextStyle? = MaterialTheme.typography.h6,
    highlightedTextStyle: TextStyle? = textStyle,
    configure: RevealSequentiallyScope.() -> Unit
): Int =
    createChildTransition { it.toState() }
        .RevealSequentially(since, until, textStyle, highlightedTextStyle, configure)

@Composable
fun Transition<Int>.RevealSequentially(
    since: Int = 1,
    until: Int = Int.MAX_VALUE,
    textStyle: TextStyle? = MaterialTheme.typography.h6,
    highlightedTextStyle: TextStyle? = null,
    configure: RevealSequentiallyScope.() -> Unit
): Int {
    val scope = RevealSequentiallyScope(since, until, textStyle, highlightedTextStyle, this)
    scope.configure()
    with(scope) {
        Content()
    }
    return since + scope.items.size
}

class RevealSequentiallyScope(
    private val since: Int,
    private val until: Int,
    private val textStyle: TextStyle?,
    private val highlightedTextStyle: TextStyle?,
    private val transition: Transition<Int>
) {
    val items = mutableListOf<RevealedItem>()

    fun item(item: RevealedItem) {
        items.add(item)
    }

    fun item(stateCount: Int = 1, content: @Composable () -> Unit) {
        items.add(RevealedItem(stateCount, content))
    }

    fun stringItem(content: String, stateCount: Int = 1) {
        items.add(RevealedItem(stateCount) { Text(content) })
    }

    fun annotatedStringItem(stateCount: Int = 1, content: @Composable AnnotatedString.Builder.() -> Unit) {
        items.add(RevealedItem(stateCount) { Text(content = content) })
    }

    @Composable
    fun Content() {
        // Calculate cumulative state positions for each item
        var cumulativeState = since
        
        // Calculate the state when the last item appears and when highlighting should be removed
        val lastItemStartState = since + items.sumOf { it.stateCount } - (items.lastOrNull()?.stateCount ?: 0)
        val highlightRemovalState = lastItemStartState + (items.lastOrNull()?.stateCount ?: 0)
        
        // Get current target state once at the beginning
        val currentTargetState = transition.targetState

        for ((index, revealedItem) in items.withIndex()) {
            val itemStartState = cumulativeState
            val nextItemStartState = cumulativeState + revealedItem.stateCount
            
            // Determine if this item should be highlighted
            // An item is highlighted if:
            // 1. It's the most recently appeared item (target state is within its reveal range)
            // 2. We haven't reached the highlight removal state yet
            val shouldHighlight = currentTargetState >= itemStartState && 
                                 currentTargetState < nextItemStartState &&
                                 currentTargetState < highlightRemovalState

            val styleToUse = if (shouldHighlight && highlightedTextStyle != null) {
                textStyle?.merge(highlightedTextStyle)
            } else {
                textStyle
            }

            transition.SlideFromTopAnimatedVisibility({ it in itemStartState..< until }) {
                if (styleToUse != null) {
                    ProvideTextStyle(styleToUse) {
                        revealedItem.composable()
                    }
                } else {
                    revealedItem.composable()
                }
            }

            cumulativeState = nextItemStartState
        }
    }
}

data class RevealedItem(
    val stateCount: Int = 1,
    val composable: @Composable () -> Unit,
)