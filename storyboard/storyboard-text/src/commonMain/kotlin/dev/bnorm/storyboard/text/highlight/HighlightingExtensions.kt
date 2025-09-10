package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token

fun AnnotatedString.Builder.addStyle(style: SpanStyle, ctx: ParserRuleContext) {
    addStyle(style, ctx.start!!.startIndex, ctx.stop!!.stopIndex + 1)
}

fun AnnotatedString.Builder.addStyle(style: SpanStyle, token: Token) {
    addStyle(style, token.startIndex, token.stopIndex + 1)
}

fun AnnotatedString.Builder.addStyle(style: SpanStyle, range: IntRange) {
    addStyle(style, range.first, range.last + 1)
}