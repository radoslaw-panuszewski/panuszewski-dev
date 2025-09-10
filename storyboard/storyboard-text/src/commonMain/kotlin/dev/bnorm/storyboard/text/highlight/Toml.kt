package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.buildAnnotatedString

fun highlightToml(text: String, codeStyle: CodeStyle) = buildAnnotatedString {
    KEY_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.keyword, range) }
    TABLE_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.keyword, range) }
    STRING_REGEX.forEachOccurrence(text) { range -> addStyle(codeStyle.string, range) }
}

private val KEY_REGEX = """(.+) = .+""".toRegex()
private val TABLE_REGEX = """\[(.+)]""".toRegex()
private val STRING_REGEX = """(".+")""".toRegex()

expect val MatchGroup.rangeCompat: IntRange