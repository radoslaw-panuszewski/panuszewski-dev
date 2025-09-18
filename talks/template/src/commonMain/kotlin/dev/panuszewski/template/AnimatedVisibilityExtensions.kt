package dev.panuszewski.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Transition<Boolean>.AnimatedVisibility(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = { it },
    modifier = modifier,
    enter = enter,
    exit = exit,
    content = content,
)