package dev.panuszewski

import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.components.AgendaDivider
import dev.panuszewski.scenes.AnnotationsAndAggregate
import dev.panuszewski.scenes.Comparison
import dev.panuszewski.scenes.Context
import dev.panuszewski.scenes.ContractsOrSeparated
import dev.panuszewski.scenes.DumpingContracts
import dev.panuszewski.scenes.DynamicBuildChains
import dev.panuszewski.scenes.HowManyContractConfigs
import dev.panuszewski.scenes.Requirements
import dev.panuszewski.scenes.SkipTags
import dev.panuszewski.scenes.TeamcityPlugin
import dev.panuszewski.scenes.TestSlices
import dev.panuszewski.scenes.ThankYouSlide
import dev.panuszewski.scenes.TitleScene
import dev.panuszewski.scenes.VirtualConfigsAndDsl
import dev.panuszewski.scenes.WrappingUp
import dev.panuszewski.template.theme.TOGGLEABLE_THEME

private const val TITLE = "Test Federation setup on TeamCity"

val TestFederationInTeamCity = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = TOGGLEABLE_THEME,
) {
    TitleScene()

    // 1. Context
    AgendaDivider(currentSection = 1)
    Context()

    // 2. Requirements
    AgendaDivider(currentSection = 2)
    Requirements()

    // 3. Design choices
    AgendaDivider(currentSection = 3)
    ContractsOrSeparated()
    HowManyContractConfigs()
    DumpingContracts()
    TestSlices()

    // 4. API design
    AgendaDivider(currentSection = 4)
    VirtualConfigsAndDsl()
    AnnotationsAndAggregate()

    // 5. Implementation
    AgendaDivider(currentSection = 5)
    TeamcityPlugin()
    DynamicBuildChains()
    SkipTags()

    Comparison()
    WrappingUp()
    ThankYouSlide()
}
