package dev.panuszewski.components

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.theme.palette

@Composable
fun Transition<Int>.Verdict(
    since: Int,
    modifier: Modifier = Modifier,
    icon: String = "✅",
    accent: Color? = null,
    content: @Composable AnnotatedString.Builder.() -> Unit,
) {
    val resolvedAccent = accent ?: palette.green

    SlideFromBottomAnimatedVisibility({ it >= since }) {
        Box(
            modifier = modifier
                .border(2.dp, resolvedAccent)
                .background(resolvedAccent.copy(alpha = 0.06f))
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            h6 {
                Text {
                    append("$icon  ")
                    content()
                }
            }
        }
    }
}
