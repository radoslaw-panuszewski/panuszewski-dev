package dev.panuszewski.scenes.gradle

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.appendWithPrimaryColor
import dev.panuszewski.template.h6
import dev.panuszewski.template.toCode

fun StoryboardBuilder.ShowingThatBuildCacheIsOld() {
    scene(3) {
        with(transition) {
            TitleScaffold("Gradle") {
                PhasesBar()

                FadeOutAnimatedVisibility({ it is Frame.State<*> }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        h6 {
                            SlideFromTopAnimatedVisibility({ it.toState() >= 1 }) {
                                Text("Build Cache is there since Gradle 3.5 👴🏼")
                            }
                            Spacer(Modifier.height(32.dp))
                            SlideFromTopAnimatedVisibility({ it.toState() >= 2 }) {
                                Text {
                                    append("Just enable it in your ")
                                    appendWithPrimaryColor("gradle.properties")
                                    append("!")
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        SlideFromBottomAnimatedVisibility({ it.toState() >= 2 }) {
                            Box(
                                modifier = Modifier
                                    .border(
                                        color = Color.Black,
                                        width = 1.dp,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text("org.gradle.caching=true".toCode(language = Language.Properties))
                            }
                        }
                    }
                }
            }
        }
    }
}