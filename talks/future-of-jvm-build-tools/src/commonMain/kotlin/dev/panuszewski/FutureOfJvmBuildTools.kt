package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.MavenTransitionOnChart
import dev.panuszewski.template.LIGHT_THEME

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
//    section("The Future of JVM Build Tools") {
//        Title(animateToPresent = false)
//        Agenda()
//        Title(animateToPresent = true)
//        Overview()
//    }

    section("Maven") {
//        SectionTitle(animateToHeader = true)
//        Maven()
        MavenTransitionOnChart()
    }
}

