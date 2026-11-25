package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.AboutMe
import dev.panuszewski.scenes.Chaos
import dev.panuszewski.scenes.CrossConfiguration
import dev.panuszewski.scenes.DeclarativeGradle
import dev.panuszewski.scenes.Groovy
import dev.panuszewski.scenes.ImperativeCode
import dev.panuszewski.scenes.KnowYourEnemy
import dev.panuszewski.scenes.NoTypeSafety
import dev.panuszewski.scenes.WhereToPutTheConventions
import dev.panuszewski.scenes.ThankYouSlide
import dev.panuszewski.scenes.TitleScene
import dev.panuszewski.scenes.TwoTypesOfGradleUsers
import dev.panuszewski.scenes.Ultramarines
import dev.panuszewski.scenes.WrappingUp
import dev.panuszewski.template.theme.TOGGLEABLE_THEME

private const val TITLE = "Purging the Chaos from Your Gradle Build"

val PurgingChaosFromYourGradleBuild = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = TOGGLEABLE_THEME,
) {
    TitleScene()
    Chaos()
    Ultramarines()
    AboutMe()
    KnowYourEnemy()
    Groovy()
    NoTypeSafety()
    ImperativeCode()
    CrossConfiguration()
    WhereToPutTheConventions()
    TwoTypesOfGradleUsers()
    DeclarativeGradle()
    WrappingUp()
    ThankYouSlide()
}
