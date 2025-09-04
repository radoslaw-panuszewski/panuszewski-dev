package dev.panuszewski.scenes

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.h6

fun StoryboardBuilder.WrappingUp() {
    scene(5) {
        val stateTransition = transition.createChildTransition { it.toState() }
        stateTransition.Bulletpoints()
    }
}

@Composable
fun Transition<Int>.Bulletpoints() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SlideFromTopAnimatedVisibility({ it >= 1 }) {
            h6 { Text("Maven 4 is done with XML!") }
        }

        SlideFromTopAnimatedVisibility({ it >= 2 }) {
            h6 { Text("Gradle configuration cache can save you a lot of time") }
        }

        SlideFromTopAnimatedVisibility({ it >= 3 }) {
            h6 { Text("Declarative Gradle is what's Gradle needs the most!") }
        }

        SlideFromTopAnimatedVisibility({ it >= 4 }) {
            h6 { Text("Amper is really good for simple projects") }
        }
    }
}