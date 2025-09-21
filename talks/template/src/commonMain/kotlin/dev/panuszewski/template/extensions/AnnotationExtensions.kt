package dev.panuszewski.template.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

fun String.annotate(tagStyles: Map<String, SpanStyle> = emptyMap()): AnnotatedString =
    buildAnnotatedString {
        val tagMatches = findTagMatches(this@annotate)
        val tagStack = mutableListOf<TagInfo>()
        var currentIndex = 0
        tagMatches.forEach { tagMatch ->
            appendTextBeforeTag(this@annotate, currentIndex, tagMatch.startIndex)
            currentIndex = processTag(tagMatch, tagStack, tagStyles)
        }
        appendRemainingText(this@annotate, currentIndex)
    }

private fun findTagMatches(input: String): List<TagMatch> {
    val tagRegex = Regex("""\{([^}]+)}""")
    return tagRegex.findAll(input).map { match ->
        TagMatch(
            name = match.groupValues[1],
            startIndex = match.range.first,
            endIndex = match.range.last + 1
        )
    }.toList()
}

private fun AnnotatedString.Builder.appendTextBeforeTag(input: String, currentIndex: Int, tagStart: Int) {
    if (tagStart > currentIndex) {
        append(input.substring(currentIndex, tagStart))
    }
}

private fun AnnotatedString.Builder.processTag(
    tagMatch: TagMatch,
    tagStack: MutableList<TagInfo>,
    tagStyles: Map<String, SpanStyle>
): Int {
    val existingTagIndex = tagStack.indexOfLast { it.name == tagMatch.name }

    if (existingTagIndex >= 0) {
        closeTag(existingTagIndex, tagStack, tagStyles)
    } else {
        openTag(tagMatch.name, tagStack)
    }

    return tagMatch.endIndex
}

private fun AnnotatedString.Builder.closeTag(
    tagIndex: Int,
    tagStack: MutableList<TagInfo>,
    tagStyles: Map<String, SpanStyle>
) {
    val tagInfo = tagStack[tagIndex]
    tagStyles[tagInfo.name]?.let { style ->
        addStyle(style, tagInfo.startPosition, length)
    }
    tagStack.removeAt(tagIndex)
}

private fun AnnotatedString.Builder.openTag(tagName: String, tagStack: MutableList<TagInfo>) {
    tagStack.add(TagInfo(tagName, length))
}

private fun AnnotatedString.Builder.appendRemainingText(input: String, currentIndex: Int) {
    if (currentIndex < input.length) {
        append(input.substring(currentIndex))
    }
}

private data class TagMatch(
    val name: String,
    val startIndex: Int,
    val endIndex: Int
)

private data class TagInfo(
    val name: String,
    val startPosition: Int
)