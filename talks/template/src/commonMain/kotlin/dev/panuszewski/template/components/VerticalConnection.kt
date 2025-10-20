package dev.panuszewski.template.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun VerticalConnection(
    parentRect: Rect,
    childRect: Rect,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    stroke: Stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx()) },
) {
    Canvas(modifier.fillMaxSize()) {
        // Start at the middle of the bottom edge of the parent
        val startX = parentRect.left + (parentRect.right - parentRect.left) / 2f
        val startY = parentRect.bottom

        // End at the middle of the top edge of the child
        val endX = childRect.left + (childRect.right - childRect.left) / 2f
        val endY = childRect.top

        val middleY = startY + (endY - startY) / 2f

        val path = Path()
        path.moveTo(startX, startY)
        path.cubicTo(
            x1 = startX, y1 = middleY,
            x2 = endX, y2 = middleY,
            x3 = endX, y3 = endY,
        )
        drawPath(path, color, style = stroke)
    }
}
