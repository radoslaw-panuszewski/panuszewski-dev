package dev.panuszewski.template.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.extensions.h2

fun StoryboardBuilder.BigTitle(title: String) {
    scene(2) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            h2 { AnimatedTitle(title) }
        }
    }
}
