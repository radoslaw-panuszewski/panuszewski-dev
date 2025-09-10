package dev.panuszewski.scenes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.panuszewski.template.ResourceImage
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.first_slide_green
import talks.future_of_jvm_build_tools.generated.resources.first_slide_original
import talks.future_of_jvm_build_tools.generated.resources.thank_you_slide

fun StoryboardBuilder.ThankYouSlide() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        ResourceImage(Res.drawable.thank_you_slide, modifier = Modifier.fillMaxSize())
    }
}