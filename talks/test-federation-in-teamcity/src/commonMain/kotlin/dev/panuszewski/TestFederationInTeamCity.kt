import dev.bnorm.storyboard.SceneFormat.Companion.Default
import dev.bnorm.storyboard.Storyboard
import dev.panuszewski.scenes.DynamicBuildChains
import dev.panuszewski.scenes.SkipTags
import dev.panuszewski.scenes.TeamcityPlugin
import dev.panuszewski.scenes.TitleScene
import dev.panuszewski.template.theme.TOGGLEABLE_THEME

private const val TITLE = "Test Federation in TeamCity"

val TestFederationInTeamCity = Storyboard.build(
    title = TITLE,
    format = Default,
    decorator = TOGGLEABLE_THEME,
) {
    TitleScene()
    TeamcityPlugin()
    DynamicBuildChains()
    SkipTags()
}
