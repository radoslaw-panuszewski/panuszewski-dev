package dev.panuszewski.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import dev.panuszewski.template.components.TreeElement
import dev.panuszewski.template.theme.withColor

@Composable
fun JustName(node: TreeElement<String>): AnnotatedString =
    buildAnnotatedString { withColor(Color.White) { append(node.value) } }