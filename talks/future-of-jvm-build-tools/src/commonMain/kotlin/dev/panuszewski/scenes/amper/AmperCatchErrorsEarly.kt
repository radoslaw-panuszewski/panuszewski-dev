package dev.panuszewski.scenes.amper

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.addDirectory
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.amper_catching_errors

fun StoryboardBuilder.AmperCatchErrorsEarly() {
    val initialState = 0
    val ideExpands = initialState + 1
    val warningAppears = ideExpands + 3
    val warningEnlarged = warningAppears + 1
    val warningDisappears = warningEnlarged + 2
    val finalState = warningDisappears

    scene((initialState..finalState).toList()) {
        withStateTransition {
            val ideTopPadding by animateDp { if (it >= ideExpands) 0.dp else 281.dp }
            val warningHeight by animateDp { if (it == warningEnlarged) 225.dp else 150.dp }
            val warningXOffset by animateDp { if (it == warningEnlarged) 0.dp else 165.dp }
            val warningYOffset by animateDp { if (it == warningEnlarged) 0.dp else 70.dp }
            val ideAlpha by animateFloat { if (it == warningEnlarged) 0.1f else 1f }

            TitleScaffold("Catching errors early") {
                IDE_STATE = IdeState(
                    files = buildList {
                        addFile(
                            name = "module.yaml",
                            content = createChildTransition { MODULE_YAML.safeGet(it - ideExpands) }
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

                transition.FadeOutAnimatedVisibility({ it is Frame.State<*> }) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        IDE(
                            ideState = IDE_STATE,
                            modifier = Modifier
                                .alpha(ideAlpha)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(start = 32.dp, end = 32.dp, top = ideTopPadding, bottom = 32.dp)
                        )
                        Box(Modifier.offset(x = warningXOffset, y = warningYOffset)) {
                            FadeInOutAnimatedVisibility({ it in warningAppears until warningDisappears }) {
                                ResourceImage(
                                    resource = Res.drawable.amper_catching_errors,
                                    modifier = Modifier
                                        .height(warningHeight)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val MODULE_YAML = buildCodeSamples {
    val springCoreDependency by tag()
    val warning by tag()
    val wrongVersion by tag()
    val correctVersion by tag()

    $$"""
    product: jvm/app

    settings:
      springBoot:
        enabled: true
        version: 3.5.3

    dependencies:
      - $spring.boot.starter.web$${springCoreDependency}
      - $${warning}org.springframework:spring-core:$${wrongVersion}5.0.0$${wrongVersion}$${correctVersion}6.2.3$${correctVersion}$${warning}$${springCoreDependency}
    """
        .trimIndent()
        .toCodeSample(language = Language.Yaml)
        .startWith { hide(springCoreDependency).hide(correctVersion) }
        .then { reveal(springCoreDependency) }
        .then {
            focus(
                tag = warning,
                focusedStyle = SpanStyle(background = Color(0xFFFBF4D7)),
                unfocusedStyle = null,
                scroll = false
            )
        }
        .then { this }
        .then { this }
        .then { this }
        .then { reveal(correctVersion).hide(wrongVersion).unfocus() }
}
