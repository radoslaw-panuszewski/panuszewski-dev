package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.theme.appendWithSecondaryColor

fun StoryboardBuilder.Agenda() {
    scene(stateCount = 3) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))

            ProvideTextStyle(MaterialTheme.typography.h4) {
                Text(
                    text = TITLE[0],
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/Title"),
                            animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        )
                )
            }

            Spacer(Modifier.height(32.dp))

            transition.AnimatedVisibility(
                visible = { it.toState() == 1 },
                enter = slideInVertically(initialOffsetY = { it }),
                exit = fadeOut()
            ) {
                Column(
                    Modifier.padding(vertical = 32.dp, horizontal = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text {
                        append("1. Current state of the world")
                    }
                    Text {
                        append("2. ")
                        appendWithSecondaryColor("Maven 4")
                        append(" on the horizon")
                    }
                    Text {
                        append("3. ")
                        appendWithSecondaryColor("Gradle")
                        append(" and its path towards declarativeness")
                    }
                    Text {
                        append("4. ")
                        appendWithSecondaryColor("Amper")
                        append(" - a brand new build tool from JetBrains")
                    }
                    Text {
                        append("5. Wrapping up")
                    }
                }
            }
        }
    }
}
