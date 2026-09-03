package dev.panuszewski.template.components

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import dev.panuszewski.template.theme.withColor

@Composable
fun JustName(node: TreeElement<String>): AnnotatedString =
    buildAnnotatedString { withColor(MaterialTheme.colors.onBackground) { append(node.value) } }