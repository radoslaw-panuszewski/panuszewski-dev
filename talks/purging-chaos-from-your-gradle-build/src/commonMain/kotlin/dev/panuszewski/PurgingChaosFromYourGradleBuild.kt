package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.DeclarativeGradle
import dev.panuszewski.scenes.TwoTypesOfGradleUsers
import dev.panuszewski.template.theme.DARK_THEME

private const val TITLE = "Purging the Chaos from Your Gradle Build"

val PurgingChaosFromYourGradleBuild = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = DARK_THEME,
) {
//    TitleScene()
//    Chaos()
//    Ultramarines()
//    WhyBother()
//    Groovy()
//    NoTypeSafety()
//    ImperativeCode()
//    CrossConfiguration()
//    BuildSrcVsBuildLogic()
    TwoTypesOfGradleUsers()
//    DeclarativeGradle()
}
