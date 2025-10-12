package dev.panuszewski.template.extensions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun Transition<Boolean>.AnimatedVisibility(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = { it },
    modifier = modifier,
    enter = enter,
    exit = exit,
    content = content,
)

@Composable
fun <T> Transition<T>.SlideFromLeftAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_LEFT.enter(fraction),
    exit = SlideDirection.FROM_LEFT.exit(fraction),
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromRightAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_RIGHT.enter(fraction),
    exit = SlideDirection.FROM_RIGHT.exit(fraction),
    content = content
)

@Composable
fun Transition<Boolean>.SlideFromRightAnimatedVisibility(
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = SlideFromRightAnimatedVisibility(
    visible = { it },
    modifier = modifier,
    fraction = fraction,
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromTopAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_TOP.enter(fraction),
    exit = SlideDirection.FROM_TOP.exit(fraction),
    content = content
)

@Composable
fun Transition<Boolean>.SlideFromTopAnimatedVisibility(
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = SlideFromTopAnimatedVisibility(
    visible = { it },
    modifier = modifier,
    fraction = fraction,
    content = content
)

@Composable
fun <T> Transition<T>.SlideFromBottomAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = SlideDirection.FROM_BOTTOM.enter(fraction),
    exit = SlideDirection.FROM_BOTTOM.exit(fraction),
    content = content
)

@Composable
fun Transition<Boolean>.SlideFromBottomAnimatedVisibility(
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = SlideFromBottomAnimatedVisibility(
    visible = { it },
    modifier = modifier,
    fraction = fraction,
    content = content
)

@Composable
fun <T> Transition<T>.SlideOutToBottomAnimatedVisibility(
    visible: (T) -> Boolean,
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = EnterTransition.None,
    exit = SlideDirection.FROM_BOTTOM.exit(fraction),
    content = content
)

@Composable
fun <T> Transition<T>.FadeInOutAnimatedVisibility(
    visible: (T) -> Boolean,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    modifier: Modifier = Modifier,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = fadeIn(animationSpec),
    exit = fadeOut(animationSpec),
    content = content
)

@Composable
fun Transition<Boolean>.FadeInOutAnimatedVisibility(
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = FadeInOutAnimatedVisibility(
    visible = { it },
    modifier = modifier,
    content = content
)

@Composable
fun <T> Transition<T>.FadeOutAnimatedVisibility(
    visible: (T) -> Boolean,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    modifier: Modifier = Modifier,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = AnimatedVisibility(
    visible = visible,
    modifier = modifier,
    enter = EnterTransition.None,
    exit = fadeOut(animationSpec),
    content = content
)

@Composable
fun Transition<Boolean>.FadeOutAnimatedVisibility(
    modifier: Modifier = Modifier,
    fraction: Float = 0.5f,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = FadeOutAnimatedVisibility(
    visible = { it },
    modifier = modifier,
    content = content
)

enum class SlideDirection(
    val enter: (Float) -> EnterTransition,
    val exit: (Float) -> ExitTransition,
) {
    FROM_LEFT(
        enter = { fraction -> slideInHorizontally(initialOffsetX = { -(it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutHorizontally(targetOffsetX = { -(it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_RIGHT(
        enter = { fraction -> slideInHorizontally(initialOffsetX = { (it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutHorizontally(targetOffsetX = { (it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_TOP(
        enter = { fraction -> slideInVertically(initialOffsetY = { -(it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutVertically(targetOffsetY = { -(it * fraction).toInt() }) + fadeOut() },
    ),
    FROM_BOTTOM(
        enter = { fraction -> slideInVertically(initialOffsetY = { (it * fraction).toInt() }) + fadeIn() },
        exit = { fraction -> slideOutVertically(targetOffsetY = { (it * fraction).toInt() }) + fadeOut() },
    );

    val combined: (Float) -> ContentTransform get() = { enter(it) togetherWith exit(it) }
}

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