package dev.panuszewski.scenes

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.SCENE_FADE
import dev.panuszewski.components.Design.ANNOTATIONS
import dev.panuszewski.components.Implementation.SKIP_TAGS
import dev.panuszewski.components.Requirement.BREADCRUMBS
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.components.Verdict
import dev.panuszewski.components.appendDesign
import dev.panuszewski.components.appendImplementation
import dev.panuszewski.components.appendRequirementShort
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.WrappingUp() {
    val (
        titleOnly,
        firstBullet,
        secondBullet,
        thirdBullet,
        fourthBullet,
        proposalAppears,
        openQuestionAppears,
        lastState
    ) = subsequentNumbers()

    scene(
        stateCount = lastState,
        enterTransition = { fadeIn(SCENE_FADE) },
        exitTransition = { fadeOut(SCENE_FADE) },
    ) {
        withIntTransition {
            SectionScaffold("Wrapping Up") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 14.dp) {
                        RevealSequentially(since = firstBullet) {
                            annotatedStringItem {
                                append("Split every configuration into ")
                                appendWithColor(palette.orange, "variants")
                                append(" that always execute the same tests")
                            }
                            annotatedStringItem {
                                append("Replace execution modes with ")
                                appendWithColor(palette.turquoise, "test slices")
                                append(" — 🛡️ AllTests, 💨 SmokeTests, 🤝 ContractTests")
                            }
                            annotatedStringItem {
                                appendDesign(ANNOTATIONS)
                                append(" is the only design every mechanism supports, and the only one with ")
                                appendRequirementShort(BREADCRUMBS, "✅")
                            }
                            annotatedStringItem {
                                appendImplementation(SKIP_TAGS)
                                append(" is the only all-🟢 mechanism, and ")
                                appendAsCode("SafeMergeCoordinator")
                                append(" already gives us the trigger")
                            }
                        }
                    }

                    Verdict(since = proposalAppears, icon = "👉") {
                        append("Proposal: implement ")
                        appendDesign(ANNOTATIONS)
                        append(" on top of ")
                        appendImplementation(SKIP_TAGS)
                    }

                    CenteredColumn(Modifier.fillMaxWidth(), spacing = 12.dp) {
                        RevealSequentially(since = openQuestionAppears) {
                            annotatedStringItem {
                                append("Open question: is ")
                                appendWithColor(palette.turquoise, "Clear TeamCity UX")
                                append(" worth the cost of a custom plugin?")
                            }
                        }
                    }
                }
            }
        }
    }
}
