package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.Header
import dev.panuszewski.template.HeaderScaffold
import dev.panuszewski.template.MagicCodeSample
import dev.panuszewski.template.code.buildCodeSamples
import dev.panuszewski.template.code1

fun StoryboardBuilder.GuardConditionsInWhen(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val sample = transition.createChildTransition { SAMPLES[start + it.toState()] }

        HeaderScaffold(
            header = { Text("Guard conditions") },
            body = { padding ->
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    sample.MagicCodeSample(modifier = Modifier.padding(padding))
                }
            })
    }
}

private val SAMPLES = buildCodeSamples {
    val oldWhen by tag("")
    val newWhen by tag("")

    val bookSample = """
        when (user) {
            is Admin -> allowFullAccess()${oldWhen}
            is Manager -> {
                if (user.hasPermission("edit")) {
                    allowEditAccess()
                } else {
                    allowViewAccess()
                }
            }${oldWhen}${newWhen}
            is Manager -> allowViewAccess()
            is Manager if user.hasPermission("edit") -> allowEditAccess()${newWhen}
            is Guest -> denyAccess()
        }
    """.trimIndent().toCodeSample()

    bookSample.hide(newWhen).then { reveal(newWhen).hide(oldWhen) }
}
