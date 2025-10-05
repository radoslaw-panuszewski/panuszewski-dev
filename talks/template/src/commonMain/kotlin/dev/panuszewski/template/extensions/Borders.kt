package dev.panuszewski.template.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.bottomBorder(width: Dp, color: Color): Modifier {
    return this then Modifier.border(width = width, color = color, shape=getBottomLineShape(width))
}

@Composable
fun Modifier.topBorder(width: Dp, color: Color): Modifier {
    return this then Modifier.border(width = width, color = color, shape=getTopLineShape(width))
}

@Composable
fun Modifier.startBorder(width: Dp, color: Color): Modifier {
    return this then Modifier.border(width = width, color = color, shape=getStartLineShape(width))
}

@Composable
fun Modifier.endBorder(width: Dp, color: Color): Modifier {
    return this then Modifier.border(width = width, color = color, shape=getEndLineShape(width))
}

@Composable
private fun getBottomLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) { lineThicknessDp.toPx() }
    return GenericShape { size, _ ->
        moveTo(0f, size.height)
        lineTo(size.width, size.height)
        lineTo(size.width, size.height - lineThicknessPx)
        lineTo(0f, size.height - lineThicknessPx)
    }
}

@Composable
private fun getTopLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) { lineThicknessDp.toPx() }
    return GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, lineThicknessPx)
        lineTo(0f, lineThicknessPx)
    }
}

@Composable
private fun getStartLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) { lineThicknessDp.toPx() }
    return GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(lineThicknessPx, 0f)
        lineTo(lineThicknessPx, size.height)
        lineTo(0f, size.height)
    }
}

@Composable
private fun getEndLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) { lineThicknessDp.toPx() }
    return GenericShape { size, _ ->
        moveTo(size.width, 0f)
        lineTo(size.width, size.height)
        lineTo(size.width - lineThicknessPx, size.height)
        lineTo(size.width - lineThicknessPx, 0f)
    }
}