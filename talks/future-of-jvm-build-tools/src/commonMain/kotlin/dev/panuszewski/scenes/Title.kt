package dev.panuszewski.scenes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.MagicText
import dev.panuszewski.template.animateTextStyle
import dev.panuszewski.template.safeGet

val TITLE = listOf(
    buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(Color(54, 161, 165))) { append("Future") }
        append(" of JVM Build Tools")
    },
    buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(Color(148, 45, 243))) { append("Present") }
        append(" of JVM Build Tools")
    }
)

fun StoryboardBuilder.Title() {
    scene(2) {
        val titleTransition = transition.createChildTransition { TITLE.safeGet(it.toState()) }

        val textStyle by transition.animateTextStyle({ tween(durationMillis = 500) }) {
            when (it.toState()) {
                0, 1 -> MaterialTheme.typography.h2
                else -> MaterialTheme.typography.h4
            }
        }

        val yOffset by transition.animateDp({ tween(durationMillis = 500) }) {
            when (it.toState()) {
                0, 1 -> -16.dp
                else -> -225.dp
            }
        }

        val paddingStart by transition.animateDp({ tween(durationMillis = 500) }) {
            when (it.toState()) {
                0, 1 -> 64.dp
                else -> 225.dp
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProvideTextStyle(MaterialTheme.typography.h2) {
                titleTransition.MagicText(
                    Modifier
                        .offset(y = -16.dp)
                        .padding(start = 64.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("text/Present"),
                            animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                        )
                )
            }
        }
    }
}

//private val TITLE: List<CodeSample> = buildCodeSamples {
//    val future by tag()
//    val present by tag()
//    val sample = buildAnnotatedString {
//        append("The ")
//        withStyle(INTELLIJ_LIGHT_CODE_STYLE.keyword) { append("Future") }
//        append(" of JVM Build Tools")
//    }
//        .toCodeSample(
//            codeStyle = INTELLIJ_LIGHT_CODE_STYLE,
//            identifierType = { codeStyle, text ->
//                when (text) {
//                    "Future" -> codeStyle.keyword
//                    "Present" -> codeStyle.keyword
//                    else -> null
//                }
//            }
//        )

//    sample.hide(present)
//        .then { hide(future).reveal(present) }
//}