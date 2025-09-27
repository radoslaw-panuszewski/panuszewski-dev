package dev.panuszewski.template.components.spans

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.ResolvedTextDirection.Ltr

abstract class ExtendedSpanPainter {
    protected fun TextLayoutResult.getBoundingBoxes(
        startOffset: Int,
        endOffset: Int,
        flattenForFullParagraphs: Boolean = false,
    ): List<Rect> {
        if (startOffset == endOffset) {
            return emptyList()
        }

        val startLineNum = getLineForOffset(startOffset)
        val endLineNum = getLineForOffset(endOffset)

        if (flattenForFullParagraphs) {
            val isFullParagraph = (startLineNum != endLineNum)
                    && getLineStart(startLineNum) == startOffset
                    && multiParagraph.getLineEnd(endLineNum, visibleEnd = true) == endOffset

            if (isFullParagraph) {
                return listOf(
                    Rect(
                        top = getLineTop(startLineNum),
                        bottom = getLineBottom(endLineNum),
                        left = 0f,
                        right = size.width.toFloat()
                    )
                )
            }
        }

        val isLtr = multiParagraph.getParagraphDirection(offset = layoutInput.text.lastIndex) == Ltr

        return fastMapRange(startLineNum, endLineNum) { lineNum ->
            Rect(
                top = getLineTop(lineNum),
                bottom = getLineBottom(lineNum),
                left = if (lineNum == startLineNum) {
                    getHorizontalPosition(startOffset, usePrimaryDirection = isLtr)
                } else {
                    getLineLeft(lineNum)
                },
                right = if (lineNum == endLineNum) {
                    getHorizontalPosition(endOffset, usePrimaryDirection = isLtr)
                } else {
                    getLineRight(lineNum)
                }
            )
        }
    }
}

fun interface SpanDrawInstructions {
    fun DrawScope.draw()
}