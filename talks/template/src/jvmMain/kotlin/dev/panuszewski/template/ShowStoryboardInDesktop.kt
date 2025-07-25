@file:OptIn(ExperimentalStoryStateApi::class)

package dev.panuszewski.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.DesktopStoryEasel
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.bnorm.storyboard.easel.StoryState

fun showStoryboardInDesktop(storyboard: Storyboard) {
    val state = StoryState()

    application {
        remember {
            storyboard.also(state::updateStoryboard)
        }

        MaterialTheme(colors = darkColors()) {
            DesktopStoryEasel(
                storyState = state,
                overlay = {},
            )
        }
    }
}