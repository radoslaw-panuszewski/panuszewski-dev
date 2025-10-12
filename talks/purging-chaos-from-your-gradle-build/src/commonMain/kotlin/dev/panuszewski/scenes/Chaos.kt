package dev.panuszewski.scenes

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import dev.bnorm.storyboard.AdvanceDirection.Forward
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import org.jetbrains.compose.resources.painterResource
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.chaos

fun StoryboardBuilder.Chaos() {

    scene(
        enterTransition = { direction -> if (direction == Forward) fadeIn(tween(3000)) else SceneEnter(CenterEnd).invoke(direction) },
        exitTransition = { direction -> if (direction == Forward) SceneExit(CenterEnd).invoke(direction) else fadeOut(tween(3000)) },
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clipToBounds()
                .paint(
                    painter = painterResource(Res.drawable.chaos),
                    contentScale = ContentScale.Crop
                )
        )
    }
}
