package dev.panuszewski.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope

private var SHARED_TITLE: String = "Amper"

@Composable
fun SceneScope<*>.TitleScaffold(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (transition.currentState) {
            is Frame.State<*> -> SHARED_TITLE = title
            else -> {}
        }
        Title(SHARED_TITLE)
        content()
    }
}