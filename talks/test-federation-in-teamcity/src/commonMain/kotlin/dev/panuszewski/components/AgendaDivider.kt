package dev.panuszewski.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.extensions.h5
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.AgendaDivider(currentSection: Int) {
    scene(
        stateCount = 1,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        val accent = palette.orange

        withIntTransition {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                h5 {
                    Agenda {
                        AGENDA_SECTIONS.forEachIndexed { index, section ->
                            val sectionNumber = index + 1
                            item(
                                text = section,
                                crossedOutSince = if (sectionNumber < currentSection) 0 else null,
                                accent = if (sectionNumber == currentSection) accent else null,
                            )
                        }
                    }
                }
            }
        }
    }
}
