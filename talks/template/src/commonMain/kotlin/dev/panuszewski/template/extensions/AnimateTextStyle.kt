package dev.panuszewski.template.extensions

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp

@Composable
fun <S> Transition<S>.animateTextStyle(
    transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Float> = { spring() },
    label: String = "TextStyleAnimation",
    targetValueByState: @Composable (state: S) -> TextStyle,
): State<TextStyle> {
    val fontSize = animateFloat(transitionSpec, "$label:fontSize") {
        targetValueByState(it).fontSize.value
    }
    val lineHeight = animateFloat(transitionSpec, "$label:lineHeight") {
        targetValueByState(it).lineHeight.value
    }
    val letterSpacing = animateFloat(transitionSpec, "$label:letterSpacing") {
        targetValueByState(it).letterSpacing.value
    }
    val fontWeight = animateFloat(transitionSpec, "$label:fontWeight") {
        targetValueByState(it).fontWeight?.weight?.toFloat() ?: FontWeight.Normal.weight.toFloat()
    }
    val shadowOffsetX = animateFloat(transitionSpec, "$label:shadowOffsetX") {
        targetValueByState(it).shadow?.offset?.x ?: 0f
    }
    val shadowOffsetY = animateFloat(transitionSpec, "$label:shadowOffsetY") {
        targetValueByState(it).shadow?.offset?.y ?: 0f
    }
    val shadowBlurRadius = animateFloat(transitionSpec, "$label:shadowBlurRadius") {
        targetValueByState(it).shadow?.blurRadius ?: 0f
    }
    val shadowColorRed = animateFloat(transitionSpec, "$label:shadowColorRed") {
        targetValueByState(it).shadow?.color?.red ?: 0f
    }
    val shadowColorGreen = animateFloat(transitionSpec, "$label:shadowColorGreen") {
        targetValueByState(it).shadow?.color?.green ?: 0f
    }
    val shadowColorBlue = animateFloat(transitionSpec, "$label:shadowColorBlue") {
        targetValueByState(it).shadow?.color?.blue ?: 0f
    }
    val shadowColorAlpha = animateFloat(transitionSpec, "$label:shadowColorAlpha") {
        targetValueByState(it).shadow?.color?.alpha ?: 1f
    }
    val textIndentFirstLine = animateFloat(transitionSpec, "$label:textIndentFirstLine") {
        targetValueByState(it).textIndent?.firstLine?.value ?: 0f
    }
    val textIndentRestLine = animateFloat(transitionSpec, "$label:textIndentRestLine") {
        targetValueByState(it).textIndent?.restLine?.value ?: 0f
    }
    return derivedStateOf {
        val shadow = if (shadowOffsetX.value != 0f || shadowOffsetY.value != 0f || shadowBlurRadius.value != 0f) {
            Shadow(
                offset = Offset(shadowOffsetX.value, shadowOffsetY.value),
                blurRadius = shadowBlurRadius.value,
                color = androidx.compose.ui.graphics.Color(
                    red = shadowColorRed.value,
                    green = shadowColorGreen.value,
                    blue = shadowColorBlue.value,
                    alpha = shadowColorAlpha.value
                )
            )
        } else null

        val textIndent = if (textIndentFirstLine.value != 0f || textIndentRestLine.value != 0f) {
            TextIndent(
                firstLine = textIndentFirstLine.value.sp,
                restLine = textIndentRestLine.value.sp
            )
        } else null

        TextStyle(
            fontSize = fontSize.value.sp,
            lineHeight = lineHeight.value.sp,
            letterSpacing = letterSpacing.value.sp,
            fontWeight = FontWeight(fontWeight.value.toInt()),
            shadow = shadow,
            textIndent = textIndent,
        )
    }
}
