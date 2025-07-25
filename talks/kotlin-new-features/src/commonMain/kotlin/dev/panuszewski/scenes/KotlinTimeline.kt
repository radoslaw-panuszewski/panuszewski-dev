package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder

enum class KotlinTimelineStage(
    val displayName: String
) {
    KOTLIN_2_0("Kotlin 2.0"),
    KOTLIN_2_1("Kotlin 2.1"),
    KOTLIN_2_2("Kotlin 2.2"),
    FUTURE("Future"),
    ;
}

fun StoryboardBuilder.KotlinTimeline(stageToBeExpanded: KotlinTimelineStage) {
    scene(stateCount = 1) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            KotlinTimelineStage.entries.forEach { stage ->

                val visible = transition.createChildTransition { frame ->
                    when (frame) {
                        Frame.End -> stage == stageToBeExpanded
                        else -> true
                    }
                }
                TimelineStageBox(stage, visible)
            }
        }
    }
}