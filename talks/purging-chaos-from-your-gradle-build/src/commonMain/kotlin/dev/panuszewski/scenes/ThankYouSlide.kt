package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.h2
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.appendWithColor

fun StoryboardBuilder.ThankYouSlide() {
    scene(
        stateCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withIntTransition {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CenteredColumn(Modifier.height(200.dp)) {
                    h2 { Text("Thank you!") }

                    RevealSequentially {
                        annotatedStringItem {
                            append("Any ")
                            appendWithColor(NICE_BLUE, "questions")
                            append("?")
                        }
                    }
                }
            }
        }
    }
}