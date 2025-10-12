package dev.panuszewski.template.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable.PlacementScope
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.animateTextStyle
import dev.panuszewski.template.extensions.annotate

@Composable
fun SceneScope<Int>.SlidingTitleScaffold(
    title: String,
    slideToTopAt: Int = 1,
    slideBackAt: Int = Int.MAX_VALUE,
    content: @Composable BoxScope.() -> Unit
) {
    SlidingTitleScaffold(title.annotate(), slideToTopAt, slideBackAt, content)
}

@Composable
fun SceneScope<Int>.SlidingTitleScaffold(
    title: AnnotatedString,
    slideToTopAt: Int = 1,
    slideBackAt: Int = Int.MAX_VALUE,
    content: @Composable BoxScope.() -> Unit
) {
    val isLargeTitle = transition.createChildTransition {
        it.toState() !in slideToTopAt until slideBackAt
    }
    val contentVisible = transition.createChildTransition {
        it.toState() in slideToTopAt + 1 until slideBackAt - 1
    }
    val durationMillis = 500

    val titleTextStyle by isLargeTitle.animateTextStyle(
        targetValueByState = { isLarge ->
            if (isLarge) MaterialTheme.typography.h3 else MaterialTheme.typography.h4
        },
        transitionSpec = { tween(durationMillis) }
    )
    val titleVerticalOffset by isLargeTitle.animateDp(
        targetValueByState = { isLarge -> if (isLarge) 0.dp else -235.dp },
        transitionSpec = { tween(durationMillis) }
    )

    SubcomposeLayout { constraints ->
        context(constraints) {

            val titleHeight = subcompose("TitleText") {
                ProvideTextStyle(titleTextStyle) {
                    Text(title)
                }
            }.measureHeight()

            layout(constraints.maxWidth, constraints.maxHeight) {
                subcompose("Title") {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ProvideTextStyle(titleTextStyle) {
                            AnimatedTitle(
                                title = title,
                                modifier = Modifier.offset(y = titleVerticalOffset)
                            )
                        }
                    }
                }.measureAndPlace()

                subcompose("Body") {
                    contentVisible.SlideFromBottomAnimatedVisibility {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 32.dp,
                                    end = 32.dp,
                                    top = titleHeight.toDp() + 48.dp,
                                    bottom = 32.dp,
                                ),
                        ) {
                            content()
                        }
                    }
                }.measureAndPlace()
            }
        }
    }
}

context(constraints: Constraints)
private fun List<Measurable>.measureHeight() =
    map { it.measure(constraints) }.maxOfOrNull { it.height } ?: 0

context(placeableScope: PlacementScope, constraints: Constraints)
private fun List<Measurable>.measureAndPlace() =
    with(placeableScope) {
        map { it.measure(constraints).place(0, 0) }
    }