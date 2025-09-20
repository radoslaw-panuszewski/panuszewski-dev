package dev.panuszewski.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.animateTextStyle
import dev.panuszewski.template.extensions.withStateTransition
import dev.panuszewski.template.theme.appendWithSecondaryColor

fun StoryboardBuilder.TitleWithAgenda() {
    val agendaItems = listOf<@Composable () -> Unit>(
        {
            Text {
                append("1. Understanding ")
                appendWithSecondaryColor("build dependencies")
            }
        },
        {
            Text {
                append("2. Identifying and removing ")
                appendWithSecondaryColor("unused dependencies")
            }
        },
        {
            Text {
                append("3. Managing ")
                appendWithSecondaryColor("version conflicts")
            }
        },
        {
            Text {
                append("4. Organizing your ")
                appendWithSecondaryColor("build structure")
            }
        },
        {
            Text {
                append("5. Best practices for ")
                appendWithSecondaryColor("dependency management")
            }
        }
    )

    scene(stateCount = 5) {
        withStateTransition {
            // Create a child transition to track whether we're in the big title states (0 and 4) or smaller title states (1, 2, and 3)
            val isLargeTitle = createChildTransition { it == 0 || it == 4 }

            // Animate text style between h2 (large) and h4 (small)
            val textStyle by isLargeTitle.animateTextStyle(
                targetValueByState = { isLarge ->
                    if (isLarge) MaterialTheme.typography.h2 else MaterialTheme.typography.h4
                }
            )

            // Animate title vertical position - center when large, top when small
            val titleVerticalOffset by isLargeTitle.animateDp(
                targetValueByState = { isLarge ->
                    if (isLarge) 0.dp else -200.dp
                }
            )

            // Use Column layout with top arrangement to avoid conflicts with animated offset
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Add spacer to center the content when title is in center position
                Spacer(modifier = Modifier.weight(1f))
                // Title
                ProvideTextStyle(textStyle) {
                    Text(
                        text = "Keeping Your Build Clean",
                        modifier = Modifier
                            .padding(start = 64.dp)
                            .offset(y = titleVerticalOffset)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("text/Title"),
                                animatedVisibilityScope = contextOf<AnimatedVisibilityScope>(),
                            )
                    )
                }

                // Spacer between title and agenda (32dp when agenda is visible)
                transition.AnimatedVisibility(
                    visible = { it.toState() == 2 },
                    enter = slideInVertically(initialOffsetY = { 0 }),
                    exit = fadeOut()
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Agenda items
                transition.AnimatedVisibility(
                    visible = { it.toState() == 2 },
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut()
                ) {
                    TitleWithAgendaContent(
                        agendaItems = agendaItems
                    )
                }

                // Add spacer to balance the layout and maintain centering
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/**
 * A reusable composable for creating agenda layout.
 *
 * @param agendaItems List of agenda items to display. Each item should contain the text with color styling.
 */
@Composable
fun TitleWithAgendaContent(
    agendaItems: List<@Composable () -> Unit>
) {
    Column(
        Modifier.padding(vertical = 32.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        agendaItems.forEach { item ->
            item()
        }
    }
}