package dev.bnorm.storyboard.text.highlight

fun Regex.forEachOccurrence(text: String, block: (IntRange) -> Unit) {
    findAll(text)
        .map { it.groups[1] }
        .filterNotNull()
        .map { it.rangeCompat }
        .forEach { block(it) }
}