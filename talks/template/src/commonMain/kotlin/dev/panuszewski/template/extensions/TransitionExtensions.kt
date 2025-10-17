package dev.panuszewski.template.extensions

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.runtime.Composable
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState

@Composable
fun SceneScope<Int>.withIntTransition(initialState: Int = 0 , content: @Composable Transition<Int>.() -> Unit) {
    val stateTransition = transition.createChildTransition { initialState + it.toState() }
    with(stateTransition) { content() }
}