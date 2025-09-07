package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.amper.Amper
import dev.panuszewski.scenes.amper.AmperTransitionOnChart
import dev.panuszewski.template.LIGHT_THEME
import dev.panuszewski.template.SectionTitle

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
    // TODO hook na początku!

//    section("The Future of JVM Build Tools") {
//        Title(animateToPresent = false)
//        Agenda()
//        Title(animateToPresent = true)
//        Overview()
//    }
//
//    section("Maven") {
//        SectionTitle(animateToHeader = true)
//        Maven()
//        MavenTransitionOnChart()
//    }

//    section("Gradle") {
//        SectionTitle(animateToHeader = true)
//        Gradle()
//        GradleTransitionOnChart()
//    }

    section("Amper") {
        SectionTitle(animateToHeader = true)
//        Amper()
        AmperTransitionOnChart()
    }

//    section("Wrapping up!") {
//        SectionTitle(animateToHeader = true)
//        WrappingUp()
//    }

    section("Thank You!") {
        SectionTitle()
    }
}

