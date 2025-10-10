package dev.panuszewski

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.scenes.CrossConfiguration
import dev.panuszewski.scenes.Groovy
import dev.panuszewski.scenes.ImperativeCode
import dev.panuszewski.scenes.NoTypeSafety
import dev.panuszewski.scenes.WhyBother
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.theme.DARK_THEME
import dev.panuszewski.template.theme.LIGHT_THEME
import talks.keeping_your_build_clean.generated.resources.Res
import talks.keeping_your_build_clean.generated.resources.typesafe_conventions

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
//    ImperativeCode()
    CrossConfiguration()
}


