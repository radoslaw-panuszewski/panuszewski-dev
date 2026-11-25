package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.h2
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.ThankYouSlide() {
    scene(
        stateCount = 2,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CenteredColumn(Modifier.height(200.dp)) {
                    h2 { Text("Thank you!") }

                    RevealSequentially {
                        annotatedStringItem {
                            append("So — which ")
                            appendWithColor(palette.blue, "combination")
                            append(" do we pick?")
                        }
                    }
                }
            }
        }
    }
}
