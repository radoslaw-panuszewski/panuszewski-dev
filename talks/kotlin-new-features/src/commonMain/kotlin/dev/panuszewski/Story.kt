package dev.panuszewski

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import dev.bnorm.kc25.sections.stages.GuardConditions
import dev.bnorm.kc25.sections.stages.Kotlin2_1
import dev.bnorm.kc25.sections.stages.MultiDollarInterpolation
import dev.bnorm.kc25.sections.stages.NonLocalBreakContinue
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.stages.KotlinTimelineStage
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_1
import dev.panuszewski.stages.KotlinTimelineStage.KOTLIN_2_2
import dev.panuszewski.stages.Timeline

fun createStoryboard(decorator: SceneDecorator = theme): Storyboard {
    return Storyboard.build(
        title = "Basic Storyboard",
        format = SceneFormat.Default,
        decorator = theme,
    ) {
        Timeline(stageToBeExpanded = KOTLIN_2_1)
        Kotlin2_1()
        GuardConditions()
        Kotlin2_1(startState = 3)
        NonLocalBreakContinue()
        Kotlin2_1(startState = 3)
        MultiDollarInterpolation()
        Kotlin2_1(startState = 3)
        Timeline(stageToBeExpanded = KOTLIN_2_2)
    }
}

private val theme = SceneDecorator { content ->
    val colors = darkColors(
        background = Color.Black,
        surface = Color(0xFF1E1F22),
        onBackground = Color(0xFFBCBEC4),
        primary = Color(0xFF0E4A3E),
        primaryVariant = Color(0xFF145F50),
        secondary = Color(0xFF942DF3),
    )

    val typography = Typography()

    MaterialTheme(colors, typography) {
        Surface {
            content()
        }
    }
}
