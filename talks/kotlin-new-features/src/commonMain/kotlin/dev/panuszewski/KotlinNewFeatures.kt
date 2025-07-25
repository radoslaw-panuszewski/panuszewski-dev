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

val KotlinNewFeatures = Storyboard.build(
    title = "What's new after Kotlin 2.0?",
    format = SceneFormat.Default,
    decorator = DECORATOR,
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

