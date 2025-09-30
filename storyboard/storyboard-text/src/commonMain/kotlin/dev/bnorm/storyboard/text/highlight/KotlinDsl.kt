package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.buildAnnotatedString

fun highlightKotlinDsl(text: String, codeStyle: CodeStyle) = buildAnnotatedString {
    append(text)

    val stringRanges = mutableSetOf<IntRange>()
    val highlightedRanges = mutableSetOf<IntRange>()

    DOUBLE_QUOTE_STRING_REGEX.forEachOccurrence(text) { range ->
        addStyle(codeStyle.string, range)
        stringRanges.add(range)
        highlightedRanges.add(range)
    }

    KEYWORD_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges) && !overlapsAny(range, highlightedRanges)) {
            addStyle(codeStyle.property, range)
            highlightedRanges.add(range)
        }
    }

    FUNCTION_CALL_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges) && !overlapsAny(range, highlightedRanges)) {
            addStyle(codeStyle.functionDeclaration, range)
            highlightedRanges.add(range)
        }
    }
}

private fun isInsideAnyRange(range: IntRange, ranges: Set<IntRange>): Boolean {
    return ranges.any { it.contains(range.first) || it.contains(range.last) }
}

private fun overlapsAny(range: IntRange, ranges: Set<IntRange>): Boolean {
    return ranges.any { existing ->
        range.first in existing || range.last in existing ||
        existing.first in range || existing.last in range
    }
}

private val DOUBLE_QUOTE_STRING_REGEX = """(".*?")""".toRegex()

private val KEYWORD_REGEX = """\b(buildscript|allprojects|subprojects|dependencies|tasks)\b""".toRegex()

private val FUNCTION_CALL_REGEX = """\b(classpath|apply|filter|forEach|endsWith|implementation|project|register)(?=\s*\()""".toRegex()
