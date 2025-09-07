package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IdeState
import dev.panuszewski.components.addDirectory
import dev.panuszewski.components.addFile
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.AmperCatchErrorsEarly() {
    val initialState = 0
    val ideExpands = initialState + 1
    val finalState = initialState + 10

    scene((initialState..finalState).toList()) {
        withStateTransition {
            val ideTopPadding by animateDp { if (it >= ideExpands) 0.dp else 281.dp }

            AmperTitleScaffold("Catching errors early") {
                AMPER_IDE_STATE = IdeState(
                    files = buildList {
                        addFile(
                            name = "module.yaml",
                            content = createChildTransition { MODULE_YAML.safeGet(it) }
                        )
                        addDirectory(name = "src")
                        addDirectory(name = "com/example", path = "src/com/example")
                        addFile(
                            name = "main.kt",
                            path = "src/com/example/main.kt",
                            content = createChildTransition { MAIN_KT[0] }
                        )
                        addDirectory(name = "test")
                        addDirectory(name = "com/example", path = "test/com/example")
                        addFile(
                            name = "ExampleTest.kt",
                            path = "test/com/example/ExampleTest.kt",
                            content = createChildTransition { EXAMPLE_TEST_KT[0] }
                        )
                    },
                    selectedFile = "module.yaml"
                )
                IDE(
                    ideState = AMPER_IDE_STATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp)
                )
            }
        }
    }
}

private val MODULE_YAML = buildCodeSamples {
    $$"""
    product: jvm/app

    settings:
      springBoot:
        enabled: true
        version: 3.5.3

    dependencies:
      - $spring.boot.starter.web
    """
        .trimIndent()
        .toCodeSample(language = Language.Yaml)
        .startWith { hide(this) }
}
