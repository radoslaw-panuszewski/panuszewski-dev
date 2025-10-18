package dev.panuszewski.scenes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.withStyle
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.h3
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import org.jetbrains.compose.resources.painterResource
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.chaotic_groovy_dark

fun StoryboardBuilder.TitleScene() {

    val titleDisappears = 1
    val chaoticConfigAppears = titleDisappears
    val waitBeforeScroll = chaoticConfigAppears + 1

    scene(
        stateCount = waitBeforeScroll,
        enterTransition = { fadeIn(tween(3000)) },
        exitTransition = { fadeOut(tween(durationMillis = 10000, delayMillis = 3000)) },
    ) {

        transition.FadeOutAnimatedVisibility({ it.toState() < titleDisappears }) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                h3 {
                    Text {
                        append("Purging the ")
                        withStyle(SpanStyle(color = Color(0xFF682F29), fontWeight = Medium)) { append("Chaos") }
                    }
                    Text("from Your Gradle Build")
                }
            }
        }

        var shouldScroll by remember { mutableStateOf(false) }
        
        LaunchedEffect(transition.currentState) {
            if (transition.currentState == Frame.End) {
                shouldScroll = true
            }
        }

        val scrollProgress by animateFloatAsState(
            targetValue = if (shouldScroll) 1f else 0f,
            animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
        )
        
        val scrollPosition = (scrollProgress * 10886).toInt()

        transition.FadeInOutAnimatedVisibility({ it.toState() >= chaoticConfigAppears }, tween(2000)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
                    .verticalScroll(ScrollState(scrollPosition)),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .paint(
                            painter = painterResource(Res.drawable.chaotic_groovy_dark),
                            contentScale = ContentScale.Crop
                        )
                )
            }
        }
    }
}
