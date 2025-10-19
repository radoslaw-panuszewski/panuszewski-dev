package dev.panuszewski.template.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.body1
import dev.panuszewski.template.theme.LocalIdeColors

@Composable
fun RoundedTreeNode(
    node: TreeElement<String>,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    content: @Composable (String) -> AnnotatedString = { it.annotate() }
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(width = 2.dp, color = node.color ?: Color.Unspecified, shape = RoundedCornerShape(8.dp))
            .background(LocalIdeColors.current.paneBackground)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.animateContentSize(tween(durationMillis = 300, delayMillis = 300))
        ) {
            ProvideTextStyle(textStyle) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = content.invoke(node.value)
                )
            }
        }
    }
}