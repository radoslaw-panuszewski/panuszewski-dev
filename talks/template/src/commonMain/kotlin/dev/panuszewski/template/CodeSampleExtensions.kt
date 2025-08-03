package dev.panuszewski.template

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.TextTagScope
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun TextTagScope.tag() = tag("")

fun CodeSample.startWith(transformer: CodeSample.() -> CodeSample) = transformer()

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

fun String.toCode(
    codeStyle: CodeStyle = INTELLIJ_DARK_CODE_STYLE,
    scope: CodeScope = CodeScope.File,
    identifierType: (CodeStyle, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    return highlight(
        codeStyle = codeStyle,
        language = Language.Kotlin,
        scope = scope,
        identifierStyle = { identifierType(codeStyle, it) },
    )
}
