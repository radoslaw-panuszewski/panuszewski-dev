package dev.panuszewski.template.extensions

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.theme.code1
import dev.panuszewski.template.theme.code2
import dev.panuszewski.template.theme.code3

@Composable
fun Text(modifier: Modifier = Modifier, content: @Composable AnnotatedString.Builder.() -> Unit) {
    val builder = AnnotatedString.Builder()
    builder.content()
    Text(text = builder.toAnnotatedString(), modifier = modifier)
}

@Composable
context(_: SceneScope<Int>)
private fun Transition<out Frame<Int>>.annotatedStringTransition(texts: List<AnnotatedString>) =
    createChildTransition { texts.safeGet(it.toState()) }

@Composable
context(_: SceneScope<Int>)
private fun Transition<out Frame<Int>>.stringTransition(texts: List<String>) =
    createChildTransition { texts.safeGet(it.toState()) }

@Composable
fun h1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h1, content)
}

@Composable
fun h2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h2, content)
}

@Composable
fun h3(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h3, content)
}

@Composable
fun h4(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h4, content)
}

@Composable
fun h5(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h5, content)
}

@Composable
fun h6(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h6, content)
}

@Composable
fun subtitle1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.subtitle1, content)
}

@Composable
fun subtitle2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.subtitle2, content)
}

@Composable
fun body1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.body1, content)
}

@Composable
fun body2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.body2, content)
}

@Composable
fun button(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.button, content)
}

@Composable
fun caption(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.caption, content)
}

@Composable
fun overline(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.overline, content)
}

@Composable
fun code1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code1, content)
}

@Composable
fun code2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code2, content)
}

@Composable
fun code3(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code3, content)
}

/**
 * Splits an [AnnotatedString] into a list of "characters" for per-character magic-move animations
 * (e.g. `MagicAnnotatedString(split = { it.splitByChars() })`).
 *
 * This is a drop-in replacement for `dev.bnorm.storyboard.text.magic.splitByChars`, which naively
 * slices the string by UTF-16 code unit (`subSequence(i, i + 1)`). That breaks any character outside
 * the Basic Multilingual Plane - most emoji, like 💨 or 🤝 - into two unpaired surrogate halves, each
 * rendered as a "tofu"/invalid-character glyph.
 *
 * Instead, this keeps together:
 *  - surrogate pairs forming a single code point,
 *  - a base code point followed by variation selectors (U+FE0E/U+FE0F) or combining marks,
 *  - zero-width-joiner (U+200D) emoji sequences (e.g. 🧑‍🤝‍🧑),
 *  - regional indicator pairs that form flag emoji (e.g. 🇵🇱).
 *
 * For plain ASCII/BMP text (no surrogate pairs or combining sequences) the result is identical to
 * splitting by UTF-16 code unit, so this is a safe replacement everywhere.
 */
fun AnnotatedString.splitByChars(): List<AnnotatedString> {
    val boundaries = graphemeClusterBoundaries(text)
    return (0 until boundaries.size - 1).map { subSequence(boundaries[it], boundaries[it + 1]) }
}

private fun graphemeClusterBoundaries(text: String): List<Int> {
    val boundaries = mutableListOf(0)
    var i = 0
    while (i < text.length) {
        i += text.charCountAt(i)

        while (i < text.length) {
            val codePoint = text.codePointAt(i)
            when {
                isVariationSelector(codePoint) || isCombiningMark(codePoint) ||
                    isEnclosingKeycap(codePoint) || isSkinToneModifier(codePoint) -> {
                    i += text.charCountAt(i)
                }

                isZeroWidthJoiner(codePoint) -> {
                    i += text.charCountAt(i)
                    if (i < text.length) i += text.charCountAt(i)
                }

                else -> break
            }
        }

        val clusterStart = boundaries.last()
        if (isRegionalIndicator(text.codePointAt(clusterStart)) &&
            i < text.length && isRegionalIndicator(text.codePointAt(i))
        ) {
            i += text.charCountAt(i)
        }

        boundaries.add(i)
    }
    return boundaries
}

private fun String.charCountAt(index: Int): Int {
    val high = this[index]
    return if (high.isHighSurrogate() && index + 1 < length && this[index + 1].isLowSurrogate()) 2 else 1
}

private fun String.codePointAt(index: Int): Int {
    val high = this[index]
    if (high.isHighSurrogate() && index + 1 < length) {
        val low = this[index + 1]
        if (low.isLowSurrogate()) {
            return ((high.code - 0xD800) shl 10) + (low.code - 0xDC00) + 0x10000
        }
    }
    return high.code
}

private fun isVariationSelector(codePoint: Int) = codePoint == 0xFE0E || codePoint == 0xFE0F
private fun isZeroWidthJoiner(codePoint: Int) = codePoint == 0x200D
private fun isEnclosingKeycap(codePoint: Int) = codePoint == 0x20E3
private fun isSkinToneModifier(codePoint: Int) = codePoint in 0x1F3FB..0x1F3FF
private fun isRegionalIndicator(codePoint: Int) = codePoint in 0x1F1E6..0x1F1FF

private fun isCombiningMark(codePoint: Int): Boolean =
    codePoint in 0x0300..0x036F ||
        codePoint in 0x1AB0..0x1AFF ||
        codePoint in 0x1DC0..0x1DFF ||
        codePoint in 0x20D0..0x20FF ||
        codePoint in 0xFE20..0xFE2F