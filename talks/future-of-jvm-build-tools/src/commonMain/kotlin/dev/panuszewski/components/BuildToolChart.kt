package dev.panuszewski.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.rememberTextMeasurer
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.drawAxes

@Composable
fun SceneScope<Int>.BuildToolChart(
    itemsVisibleSince: Int? = null,
    drawAxesSince: Int,
    moveItemsToTargetSince: Int? = null,
    content: BuildToolChartContent,
) {
    val axisLength by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        targetValueByState = { if (it.toState() >= drawAxesSince) 0.99f else 0f }
    )

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .drawBehind {
                drawAxes(
                    axisLength = axisLength,
                    textMeasurer = textMeasurer,
                    xLabel = "Toolability",
                    yLabel = "Extensibility"
                )
            }
    ) {
        val chartContext = BuildToolChartContext(
            itemsVisibleSinceState = itemsVisibleSince,
            moveItemsToTargetSinceState = moveItemsToTargetSince
        )
        context(chartContext) { content.Content() }
    }
}

fun interface BuildToolChartContent {
    @Composable
    context(sceneScope: SceneScope<Int>, boxScope: BoxScope, chartScope: BuildToolChartContext)
    fun Content()
}

data class BuildToolChartContext(
    val itemsVisibleSinceState: Int?,
    val moveItemsToTargetSinceState: Int?,
)
