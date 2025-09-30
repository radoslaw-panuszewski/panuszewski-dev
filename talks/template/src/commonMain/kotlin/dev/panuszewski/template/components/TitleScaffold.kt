package dev.panuszewski.template.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h4

@Composable
fun SceneScope<*>.TitleScaffold(title: String?, content: @Composable BoxScope.() -> Unit) {
    TitleScaffold(title?.annotate(), content)
}

@Composable
fun SceneScope<*>.TitleScaffold(title: AnnotatedString?, content: @Composable BoxScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        h4 { AnimatedTitle(title) }
        Box(modifier = Modifier.padding(32.dp)) {
            content()
        }
    }
}