package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.BuildSrcVsBuildLogic
import dev.panuszewski.scenes.Chaos
import dev.panuszewski.scenes.CrossConfiguration
import dev.panuszewski.scenes.Groovy
import dev.panuszewski.scenes.ImperativeCode
import dev.panuszewski.scenes.NoTypeSafety
import dev.panuszewski.scenes.TitleScene
import dev.panuszewski.scenes.Ultramarines
import dev.panuszewski.scenes.WhyBother
import dev.panuszewski.template.components.TitleWithAgenda
import dev.panuszewski.template.theme.DARK_THEME

private const val TITLE = "Purging the Chaos from Your Gradle Build"

val KeepingYourBuildClean = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = DARK_THEME,
) {
    TitleScene()
    Chaos()
    Ultramarines()
    WhyBother()
    Groovy()
    NoTypeSafety()
    ImperativeCode()
    CrossConfiguration()
    BuildSrcVsBuildLogic()
}


