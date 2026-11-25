package dev.panuszewski.scenes

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.h3
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.TitleScene() {
    scene(
        stateCount = 2,
        enterTransition = { fadeIn(tween(1500)) },
        exitTransition = { fadeOut(tween(500)) },
    ) {
        withIntTransition {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CenteredColumn {
                    h3 {
                        Text {
                            appendWithColor(palette.turquoise, "Test Federation")
                            append(" setup on TeamCity")
                        }
                    }

                    RevealSequentially {
                        annotatedStringItem {
                            append("A ")
                            appendWithColor(palette.orange, "design doc")
                            append(" review")
                        }
                    }
                }
            }
        }
    }
}
