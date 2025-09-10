package dev.panuszewski.template

import androidx.compose.material.Colors
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.drawAxes(axisLength: Float, textMeasurer: TextMeasurer, colors: Colors, xLabel: String, yLabel: String) {
    val xAxisY = size.height - 40.dp.toPx()
    val yAxisX = 40.dp.toPx()
    val strokeWidth = 2.dp.toPx()
    val arrowSize = 10.dp.toPx()

    val endY = xAxisY - (xAxisY * axisLength)
    drawLine(
        color = colors.onBackground,
        start = Offset(yAxisX, xAxisY),
        end = Offset(yAxisX, endY),
        strokeWidth = strokeWidth
    )

    if (axisLength > 0.05f) {
        drawArrowhead(
            start = Offset(yAxisX, endY),
            end = Offset(yAxisX, 0f),
            size = arrowSize,
            color = colors.onBackground,
            strokeWidth = strokeWidth
        )
    }

    val endX = yAxisX + ((size.width - yAxisX) * axisLength)
    drawLine(
        color = colors.onBackground,
        start = Offset(yAxisX, xAxisY),
        end = Offset(endX, xAxisY),
        strokeWidth = strokeWidth
    )

    if (axisLength > 0.05f) {
        drawArrowhead(
            start = Offset(endX, xAxisY),
            end = Offset(size.width, xAxisY),
            size = arrowSize,
            color = colors.onBackground,
            strokeWidth = strokeWidth
        )

        drawText(
            textMeasurer = textMeasurer,
            text = xLabel,
            style = TextStyle(color = colors.onBackground),
            topLeft = Offset(axisLength * (size.width - 80.dp.toPx()), xAxisY + 10.dp.toPx())
        )

        rotate(degrees = -90f) {
            drawText(
                textMeasurer = textMeasurer,
                text = yLabel,
                style = TextStyle(color = colors.onBackground),
                topLeft = Offset(axisLength * (center.x + 150.dp.toPx()), center.y - 470.dp.toPx()),
            )
        }
    }
}

private fun DrawScope.drawArrowhead(
    start: Offset,
    end: Offset,
    size: Float,
    color: Color,
    strokeWidth: Float
) {
    val angle = atan2(y = end.y - start.y, x = end.x - start.x)
    val halfArrowAngle = 30.0 * (PI / 180.0)

    val path = Path().apply {
        moveTo(start.x, start.y)
        lineTo(
            x = start.x - size * cos(angle - halfArrowAngle).toFloat(),
            y = start.y - size * sin(angle - halfArrowAngle).toFloat()
        )
        moveTo(start.x, start.y)
        lineTo(
            x = start.x - size * cos(angle + halfArrowAngle).toFloat(),
            y = start.y - size * sin(angle + halfArrowAngle).toFloat()
        )
    }

    drawPath(path, color = color, style = Stroke(width = strokeWidth))
}