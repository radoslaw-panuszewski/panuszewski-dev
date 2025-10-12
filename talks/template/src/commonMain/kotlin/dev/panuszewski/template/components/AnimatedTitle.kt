package dev.panuszewski.template.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
    val titleToDisplay = if (title != null) {
        buildAnnotatedString {
            // surrounded by invisible emojis to fix issue with hoping height when real emoji is added :)
            withStyle(SpanStyle(color = Color.Transparent)) { append("🥞") }
            append(title)
            withStyle(SpanStyle(color = Color.Transparent)) { append("🥞") }
        }
    } else null

    when (transition.currentState) {
        is Frame.State<*> if titleToDisplay != null -> SHARED_TITLE = titleToDisplay
        else -> {}
    }
    if (SHARED_TITLE == null) {
        SHARED_TITLE = titleToDisplay
    }
    SHARED_TITLE?.let {
        AnimatedContent(targetState = it) { targetTitle ->
            Text(text = targetTitle, modifier = modifier)
        }
    }
}