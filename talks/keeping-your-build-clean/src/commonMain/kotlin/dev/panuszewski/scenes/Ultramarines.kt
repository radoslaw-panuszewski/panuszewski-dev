package dev.panuszewski.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.painterResource
import talks.keeping_your_build_clean.generated.resources.Res
import talks.keeping_your_build_clean.generated.resources.chaos
import talks.keeping_your_build_clean.generated.resources.ultramarines

fun StoryboardBuilder.Ultramarines() {

    scene(
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(Res.drawable.ultramarines),
                    contentScale = ContentScale.Crop
                )
        )
    }
}
