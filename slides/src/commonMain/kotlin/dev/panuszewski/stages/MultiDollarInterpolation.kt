package dev.bnorm.kc25.sections.stages

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.toState
import dev.panuszewski.stages.BoxMovementSpec
import dev.panuszewski.stages.FadeInOutAnimatedVisibility
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_1
import dev.panuszewski.stages.TextMovementSpec
import dev.panuszewski.stages.tag
import dev.panuszewski.template.code.buildCodeSamples
import dev.panuszewski.template.code1

fun StoryboardBuilder.MultiDollarInterpolation() {
    scene(stateCount = SAMPLES.size + 1) {
        Box(Modifier.padding(horizontal = 100.dp, vertical = 50.dp)) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("box/$KOTLIN_2_1"),
                        animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        boundsTransform = BoxMovementSpec
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Multi-dollar interpolation",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("text/Multi-dollar interpolation"),
                                animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                                boundsTransform = TextMovementSpec
                            )
                    )
                    Box(
                        modifier = Modifier.padding(32.dp).fillMaxSize(),
                    ) {
                        transition.createChildTransition {
                            if (it.toState() >= 1) {
                                transition.FadeInOutAnimatedVisibility(visible = { it != Frame.End }) {
                                    ProvideTextStyle(MaterialTheme.typography.code1) {
                                        val sample = SAMPLES[(it.toState() - 1).coerceIn(SAMPLES.indices)]
                                        val sampleString = sample.string
                                        val words = sampleString.toWords()
                                        MagicText(words)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val SAMPLES = buildCodeSamples {
    val escaped by tag()
    val unescapedSingle by tag()
    val unescapedDouble by tag()
    val twoDollarPrefix by tag()
    val threeDollarPrefix by tag()

    val codeSample = $$$$"""
        val graphqlQuery = $$$${twoDollarPrefix}$$$$$${twoDollarPrefix}$$$${threeDollarPrefix}$$$$$$${threeDollarPrefix}$$$${"\"\"\""}    
            query GetBookById($$$${escaped}${"$"}$$$${escaped}$$$${unescapedSingle}$$$$${unescapedSingle}$$$${unescapedDouble}$$$$$${unescapedDouble}bookId: ID!) {
              book(id: $$$${escaped}${"$"}$$$${escaped}$$$${unescapedSingle}$$$$${unescapedSingle}$$$${unescapedDouble}$$$$$${unescapedDouble}bookId) {
                title
                author {
                  name
                }
              }
            }
            $$$${"\"\"\""} 
        """.trimIndent().toCodeSample()

    codeSample.hide(twoDollarPrefix, threeDollarPrefix, escaped, unescapedDouble).let(::listOf)
        .then { hide(unescapedSingle).reveal(escaped) }
        .then { reveal(twoDollarPrefix) }
        .then { hide(escaped).reveal(unescapedSingle) }
        .then { hide(twoDollarPrefix, unescapedSingle).reveal(threeDollarPrefix, unescapedDouble) }
}
