package dev.panuszewski.template

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ResourceImage(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = painterResource(resource),
        contentDescription = "",
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}