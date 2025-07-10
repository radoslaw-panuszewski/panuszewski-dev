package dev.panuszewski

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.template.SceneIndexDecorator
import dev.panuszewski.template.storyDecorator

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    val state = StoryState()

    application {
        val infiniteTransition = rememberInfiniteTransition()

        remember {
            val decorator = SceneDecorator.from(
                SceneIndexDecorator(state),
                storyDecorator(infiniteTransition),
            )
            createStoryboard(decorator).also { state.updateStoryboard(it) }
        }

        MaterialTheme(colors = darkColors()) {
            DesktopStoryEasel(
                storyState = state,
                overlay = {},
            )
        }
    }
}
