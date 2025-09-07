package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.panuszewski.components.IDE
import dev.panuszewski.template.Stages

@Composable
fun Transition<Int>.AmperCatchErrorsEarly(stages: Stages, title: MutableState<String>) {
    val initialState = stages.lastState + 1
    val ideExpands = initialState + 1
    val finalState = initialState + 10

    val ideTopPadding by animateDp { if (it >= ideExpands) 0.dp else 281.dp }

    if (currentState in initialState..finalState) {
        IDE(
            ideState = AMPER_IDE_STATE,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp)
        )
    }

    val states = remember { stages.registerStatesByRange(start = initialState, end = finalState) }

    if (currentState in states) {
        title.value = "Catching errors early"
    }
}
