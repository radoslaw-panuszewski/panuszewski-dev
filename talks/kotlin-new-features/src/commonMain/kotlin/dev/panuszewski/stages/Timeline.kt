package dev.panuszewski.stages

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneExitTransition
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.panuszewski.stages.KotlinTimelineStage.FUTURE
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_0
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_1
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_2
import dev.panuszewski.template.code2

data class Node(
    val text: String,
    val children: List<Node> = emptyList()
)

enum class KotlinTimelineStage(
    val displayName: String
) {
    KOTLIN_2_0("Kotlin 2.0"),
    KOTLIN_2_1("Kotlin 2.1"),
    KOTLIN_2_2("Kotlin 2.2"),
    FUTURE("Future"),
    ;
}

class StageView(
    val stage: KotlinTimelineStage
) {
    var numberOfStatesToTake = 0
        private set

    fun reset() {
        numberOfStatesToTake = 0
    }

    fun advance() {
        numberOfStatesToTake++
    }
}

data class TimelineState(
    val nodes: Map<KotlinTimelineStage, Node>,
    val isLast: Boolean = false
)

object TimelineStateMachine {
    val stageViews = KotlinTimelineStage.entries.map(::StageView)
    var currentStageIndex = 0
    var lastItemEmitted = false

    val statesPerStage = mapOf(
        KOTLIN_2_0 to listOf(Node("K2 compiler"), Node("Performance improvements")),
        KOTLIN_2_1 to listOf(
            Node("Guard conditions"),
            Node("Non-local break & continue"),
            Node("Multi-dollar interpolation")
        ),
        KOTLIN_2_2 to listOf(Node("Context parameters")),
        FUTURE to listOf(Node("Rich errors"), Node("Must-use return values")),
    )

    fun next(): Map<KotlinTimelineStage, Node>? {
        if (lastItemEmitted) {
            return null
        }

        if (currentStageIndex > stageViews.lastIndex) {
            lastItemEmitted = true
            return stageViews.associate { it.stage to Node(it.stage.displayName) }
        }

        val map = buildMap {
            for (stageView in stageViews) {
                val states = statesPerStage[stageView.stage]!!
                put(stageView.stage, Node(stageView.stage.displayName, states.take(stageView.numberOfStatesToTake)))
            }
        }
        if (currentStageView().numberOfStatesToTake == statesPerStage[currentStage()]!!.size) {
            currentStageView().reset()
            nextStage()
        } else {
            currentStageView().advance()
        }

        return map
    }

    private fun nextStage() {
        currentStageIndex++
    }

    private fun currentStageView() =
        stageViews[currentStageIndex]

    private fun currentStage() =
        currentStageView().stage
}

private val nodeStates = generateSequence { TimelineStateMachine.next() }
    .toList()

fun StoryboardBuilder.Timeline(stageToBeExpanded: KotlinTimelineStage) {
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