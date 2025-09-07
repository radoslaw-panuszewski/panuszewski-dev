package dev.panuszewski.template

import androidx.compose.animation.core.Transition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

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
    val composables = mutableListOf<@Composable () -> Unit>()

    fun item(content: @Composable () -> Unit) {
        composables.add(content)
    }

    @Composable
    fun Transition<Int>.Content() {
        for ((index, composable) in composables.withIndex()) {
            SlideFromTopAnimatedVisibility({ index + since <= it && it < since + composables.size && it < until }) {
                composable()
            }
        }
    }
}