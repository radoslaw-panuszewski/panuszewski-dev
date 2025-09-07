package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.IDE
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.AmperCatchErrorsEarly() {
    val initialState = 0
    val ideExpands = initialState + 1
    val finalState = initialState + 10

    scene(finalState + 1) {
        withStateTransition {
            val ideTopPadding by animateDp { if (it >= ideExpands) 0.dp else 281.dp }

            AmperTitleScaffold("Catching errors early") {
                IDE(
                    ideState = AMPER_IDE_STATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp)
                )
            }
        }
    }
}
