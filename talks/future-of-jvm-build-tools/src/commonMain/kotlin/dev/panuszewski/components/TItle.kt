package dev.panuszewski.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.panuszewski.template.h4

@Composable
context(_: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope)
fun Title(title: String) {
    Spacer(Modifier.height(16.dp))
    AnimatedContent(targetState = title) { targetTitle ->
        h4 { Text(targetTitle) }
    }
    Spacer(Modifier.height(32.dp))
}