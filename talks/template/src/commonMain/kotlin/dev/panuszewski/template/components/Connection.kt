package dev.panuszewski.template.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Describes whether (and in which direction) an arrowhead should be drawn on a [Connection]
 * between a parent and a child node.
 */
enum class ArrowDirection {
    /** No arrowhead is drawn. */
    NONE,

    /** An arrowhead is drawn at the child end, pointing from the parent towards the child. */
    FORWARD,

    /** An arrowhead is drawn at the parent end, pointing from the child towards the parent. */
    BACKWARD,

    /** Arrowheads are drawn at both ends. */
    BOTH,
}

@Composable
fun Connection(
    parentRect: Rect,
    childRect: Rect,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    stroke: Stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx()) },
    arrow: ArrowDirection = ArrowDirection.NONE,
) {
    Canvas(modifier.fillMaxSize()) {
        val startX = when (layoutDirection) {
            LayoutDirection.Ltr -> parentRect.right
            LayoutDirection.Rtl -> parentRect.left
        }
        val startY = parentRect.top + (parentRect.bottom - parentRect.top) / 2f

        val endX = when (layoutDirection) {
            LayoutDirection.Ltr -> childRect.left
            LayoutDirection.Rtl -> childRect.right
        }
        val endY = childRect.top + (childRect.bottom - childRect.top) / 2f

        val middleX = startX + (endX - startX) / 2f

        val path = Path()
        path.moveTo(startX, startY)
        path.cubicTo(
            x1 = middleX, y1 = startY,
            x2 = middleX, y2 = endY,
            x3 = endX, y3 = endY,
        )
        drawPath(path, color, style = stroke)

        // The cubic curve above always leaves/arrives horizontally at both ends
        // (the control points share the Y coordinate with their respective endpoint),
        // so the tangent direction at either end is simply the sign of (endX - startX).
        val travelDirection = if (endX >= startX) 1f else -1f
        val arrowLength = stroke.width * 4f
        val arrowHalfWidth = stroke.width * 2.5f

        if (arrow == ArrowDirection.FORWARD || arrow == ArrowDirection.BOTH) {
            drawArrowHead(
                tip = Offset(endX, endY),
                direction = Offset(travelDirection, 0f),
                length = arrowLength,
                halfWidth = arrowHalfWidth,
                color = color,
            )
        }

        if (arrow == ArrowDirection.BACKWARD || arrow == ArrowDirection.BOTH) {
            drawArrowHead(
                tip = Offset(startX, startY),
                direction = Offset(-travelDirection, 0f),
                length = arrowLength,
                halfWidth = arrowHalfWidth,
                color = color,
            )
        }
    }
}

private fun DrawScope.drawArrowHead(
    tip: Offset,
    direction: Offset,
    length: Float,
    halfWidth: Float,
    color: Color,
) {
    val backX = tip.x - direction.x * length
    val backY = tip.y - direction.y * length

    // Perpendicular to `direction`, scaled to half the arrowhead width.
    val perpX = -direction.y * halfWidth
    val perpY = direction.x * halfWidth

    val path = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(backX + perpX, backY + perpY)
        lineTo(backX - perpX, backY - perpY)
        close()
    }
    drawPath(path, color)
}