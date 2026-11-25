package dev.panuszewski.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.extensions.SlideFromLeftAnimatedVisibility
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.theme.BULLET_1
import dev.panuszewski.template.theme.palette

val AGENDA_SECTIONS = listOf(
    "Context",
    "Requirements",
    "Design choices",
    "API design",
    "Implementation",
)

@Composable
fun Transition<Int>.Agenda(
    modifier: Modifier = Modifier,
    spacing: Dp = 16.dp,
    configure: AgendaScope.() -> Unit
) {
    val scope = AgendaScope()
    scope.configure()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            scope.items.forEach { item ->
                SlideFromLeftAnimatedVisibility({ it >= item.appearsSince }) {
                    if (item.crossedOutSince != null || item.accent != null) {
                        createChildTransition { state -> item.render(state) }.MagicAnnotatedString()
                    } else {
                        Text("$BULLET_1 ${item.text}")
                    }
                }
            }
        }
    }
}

@Composable
private fun AgendaItem.render(state: Int) =
    when {
        crossedOutSince != null && state >= crossedOutSince -> buildAnnotatedString {
            append(BULLET_1)
            withStyle(SpanStyle(textDecoration = LineThrough, color = palette.gray)) {
                append(text)
            }
        }

        accent != null -> buildAnnotatedString {
            append(BULLET_1)
            withStyle(SpanStyle(color = accent, fontWeight = FontWeight.Medium)) {
                append(text)
            }
        }

        else -> "$BULLET_1 $text".annotate()
    }

class AgendaScope {
    val items = mutableListOf<AgendaItem>()

    fun item(text: String, appearsSince: Int = 0, crossedOutSince: Int? = null, accent: Color? = null) {
        items.add(AgendaItem(text, appearsSince, crossedOutSince, accent))
    }
}

data class AgendaItem(
    val text: String,
    val appearsSince: Int = 0,
    val crossedOutSince: Int? = null,
    val accent: Color? = null,
)
