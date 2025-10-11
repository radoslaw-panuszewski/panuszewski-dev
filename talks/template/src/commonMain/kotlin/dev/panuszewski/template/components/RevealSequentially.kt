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
    configure: RevealSequentiallyScope.() -> Unit
): Int =
    createChildTransition { it.toState() }
        .RevealSequentially(since, until, textStyle, configure)

@Composable
fun Transition<Int>.RevealSequentially(
    since: Int = 1,
    until: Int = Int.MAX_VALUE,
    textStyle: TextStyle? = MaterialTheme.typography.h6,
    configure: RevealSequentiallyScope.() -> Unit
): Int {
    val scope = RevealSequentiallyScope(since, until)
    scope.configure()
    with(scope) {
        if (textStyle != null) {
            ProvideTextStyle(textStyle) { Content() }
        } else {
            Content()
        }
    }
    return since + scope.items.size
}

class RevealSequentiallyScope(
    private val since: Int,
    private val until: Int
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
    fun Transition<Int>.Content() {
        // Calculate cumulative state positions for each item
        var cumulativeState = since

        for (revealedItem in items) {
            val itemStartState = cumulativeState
            val nextItemStartState = cumulativeState + revealedItem.stateCount

            SlideFromTopAnimatedVisibility({ it in itemStartState..< until }) {
                revealedItem.composable()
            }

            cumulativeState = nextItemStartState
        }
    }
}

data class RevealedItem(
    val stateCount: Int = 1,
    val composable: @Composable () -> Unit,
)