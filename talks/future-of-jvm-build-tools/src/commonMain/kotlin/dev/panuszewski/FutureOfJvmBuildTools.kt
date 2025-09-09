package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.Agenda
import dev.panuszewski.scenes.FirstSlide
import dev.panuszewski.scenes.LastSlide
import dev.panuszewski.scenes.Maven
import dev.panuszewski.scenes.MavenTransitionOnChart
import dev.panuszewski.scenes.Overview
import dev.panuszewski.scenes.Title
import dev.panuszewski.scenes.WrappingUp
import dev.panuszewski.scenes.gradle.Gradle
import dev.panuszewski.scenes.gradle.GradleTransitionOnChart
import dev.panuszewski.scenes.amper.Amper
import dev.panuszewski.scenes.amper.AmperTransitionOnChart
import dev.panuszewski.template.LIGHT_THEME
import dev.panuszewski.template.SectionTitle

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
//    FirstSlide()

    section("The Future of JVM Build Tools") {
        Title(animateToPresent = false)
        Agenda()
        Title(animateToPresent = true)
        Overview()
    }

    section("Maven") {
        SectionTitle(animateToHeader = true)
        Maven()
        MavenTransitionOnChart()
    }

    section("Gradle") {
        SectionTitle(animateToHeader = true)
        Gradle()
        GradleTransitionOnChart()
    }

    section("Amper") {
        SectionTitle(animateToHeader = true)
        Amper()
        AmperTransitionOnChart()
    }

    section("Wrapping up!") {
        SectionTitle(animateToHeader = true)
        WrappingUp()
    }

    section("Thank You!") {
        SectionTitle()
    }

    LastSlide()
}

