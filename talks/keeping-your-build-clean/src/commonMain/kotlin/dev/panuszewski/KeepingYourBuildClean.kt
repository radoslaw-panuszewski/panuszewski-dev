package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.template.components.BigTitle
import dev.panuszewski.template.components.TitleWithAgenda
import dev.panuszewski.template.theme.LIGHT_THEME

private const val TITLE = "Keeping Your Build Clean"

val KeepingYourBuildClean = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = LIGHT_THEME,
) {
    TitleWithAgenda(
        title = TITLE,
        agenda = listOf(
            "First agenda item",
            "Second agenda item",
            "Third agenda item",
        )
    )
    BigTitle("Why would you care?")
}

