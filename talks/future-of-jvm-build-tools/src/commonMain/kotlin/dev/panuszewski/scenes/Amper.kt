package dev.panuszewski.scenes

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.h4
import dev.panuszewski.template.withPrimaryColor

fun StoryboardBuilder.Amper() {
    scene(100) {
        val stateTransition = transition.createChildTransition { it.toState() }
        var title by remember { mutableStateOf("Amper") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Title(title)
            stateTransition.BriefDescription()
        }
    }
}

@Composable
private fun Title(title: String) {
    Spacer(Modifier.height(16.dp))
    h4 { Text(title) }
    Spacer(Modifier.height(32.dp))
}

@Composable
private fun Transition<Int>.BriefDescription() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RevealSequentially(textStyle = MaterialTheme.typography.h6) {
            item {
                Text {
                    append("Started as a Gradle plugin, now a ")
                    withPrimaryColor { append("standalone") }
                    append(" build tool")
                }
            }
            item { Text { append("Focused on DEVEX and toolability") } }
        }
    }
}

@Composable
fun Transition<Int>.RevealSequentially(startState: Int = 1, textStyle: TextStyle? = null, configure: RevealSequentiallyScope.() -> Unit) {
    val scope = RevealSequentiallyScope(startState)
    scope.configure()
    with(scope) {
        if (textStyle != null) {
            ProvideTextStyle(textStyle) { Content() }
        } else {
            Content()
        }
    }
}

class RevealSequentiallyScope(
    private val startState: Int,
) {
    private val composables = mutableListOf<@Composable () -> Unit>()

    fun item(content: @Composable () -> Unit) {
        composables.add(content)
    }

    @Composable
    fun Transition<Int>.Content() {
        for ((index, composable) in composables.withIndex()) {
            SlideFromTopAnimatedVisibility({ it >= index + startState }) {
                composable()
            }
        }
    }
}