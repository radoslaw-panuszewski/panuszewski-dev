@file:Suppress("FunctionName")

package dev.panuszewski

import dev.bnorm.kc25.sections.stages.GuardConditions
import dev.bnorm.kc25.sections.stages.Kotlin2_1
import dev.bnorm.kc25.sections.stages.MultiDollarInterpolation
import dev.bnorm.kc25.sections.stages.NonLocalBreakContinue
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.KotlinTimelineStage.KOTLIN_2_1
import dev.panuszewski.scenes.KotlinTimelineStage.KOTLIN_2_2
import dev.panuszewski.scenes.KotlinTimeline
import dev.panuszewski.template.DECORATOR

fun KotlinNewFeatures(decorator: SceneDecorator = DECORATOR): Storyboard =
    Storyboard.build(
        title = "Basic Storyboard",
        format = SceneFormat.Default,
        decorator = decorator,
    ) {
        KotlinTimeline(stageToBeExpanded = KOTLIN_2_1)
        Kotlin2_1()
        GuardConditions()
        Kotlin2_1(startState = 3)
        NonLocalBreakContinue()
        Kotlin2_1(startState = 3)
        MultiDollarInterpolation()
        Kotlin2_1(startState = 3)
        KotlinTimeline(stageToBeExpanded = KOTLIN_2_2)
    }

