package dev.panuszewski.template

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.text.magic.DefaultDelayDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultFadeDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultMoveDurationMillis
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.toDpSize

@Composable
fun Transition<AnnotatedString>.MagicText(
    modifier: Modifier = Modifier,
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
    split: (AnnotatedString) -> List<AnnotatedString> = { it.toWords() }
) {
    MagicText(
        transition = createChildTransition { split(it) },
        modifier = modifier,
        moveDurationMillis = moveDurationMillis,
        fadeDurationMillis = fadeDurationMillis,
        delayDurationMillis = delayDurationMillis
    )
}

@Composable
fun Transition<CodeSample>.MagicCodeSample(
    modifier: Modifier = Modifier,
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
    split: (AnnotatedString) -> List<AnnotatedString> = { it.toWords() }
) {
    MagicText(
        transition = createChildTransition { codeSample -> split(codeSample.String()) },
        modifier = modifier,
        moveDurationMillis = moveDurationMillis,
        fadeDurationMillis = fadeDurationMillis,
        delayDurationMillis = delayDurationMillis
    )
}

@Composable
fun Transition<CodeSample>.ScrollableMagicCodeSample(
    modifier: Modifier = Modifier,
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
    split: (AnnotatedString) -> List<AnnotatedString> = { it.toWords() }
) {
    val state = rememberScrollState()
    animateScroll(state)

    Box(
        // Scroll should come before any padding, so it is not clipped.
        Modifier.verticalScroll(state, enabled = false)
            .then(modifier)
            // Allow scrolling to the very bottom.
            .padding(bottom = LocalStoryboard.current?.format?.toDpSize()?.height ?: 0.dp)
    ) {
        MagicCodeSample(
            modifier = modifier,
            moveDurationMillis = moveDurationMillis,
            fadeDurationMillis = fadeDurationMillis,
            delayDurationMillis = delayDurationMillis,
            split = split
        )
    }
}

@Composable
fun Transition<CodeSample>.animateScroll(
    verticalScrollState: ScrollState,
    style: TextStyle = LocalTextStyle.current,
    transitionSpec: @Composable Transition.Segment<CodeSample>.() -> FiniteAnimationSpec<Float> = {
        tween(DefaultMoveDurationMillis, delayMillis = DefaultFadeDurationMillis, easing = EaseInOut)
    },
    label: String = "ScrollAnimation",
) {
    // TODO auto-scroll doesn't seem to be working on the companion app...
    //  - i bet it's the same problem as with the start animation!
    //  - something to do with SeekableTransitionState.snapTo()?
    val lineHeight = with(LocalDensity.current) { style.lineHeight.toPx() }
    val scrollPosition by animateFloat(transitionSpec, label) { it.Scroll() * lineHeight }
    verticalScrollState.dispatchRawDelta(scrollPosition - verticalScrollState.value)
}