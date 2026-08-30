package dev.panuszewski.scenes

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.h3

fun StoryboardBuilder.TitleScene() {

    val titleDisappears = 1

    scene(
        stateCount = titleDisappears,
        enterTransition = { fadeIn(tween(3000)) },
        exitTransition = { fadeOut(tween(durationMillis = 10000, delayMillis = 3000)) },
    ) {

        transition.FadeOutAnimatedVisibility({ it.toState() < titleDisappears }) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                h3 {
                    Text("Test Federation in TeamCity")
                }
            }
        }
    }
}
