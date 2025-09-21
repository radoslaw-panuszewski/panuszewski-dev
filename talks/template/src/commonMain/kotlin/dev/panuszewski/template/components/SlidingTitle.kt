package dev.panuszewski.template.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.AnimatedTitle
import dev.panuszewski.template.extensions.animateTextStyle
import dev.panuszewski.template.extensions.annotate

@Composable
fun SceneScope<Int>.SlidingTitle(title: String) {
    SlidingTitle(title.annotate())
}

@Composable
fun SceneScope<Int>.SlidingTitle(title: AnnotatedString) {
    val durationMillis = 500
    val isLargeTitle = transition.createChildTransition { it.toState(end = 4) in listOf(0, 4) }

    val titleTextStyle by isLargeTitle.animateTextStyle(
        targetValueByState = { isLarge ->
            if (isLarge) MaterialTheme.typography.h2 else MaterialTheme.typography.h4
        },
        transitionSpec = { tween(durationMillis) }
    )

    val titleVerticalOffset by isLargeTitle.animateDp(
        targetValueByState = { isLarge -> if (isLarge) 0.dp else -200.dp },
        transitionSpec = { tween(durationMillis) }
    )

    ProvideTextStyle(titleTextStyle) {
        AnimatedTitle(
            title = title,
            modifier = Modifier.offset(y = titleVerticalOffset)
        )
    }
}