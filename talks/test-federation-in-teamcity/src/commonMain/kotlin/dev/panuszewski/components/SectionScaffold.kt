package dev.panuszewski.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.h5

/**
 * Section scaffold with a static title pinned to the top.
 *
 * Deliberately does *not* use `SlidingTitleScaffold`/`AnimatedTitle`: those animate the title from
 * the centre of the screen up to the top and only swap the text once the slide has finished, which
 * reads as "old title slides, then pops into the new one".
 *
 * Instead the title is a plain, statically positioned element, so pairing this scaffold with a
 * fading scene transition makes consecutive titles cross-fade into each other, while the body
 * content slides in and out on its own (keyed on the scene frame). Net effect: content slides,
 * title cross-fades.
 *
 * Because there is no slide-to-top phase, content is live from state 0 — scenes using this scaffold
 * must not reserve lead-in states for the title.
 */
@Composable
fun SceneScope<Int>.SectionScaffold(
    title: String,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(24.dp))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            h5 { Text(title) }
        }

        Spacer(Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            transition.SlideFromBottomAnimatedVisibility({ it is Frame.State }) {
                Box(contentAlignment = Alignment.TopCenter) {
                    content()
                }
            }
        }
    }
}
