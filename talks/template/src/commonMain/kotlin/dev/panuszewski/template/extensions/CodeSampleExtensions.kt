package dev.panuszewski.template.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.TextTagScope
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.panuszewski.template.components.CodeSample
import dev.panuszewski.template.components.CodeSamplesBuilder
import dev.panuszewski.template.theme.INTELLIJ_DARK_CODE_STYLE
import dev.panuszewski.template.theme.LocalCodeStyle
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.TagType.NORMAL

enum class TagType { NORMAL, WARNING }

fun TextTagScope.tag(tagType: TagType? = NORMAL) = tag("", tagType)

fun CodeSample.startWith(transformer: CodeSample.() -> CodeSample) = listOf(transformer())

fun AnnotatedString.toCode(
    language: Language = Language.Kotlin,
    codeStyle: CodeStyle = INTELLIJ_DARK_CODE_STYLE,
    scope: CodeScope = CodeScope.File,
    identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val styled = text.highlight(
        codeStyle = codeStyle,
        language = language,
        scope = scope,
        identifierStyle = { identifierType(codeStyle, it) }
    )
    return buildAnnotatedString {
        append(this@toCode)
        for (range in styled.spanStyles) {
            addStyle(range.item, range.start, range.end)
        }
    }
}

@Composable
fun String.toCode(
    language: Language = Language.Kotlin,
    scope: CodeScope = CodeScope.File,
    identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val codeStyle = LocalCodeStyle.current

    return highlight(
        language = language,
        codeStyle = codeStyle,
        scope = scope,
        identifierStyle = { identifierType(codeStyle, it) },
    )
}

fun CodeSample.fold(foldable: Foldable): CodeSample = foldable.fold()
fun CodeSample.expand(foldable: Foldable): CodeSample = foldable.expand()
fun CodeSample.expandAndFocus(foldable: Foldable, scroll: Boolean = false): CodeSample = foldable.expandAndFocus(scroll)

class Foldable(
    private val folded: List<TextTag>, private val expanded: List<TextTag>
) {
    constructor(folded: TextTag, expanded: TextTag) : this(listOf(folded), listOf(expanded))

    context(codeSample: CodeSample) fun fold(): CodeSample = codeSample.reveal(folded).hide(expanded)

    context(codeSample: CodeSample) fun expand(): CodeSample = codeSample.reveal(expanded).hide(folded)

    context(codeSample: CodeSample) fun expandAndFocus(scroll: Boolean = false) = codeSample.reveal(expanded).hide(folded).focus(expanded, scroll)
}

@Composable
fun buildAndRememberCodeSamples(builder: CodeSamplesBuilder.() -> List<CodeSample>): List<CodeSample> {
    val codeSamples = remember { buildCodeSamples(builder) }
    codeSamples.precompose()
    return codeSamples
}

@Composable
fun List<CodeSample>.precompose() {
    for (sample in this) {
        sample.String()
    }
}