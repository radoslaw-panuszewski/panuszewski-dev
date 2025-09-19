package dev.panuszewski.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import dev.panuszewski.components.Hat.BASEBALL_CAP
import dev.panuszewski.components.Hat.TOP_HAT
import dev.panuszewski.template.h1
import dev.panuszewski.template.h6

@Composable
fun GuyChangingHats(modifier: Modifier = Modifier, name: String?, hat: Hat) {
    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            h1 { Text("😁", Modifier.offset(y = 50.dp)) }

            AnimatedContent(
                targetState = hat,
                contentAlignment = Alignment.Center,
                transitionSpec = { slideInVertically() togetherWith slideOutVertically() }
            ) { state ->
                h1 {
                    when (state) {
                        BASEBALL_CAP -> Text("🧢", Modifier.offset(x = -10.dp, y = 10.dp))
                        TOP_HAT -> Text("🎩", Modifier.offset(x = -2.dp, y = -10.dp))
                    }
                }
            }
        }

        if (name != null) {
            h6 { Text(name) }
        }
    }
}

enum class Hat { BASEBALL_CAP, TOP_HAT }

