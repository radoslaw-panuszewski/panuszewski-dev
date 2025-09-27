package dev.panuszewski.template.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.easel.LocalStoryboard
import dev.bnorm.storyboard.text.magic.DefaultDelayDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultFadeDurationMillis
import dev.bnorm.storyboard.text.magic.DefaultMoveDurationMillis
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toDpSize
import dev.panuszewski.template.components.spans.SquigglyUnderlineSpanPainter
import dev.panuszewski.template.components.spans.rememberSquigglyUnderlineAnimator
import dev.panuszewski.template.extensions.TagType
import kotlin.time.Duration.Companion.seconds

@Composable
fun Transition<CodeSample>.EnhancedMagicCodeSample(
    modifier: Modifier = Modifier,
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
    showWarningUnderlines: Boolean = true,
) {
    val style = LocalTextStyle.current
    val measurer = rememberTextMeasurer()
    val animator = rememberSquigglyUnderlineAnimator(2.seconds)
    val painter = remember(animator) {
        SquigglyUnderlineSpanPainter(
            width = 1.sp,
            wavelength = 6.sp,
            bottomOffset = 0.sp,
            animator = animator
        )
    }

    val transition = createChildTransition { codeSample -> codeSample.Split() }

    if (showWarningUnderlines) {
        val warningRanges = extractWarningRanges(currentState)
        val sample = currentState.String()

        MagicText(
            transition = transition,
            modifier = modifier.drawBehind {
                if (warningRanges.isNotEmpty()) {
                    val result = measurer.measure(sample, style = style)
                    with(painter.drawInstructionsFor(result, warningRanges)) { draw() }
                }
            },
            moveDurationMillis = moveDurationMillis,
            fadeDurationMillis = fadeDurationMillis,
            delayDurationMillis = delayDurationMillis
        )
    } else {
        MagicText(
            transition = transition,
            modifier = modifier,
            moveDurationMillis = moveDurationMillis,
            fadeDurationMillis = fadeDurationMillis,
            delayDurationMillis = delayDurationMillis
        )
    }
}

@Composable
fun Transition<CodeSample>.ScrollableEnhancedMagicCodeSample(
    modifier: Modifier = Modifier,
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
    scrollTransitionSpec: @Composable Transition.Segment<CodeSample>.() -> FiniteAnimationSpec<Float> = {
        tween(DefaultMoveDurationMillis, delayMillis = DefaultFadeDurationMillis, easing = EaseInOut)
    },
    scrollMargin: Int = 0,
    showWarningUnderlines: Boolean = true,
) {
    val state = rememberScrollState()
    val lineHeight = with(LocalDensity.current) { LocalTextStyle.current.lineHeight.toPx() }
    val scrollPosition by animateFloat(scrollTransitionSpec, "ScrollAnimation") { (it.Scroll() - scrollMargin) * lineHeight }
    state.dispatchRawDelta(scrollPosition - state.value)

    Box(
        // Scroll should come before any padding, so it is not clipped.
        Modifier.verticalScroll(state, enabled = false)
            .then(modifier)
            // Allow scrolling to the very bottom.
            .padding(bottom = LocalStoryboard.current?.format?.toDpSize()?.height ?: 0.dp)
    ) {
        EnhancedMagicCodeSample(
            modifier = modifier,
            moveDurationMillis = moveDurationMillis,
            fadeDurationMillis = fadeDurationMillis,
            delayDurationMillis = delayDurationMillis,
            showWarningUnderlines = showWarningUnderlines,
        )
    }
}

@Composable
private fun extractWarningRanges(codeSample: CodeSample): Map<IntRange, Color> {
    val ranges = mutableMapOf<IntRange, Color>()
    val processedString = codeSample.String()

    // Use the warning tags that were stored during CodeSample creation
    for (warningTag in codeSample.warningTags) {
        // Find annotations for this specific warning tag
        val annotations = processedString.getStringAnnotations(warningTag.annotationStringTag, 0, processedString.length)

        for (annotation in annotations) {
            if (annotation.item == warningTag.id) {
                val range = annotation.start..<annotation.end
                val adjustedRanges = createRangesSkippingIndentation(processedString.text, range)
                for (adjustedRange in adjustedRanges) {
                    ranges[adjustedRange] = Color.Yellow
                }
            }
        }
    }

    return ranges
}

private fun createRangesSkippingIndentation(text: String, range: IntRange): List<IntRange> {
    if (range.isEmpty()) return listOf(range)

    val result = mutableListOf<IntRange>()
    var currentPos = range.first

    while (currentPos <= range.last && currentPos < text.length) {
        // Find the start of the current line (skip indentation)
        var lineStart = currentPos
        while (lineStart <= range.last && lineStart < text.length &&
               (text[lineStart] == ' ' || text[lineStart] == '\t')) {
            lineStart++
        }

        // Find the end of the current line
        var lineEnd = lineStart
        while (lineEnd <= range.last && lineEnd < text.length && text[lineEnd] != '\n') {
            lineEnd++
        }

        // Add this line's range if it has content
        if (lineStart <= lineEnd && lineStart <= range.last) {
            result.add(lineStart..minOf(lineEnd - 1, range.last))
        }

        // Move to the next line
        currentPos = lineEnd + 1
    }

    // If no ranges were created, return the original range
    return result.ifEmpty { listOf(range) }
}

