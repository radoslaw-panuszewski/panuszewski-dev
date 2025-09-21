package dev.panuszewski.template.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.annotate

private var SHARED_TITLE: AnnotatedString? = null

@Composable
fun SceneScope<*>.AnimatedTitle(title: String?, modifier: Modifier = Modifier) {
    AnimatedTitle(title?.annotate(), modifier)
}

@Composable
fun SceneScope<*>.AnimatedTitle(title: AnnotatedString?, modifier: Modifier = Modifier) {
    when (transition.currentState) {
        is Frame.State<*> if title != null -> SHARED_TITLE = title
        else -> {}
    }
    if (SHARED_TITLE == null) {
        SHARED_TITLE = title
    }
    SHARED_TITLE?.let {
        AnimatedContent(targetState = it) { targetTitle ->
            Text(text = targetTitle, modifier = modifier)
        }
    }
}