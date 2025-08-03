package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.Maven
import dev.panuszewski.scenes.Overview
import dev.panuszewski.scenes.Title
import dev.panuszewski.template.DARK_THEME
import dev.panuszewski.template.SectionTitle

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = DARK_THEME,
) {
    beginning()
    maven()
}

private fun StoryboardBuilder.beginning() {
    section("The Future of JVM Build Tools") {
        Title()
        Overview()
    }
}

private fun StoryboardBuilder.maven() {
    section("Maven") {
        SectionTitle(animateToHeader = true)
        Maven()
    }
}
