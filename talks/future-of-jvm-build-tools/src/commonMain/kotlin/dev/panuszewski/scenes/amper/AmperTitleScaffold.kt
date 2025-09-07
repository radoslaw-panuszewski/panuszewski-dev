package dev.panuszewski.scenes.amper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.components.Title

@Composable
fun SceneScope<*>.AmperTitleScaffold(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (transition.currentState) {
            is Frame.State<*> -> AMPER_TITLE = title
            else -> {}
        }
        Title(AMPER_TITLE)
        content()
    }
}