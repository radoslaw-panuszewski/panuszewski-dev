package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.Title
import dev.panuszewski.template.components.AnimatedTitle
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.extensions.h4
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.speaker_photo

fun StoryboardBuilder.IntroductionSlide() {
    scene(
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Title { h4 { Text("About me") } }
            Box(modifier = Modifier.padding(32.dp)) {
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    ResourceImage(Res.drawable.speaker_photo)
                }
            }
        }
    }
}