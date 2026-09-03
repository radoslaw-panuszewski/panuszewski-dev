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
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.h3
import dev.panuszewski.template.extensions.withIntTransition

fun StoryboardBuilder.TitleScene() {

    scene(
        stateCount = 2,
        enterTransition = { fadeIn(tween(3000)) },
        exitTransition = { fadeOut(tween(durationMillis = 10000, delayMillis = 3000)) },
    ) {

        withIntTransition {
            SlidingTitleScaffold("Test Federation in TeamCity") {

            }
        }
    }
}
