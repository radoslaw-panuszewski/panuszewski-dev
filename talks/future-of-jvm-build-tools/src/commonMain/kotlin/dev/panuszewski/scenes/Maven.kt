package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.HeaderScaffold

fun StoryboardBuilder.Maven() = scene(
    stateCount = 1
) {
    HeaderScaffold {

    }
}

