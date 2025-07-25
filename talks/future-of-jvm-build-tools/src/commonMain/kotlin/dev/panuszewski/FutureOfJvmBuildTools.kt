@file:Suppress("FunctionName")

package dev.panuszewski

import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.template.DECORATOR

fun KotlinNewFeatures(decorator: SceneDecorator = DECORATOR): Storyboard =
    Storyboard.build(
        title = "Basic Storyboard",
        format = SceneFormat.Default,
        decorator = decorator,
    ) {
        // TODO
    }

