package dev.panuszewski.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.body1
import dev.panuszewski.template.extensions.body2
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.theme.BULLET_1
import dev.panuszewski.template.theme.withColor

@Composable
fun OptionCard(
    title: String,
    accent: Color,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    OptionCard(title.annotate(), accent, modifier, compact, content)
}

@Composable
fun OptionCard(
    title: AnnotatedString,
    accent: Color,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .border(2.dp, accent)
            .background(accent.copy(alpha = 0.03f))
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        h6 {
            Text(Modifier.align(Alignment.CenterHorizontally)) {
                withColor(accent) { append(title) }
            }
        }

        Spacer(Modifier.height(4.dp))

        if (compact) {
            body2 { content() }
        } else {
            body1 { content() }
        }
    }
}

@Composable
fun CardLine(content: @Composable AnnotatedString.Builder.() -> Unit) {
    Text {
        append(BULLET_1)
        content()
    }
}

/**
 * A fixed-width slot in a row of [OptionCard]s.
 *
 * The slot itself is always present and always claims its share of the row, so every card has its
 * final width from the very first frame — cards that appear later slide into an already-reserved
 * position instead of causing their siblings to shrink.
 */
@Composable
fun RowScope.CardSlot(
    transition: Transition<Int>,
    since: Int,
    content: @Composable () -> Unit,
) {
    Box(Modifier.weight(1f), contentAlignment = Alignment.TopStart) {
        transition.SlideFromBottomAnimatedVisibility({ it >= since }) {
            content()
        }
    }
}
