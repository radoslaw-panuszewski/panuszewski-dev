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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun Connection(
    parentRect: Rect,
    childRect: Rect,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    stroke: Stroke = with(LocalDensity.current) { Stroke(width = 2.dp.toPx()) },
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
    }
}