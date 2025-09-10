package dev.panuszewski.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.h4

@Composable
fun Title(title: String) {
    Spacer(Modifier.height(16.dp))
    AnimatedContent(targetState = title) { targetTitle ->
        h4 { Text(targetTitle) }
    }
    Spacer(Modifier.height(32.dp))
}