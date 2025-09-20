package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.template.section
import dev.panuszewski.template.theme.LIGHT_THEME
import dev.panuszewski.template.components.SectionTitle

val KeepingYourBuildClean = Storyboard.build(
    title = "Keeping Your Build Clean",
    format = SceneFormat.Default,
    decorator = LIGHT_THEME,
) {
    section("Keeping Your Build Clean") {
        SectionTitle()
    }
}

