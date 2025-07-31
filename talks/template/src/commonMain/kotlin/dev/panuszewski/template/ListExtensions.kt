package dev.panuszewski.template

import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.toState

//context(sceneScope: SceneScope<Int>)
//fun <T> List<T>.coercedGet(frame: Frame<Int>): T = this[frame.toState().coerceIn(indices)]

fun <T> List<T>.coercedGet(index: Int): T = this[index.coerceIn(indices)]