package dev.panuszewski.scenes.gradle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.scenes.gradle.Hat.BASEBALL_CAP
import dev.panuszewski.scenes.gradle.Hat.TOP_HAT
import dev.panuszewski.template.Stages
import dev.panuszewski.template.h1
import dev.panuszewski.template.h6

private val stages = Stages()
private val lastState: Int get() = stages.lastState

private val PHASES_BAR_APPEARS = stages.registerStatesByCount(start = lastState + 1, count = 1)
private val CHARACTERIZING_PHASES = stages.registerStatesByCount(start = lastState + 1, count = 3)
private val EXPLAINING_CONFIG_EXECUTION_DIFFERENCE = stages.registerStatesByCount(start = lastState + 2, count = 5)
private val EXECUTION_BECOMES_LONG = stages.registerStatesByCount(start = lastState + 2, count = 1)
private val SHOWING_THAT_BUILD_CACHE_IS_OLD = stages.registerStatesByCount(start = lastState + 2, count = 2)
private val EXECUTION_BECOMES_SHORT = stages.registerStatesByCount(start = lastState + 1, count = 1)
private val CONFIGURATION_IS_LONG = stages.registerStatesByCount(start = lastState + 1, count = 28)
private val PHASES_BAR_DISAPPEARS = stages.registerStatesByCount(start = lastState + 2, count = 1)
private val EXTRACTING_CONVENTION_PLUGIN = stages.registerStatesByCount(start = lastState + 1, count = 9)
private val EXPLAINING_CONVENTION_PLUGINS = stages.registerStatesByCount(start = lastState + 1, count = 26)
private val SOFTWARE_DEVELOPER_AND_BUILD_ENGINEER = stages.registerStatesByCount(lastState + 1, count = 7)
private val DECLARATIVE_GRADLE = stages.registerStatesByCount(lastState + 1, count = 19)

private val PHASES_BAR_VISIBLE = PHASES_BAR_APPEARS.first() until PHASES_BAR_DISAPPEARS.first()
private val EXECUTION_IS_LONG = EXECUTION_BECOMES_LONG.first() until EXECUTION_BECOMES_SHORT.first()
private val CONVENTION_PLUGINS = EXTRACTING_CONVENTION_PLUGIN + EXPLAINING_CONVENTION_PLUGINS

fun StoryboardBuilder.Gradle() {
    CharacterizingPhases()
    ExplainingConfigExecutionDifference()
    BuildCache()
    ConfigurationCache()
    ConventionPlugins()
    TwoTypesOfGradleUsers()
    DeclarativeGradle()
}

@Composable
fun SoftwareDeveloper(modifier: Modifier = Modifier) {
    GuyChangingHats(modifier = modifier.scale(0.75f), hat = BASEBALL_CAP, name = "Software Developer")
}

@Composable
fun BuildEngineer(modifier: Modifier = Modifier) {
    GuyChangingHats(modifier = modifier.scale(0.75f), hat = TOP_HAT, name = "Build Engineer")
}

@Composable
fun GuyChangingHats(modifier: Modifier = Modifier, name: String?, hat: Hat) {
    Column(
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            h1 { Text("😁", Modifier.offset(y = 50.dp)) }

            AnimatedContent(
                targetState = hat,
                contentAlignment = Alignment.Center,
                transitionSpec = { slideInVertically() togetherWith slideOutVertically() }
            ) { state ->
                h1 {
                    when (state) {
                        BASEBALL_CAP -> Text("🧢", Modifier.offset(x = -10.dp, y = 10.dp))
                        TOP_HAT -> Text("🎩", Modifier.offset(x = -2.dp, y = -10.dp))
                    }
                }
            }
        }

        if (name != null) {
            h6 { Text(name) }
        }
    }
}

enum class Hat { BASEBALL_CAP, TOP_HAT }