package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.Chaos
import dev.panuszewski.scenes.CrossConfiguration
import dev.panuszewski.scenes.DeclarativeGradle
import dev.panuszewski.scenes.Groovy
import dev.panuszewski.scenes.ImperativeCode
import dev.panuszewski.scenes.IntroductionSlide
import dev.panuszewski.scenes.KnowYourEnemy
import dev.panuszewski.scenes.MakingChangesToConventions
import dev.panuszewski.scenes.NoTypeSafety
import dev.panuszewski.scenes.TitleScene
import dev.panuszewski.scenes.TwoTypesOfGradleUsers
import dev.panuszewski.scenes.Ultramarines
import dev.panuszewski.template.theme.DARK_THEME

private const val TITLE = "Purging the Chaos from Your Gradle Build"

// TODO slajd na przedstawienie sie
// TODO pożegnalny slajd
// TODO terminal pod github issuesem
// TODO podsumowanie?
// TODO light theme?

/**
 * Opowiedzieć więcej o plugins { ... } block że jest kompilowany wcześniej
 * Powiedzieć o dependencyResolutionManagement że jest już długo incubating
 */
val PurgingChaosFromYourGradleBuild = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = DARK_THEME,
) {
    TitleScene()
    Chaos()
    Ultramarines()
    IntroductionSlide()
    KnowYourEnemy()
    Groovy()
    NoTypeSafety()
    ImperativeCode()
    CrossConfiguration()
    MakingChangesToConventions()
    TwoTypesOfGradleUsers()
    DeclarativeGradle()
}
