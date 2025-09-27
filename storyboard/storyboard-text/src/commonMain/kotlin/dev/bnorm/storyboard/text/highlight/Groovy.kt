package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.buildAnnotatedString

fun highlightGroovy(text: String, codeStyle: CodeStyle) = buildAnnotatedString {
    val stringRanges = mutableSetOf<IntRange>()

    SINGLE_QUOTE_STRING_REGEX.forEachOccurrence(text) { range ->
        addStyle(codeStyle.string, range)
        stringRanges.add(range)
    }
    DOUBLE_QUOTE_STRING_REGEX.forEachOccurrence(text) { range ->
        addStyle(codeStyle.string, range)
        stringRanges.add(range)
    }

    NUMBER_REGEX.findAll(text).forEach { match ->
        val range = match.groups[1]?.rangeCompat
        if (range != null) {
            val isInsideString = stringRanges.any { stringRange ->
                range.first >= stringRange.first && range.last <= stringRange.last
            }
            if (!isInsideString) {
                addStyle(codeStyle.number, range)
            }
        }
    }
}

private val SINGLE_QUOTE_STRING_REGEX = """('.*?')""".toRegex()
private val DOUBLE_QUOTE_STRING_REGEX = """(".*?")""".toRegex()
private val NUMBER_REGEX = """\b(\d+(?:\.\d+)?)\b""".toRegex()