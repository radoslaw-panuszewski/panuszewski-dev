package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.buildAnnotatedString

fun highlightHocon(text: String, codeStyle: CodeStyle) = buildAnnotatedString {
    ID_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.keyword, range) }
    ASSIGNMENT_KEY_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.keyword, range) }
    CURLY_BRACES_KEY_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.keyword, range) }
    STRING_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.string, range) }
}

private val ID_REGEX = """(id)""".toRegex()
private val ASSIGNMENT_KEY_REGEX = """(\w+) = .+""".toRegex()
private val CURLY_BRACES_KEY_REGEX = """(\w+)\s*\{.*""".toRegex()
private val STRING_REGEX = """("\S+")""".toRegex()

