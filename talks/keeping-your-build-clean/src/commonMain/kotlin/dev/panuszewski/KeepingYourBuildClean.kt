package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.scenes.ImperativeCode
import dev.panuszewski.scenes.NoTypeSafety
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.theme.DARK_THEME

private const val TITLE = "Keeping Your Build Clean"

val KeepingYourBuildClean = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = DARK_THEME,
) {
//    TitleWithAgenda(
//        title = TITLE,
//        agenda = listOf(
//            "First agenda item",
//            "Second agenda item",
//            "Third agenda item",
//        )
//    )

//    WhyBother()
//    Groovy()
//    NoTypeSafety()
    ImperativeCode()
}

