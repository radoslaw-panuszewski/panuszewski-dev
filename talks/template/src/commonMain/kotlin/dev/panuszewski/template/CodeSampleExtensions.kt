package dev.panuszewski.template

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.TextTag
import dev.bnorm.storyboard.text.TextTagScope
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun TextTagScope.tag() = tag("")

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
