package dev.panuszewski.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.components.CodeSample
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.theme.BULLET_1

@Composable
fun Transition<Int>.Agenda(
    configure: AgendaScope.() -> Unit
) {
    val scope = AgendaScope()
    scope.configure()
    
    Box(
        modifier = Modifier.padding(top = 32.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            h6 {
                scope.items.forEach { item ->
                    if (item.crossedOutSince != null) {
                        createChildTransition {
                            when {
                                it >= item.crossedOutSince -> buildAnnotatedString {
                                    append(BULLET_1)
                                    withStyle(SpanStyle(textDecoration = LineThrough, color = Color.DarkGray)) { 
                                        append(item.text) 
                                    }
                                }
                                else -> "$BULLET_1 ${item.text}".annotate()
                            }
                        }.MagicAnnotatedString()
                    } else {
                        Text("$BULLET_1 ${item.text}")
                    }
                }
            }
        }
    }
}

class AgendaScope {
    val items = mutableListOf<AgendaItem>()

    fun item(text: String, crossedOutSince: Int? = null) {
        items.add(AgendaItem(text, crossedOutSince))
    }
}

data class AgendaItem(
    val text: String,
    val crossedOutSince: Int?
)
