package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.Agenda
import dev.panuszewski.scenes.Maven
import dev.panuszewski.scenes.Overview
import dev.panuszewski.scenes.Title
import dev.panuszewski.template.LIGHT_THEME
import dev.panuszewski.template.SectionTitle

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
//    beginning()
    maven()
}

private fun StoryboardBuilder.beginning() {
    section("The Future of JVM Build Tools") {
        Title(animateToPresent = false)
        Agenda()
        Title(animateToPresent = true)
        Overview()
    }
}

private fun StoryboardBuilder.maven() {
    section("Maven") {
        SectionTitle(animateToHeader = true)
        Maven()
    }
}
