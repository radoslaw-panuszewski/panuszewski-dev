package dev.panuszewski.template.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import org.jetbrains.compose.resources.DrawableResource

/**
 * Builder class for StackedImageGallery DSL
 */
class StackedImageGalleryScope {
    private val images = mutableListOf<DrawableResource>()
    private val states = mutableListOf<Int>()
    private var nextAutoState = 0
    
    /**
     * Add an image to the gallery.
     * @param resource The drawable resource to display
     * @param since The state value when this image should appear. If not specified, uses the next consecutive number.
     */
    fun image(resource: DrawableResource, since: Int? = null) {
        images.add(resource)
        val state = since ?: nextAutoState
        states.add(state)
        nextAutoState = state + 1
    }
    
    internal fun getImages() = images.toList()
    internal fun getStates() = states.toList()
}

/**
 * A composable that displays images in a stacked gallery format.
 * As new images appear, previous images shift to the left and become darker.
 *
 * @param modifier Modifier for the root Box
 * @param imageWidth Width of each image (default: 500.dp)
 * @param imageTopPadding Top padding increment for each subsequent image (default: 16.dp)
 * @param shiftAmount The amount each image shifts to the left per subsequent image (default: 32.dp)
 * @param darkenAmount The darkness increment (alpha) applied per subsequent image (default: 0.33f)
 * @param borderColor Border color for images (default: Color.Gray)
 * @param borderWidth Border width for images (default: 0.5.dp)
 * @param contentScale Content scale for images (default: ContentScale.FillWidth)
 * @param content Lambda with StackedImageGalleryScope receiver where images are defined
 */
@Composable
fun Transition<Int>.StackedImageGallery(
    modifier: Modifier = Modifier,
    imageWidth: Dp = 600.dp,
    imageTopPadding: Dp = 16.dp,
    shiftAmount: Dp = 32.dp,
    darkenAmount: Float = 0.33f,
    borderColor: Color = Color.Gray,
    borderWidth: Dp = 0.5.dp,
    contentScale: ContentScale = ContentScale.FillWidth,
    content: StackedImageGalleryScope.() -> Unit,
) {
    val scope = StackedImageGalleryScope().apply(content)
    val images = scope.getImages()
    val imageStates = scope.getStates()
    
    require(images.isNotEmpty()) {
        "StackedImageGallery must have at least one image"
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize()
    ) {
        images.forEachIndexed { index, imageResource ->
            val currentState = imageStates[index]
            val topPadding = imageTopPadding * index

            SlideFromBottomAnimatedVisibility({ it >= currentState }) {
                // Animate shift based on how many subsequent images are visible
                val shift by animateDp({ tween(300) }) {
                    val visibleCount = imageStates
                        .drop(index + 1)
                        .count { state -> it >= state }
                    shiftAmount * visibleCount
                }

                // Animate darkness based on how many subsequent images are visible
                val tint by animateFloat({ tween(300) }) {
                    val visibleCount = imageStates
                        .drop(index + 1)
                        .count { state -> it >= state }
                    if (visibleCount > 0) {
                        (darkenAmount * visibleCount).coerceAtLeast(0.01f)
                    } else {
                        0.01f
                    }
                }

                ResourceImage(
                    imageResource,
                    modifier = Modifier
                        .padding(top = topPadding, end = shift)
                        .width(imageWidth)
                        .padding(end = shift)
                        .border(borderWidth, borderColor),
                    alignment = Alignment.TopCenter,
                    contentScale = contentScale,
                    colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                )
            }
        }
    }
}