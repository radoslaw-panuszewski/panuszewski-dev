package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

internal fun highlightYaml(
    text: String,
    codeStyle: CodeStyle,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    return buildAnnotatedString {
        // Apply base style to the entire text
        withStyle(codeStyle.simple) { append(text) }

        // Split the text into lines
        val lines = text.split("\n")

        // Track the current position in the text
        var currentPosition = 0

        // Process each line
        for (line in lines) {
            // Skip empty lines and comment lines
            if (line.isBlank() || line.trimStart().startsWith("#")) {
                currentPosition += line.length + 1 // +1 for the newline character
                continue
            }

            // Find the first colon that separates key and value
            val colonIndex = line.indexOf(':')
            if (colonIndex > 0) {
                // Extract the key (trim to handle indentation)
                val key = line.substring(0, colonIndex).trimStart()

                // Handle keys that start with hyphen
                val keyWithoutHyphen = if (key.startsWith("- ")) key.substring(2) else key

                // Calculate the start and end positions of the key in the original text
                val keyStartPosition = currentPosition + line.indexOf(key.first()) +
                        (if (key.startsWith("- ")) 2 else 0)
                val keyEndPosition = keyStartPosition + keyWithoutHyphen.length

                // Apply property style to the key

                addStyle(codeStyle.keyword, keyStartPosition, keyEndPosition)
            }

            // Move to the next line
            currentPosition += line.length + 1 // +1 for the newline character
        }
    }
}

