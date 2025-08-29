package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

enum class Language {
    Kotlin,
    Xml,
    Yaml,
    Properties
}

fun String.highlight(
    language: Language,
    codeStyle: CodeStyle,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    return when (language) {
        Language.Kotlin -> highlightKotlin(this, codeStyle, scope, identifierStyle)
        Language.Xml -> highlightXml(this, codeStyle, scope, identifierStyle)
        Language.Yaml -> highlightYaml(this, codeStyle, scope, identifierStyle)
        Language.Properties -> highlightProperties(this, codeStyle, scope, identifierStyle)
    }
}
