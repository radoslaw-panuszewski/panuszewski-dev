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

    DSL_KEYWORD_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges) && !overlapsAny(range, highlightedRanges)) {
            addStyle(codeStyle.property, range)
            highlightedRanges.add(range)
        }
    }

    KOTLIN_KEYWORD_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges) && !overlapsAny(range, highlightedRanges)) {
            addStyle(codeStyle.keyword, range)
            highlightedRanges.add(range)
        }
    }

    DSL_FUNCTION_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null && !isInsideAnyRange(range, stringRanges) && !overlapsAny(range, highlightedRanges)) {
            addStyle(codeStyle.dsl, range)
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

private val DSL_KEYWORD_REGEX = """\b(explicitApiMode|Strict|buildscript|allprojects|subprojects|dependencies|tasks)\b""".toRegex()

private val KOTLIN_KEYWORD_REGEX = """\b(package|import|class|interface|fun|object|val|var|typealias|constructor|by|companion|init|this|super|typeof|where|if|else|when|try|catch|finally|for|do|while|throw|return|continue|break|as|is|in|true|false|null|get|set|abstract|annotation|actual|const|crossinline|data|enum|expect|external|final|infix|inline|inner|internal|lateinit|noinline|open|operator|out|override|private|protected|public|reified|sealed|suspend|tailrec|vararg|dynamic)\b""".toRegex()

private val DSL_FUNCTION_REGEX = """\b(plugins|explicitApi)(?=\s*(?:<[^>]+>)?\s*[(\{])""".toRegex()

private val FUNCTION_CALL_REGEX = """\b(named|repositories|configure|classpath|apply|filter|forEach|endsWith|implementation|project|register)(?=\s*(?:<[^>]+>)?\s*[(\{])""".toRegex()
