package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.ImperativeCode() {
    scene(100) {
        withStateTransition {
            TitleScaffold("Imperative code") {
                IdeLayout {

                }
            }
        }
    }
}