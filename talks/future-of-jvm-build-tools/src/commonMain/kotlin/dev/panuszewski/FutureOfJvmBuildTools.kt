package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.SceneSection
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.scenes.Overview
import dev.panuszewski.template.DARK_THEME
import dev.panuszewski.template.LIGHT_THEME
import dev.panuszewski.template.SectionTitle

val FutureOfJvmBuildTools = Storyboard.build(
    title = "The Future of JVM Build Tools",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
    section("The Future of JVM Build Tools") {
        // TODO zrobić animację która słowo Future zamienia na Present
//        SectionTitle()
        Overview()
    }

    section("Maven") {
        SectionTitle()
    }
}

