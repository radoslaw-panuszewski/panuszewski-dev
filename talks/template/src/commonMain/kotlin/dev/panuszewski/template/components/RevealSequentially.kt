package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
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
    return since + scope.composables.size
}

class RevealSequentiallyScope(
    private val since: Int,
    private val until: Int
) {
    val composables = mutableListOf<RevealedItem>()

    fun item(stateCount: Int = 1, content: @Composable () -> Unit) {
        composables.add(RevealedItem(stateCount, content))
    }

    fun textItem(stateCount: Int = 1, content: @Composable AnnotatedString.Builder.() -> Unit) {
        composables.add(RevealedItem(stateCount) { Text(content) })
    }

    @Composable
    fun Transition<Int>.Content() {
        for ((index, revealedItem) in composables.withIndex()) {
            SlideFromTopAnimatedVisibility({ index + since <= it && it < since + composables.size && it < until }) {
                revealedItem.composable()
            }
        }
    }
}

data class RevealedItem(
    val stateCount: Int,
    val composable: @Composable () -> Unit,
)