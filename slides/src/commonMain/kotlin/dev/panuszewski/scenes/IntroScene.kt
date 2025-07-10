package dev.panuszewski.scenes

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Header

fun StoryboardBuilder.IntroScene() = scene {
    Header(Modifier.padding(all = 20.dp)) {
        Text("Elo")
    }
}