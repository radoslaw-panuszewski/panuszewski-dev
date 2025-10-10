package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.buildAnnotatedString

fun highlightToml(text: String, codeStyle: CodeStyle) = buildAnnotatedString {
    append(text)
    
    val stringRanges = mutableSetOf<IntRange>()
    
    STRING_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null) {
            addStyle(codeStyle.string, range)
            stringRanges.add(range)
        }
    }
    
    KEY_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges)) {
            addStyle(codeStyle.keyword, range)
        }
    }
    
    TABLE_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges)) {
            addStyle(codeStyle.keyword, range)
        }
    }
}

private fun isInsideAnyRange(range: IntRange, ranges: Set<IntRange>): Boolean {
    return ranges.any { it.first <= range.first && range.last <= it.last }
}

private val KEY_REGEX = """([a-zA-Z0-9_-]+)\s*=""".toRegex()
private val TABLE_REGEX = """\[([^\]]+)]""".toRegex()
private val STRING_REGEX = """("(?:[^"\\]|\\.)*")""".toRegex()

expect val MatchGroup.rangeCompat: IntRange