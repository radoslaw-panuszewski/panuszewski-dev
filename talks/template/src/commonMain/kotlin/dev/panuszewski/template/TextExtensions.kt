package dev.panuszewski.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable

@Composable
fun h1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h1, content)
}

@Composable
fun h2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h2, content)
}

@Composable
fun h3(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h3, content)
}

@Composable
fun h4(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h4, content)
}

@Composable
fun h5(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h5, content)
}

@Composable
fun h6(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.h6, content)
}

@Composable
fun subtitle1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.subtitle1, content)
}

@Composable
fun subtitle2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.subtitle2, content)
}

@Composable
fun body1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.body1, content)
}

@Composable
fun body2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.body2, content)
}

@Composable
fun button(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.button, content)
}

@Composable
fun caption(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.caption, content)
}

@Composable
fun overline(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.overline, content)
}

@Composable
fun code1(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code1, content)
}

@Composable
fun code2(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code2, content)
}

@Composable
fun code3(content: @Composable () -> Unit) {
    ProvideTextStyle(MaterialTheme.typography.code3, content)
}