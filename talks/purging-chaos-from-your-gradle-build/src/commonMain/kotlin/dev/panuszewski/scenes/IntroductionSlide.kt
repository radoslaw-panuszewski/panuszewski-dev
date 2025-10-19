package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.Title
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.h4
import dev.panuszewski.template.extensions.h5
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_LIGHT_TURQUOISE
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.appendWithColor
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.allegro
import talks.purging_chaos_from_your_gradle_build.generated.resources.gradle
import talks.purging_chaos_from_your_gradle_build.generated.resources.kotlin
import talks.purging_chaos_from_your_gradle_build.generated.resources.speaker_photo

fun StoryboardBuilder.IntroductionSlide() {
    scene(
        stateCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        withIntTransition {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))

                Title { h4 { Text("About me") } }

                Box(modifier = Modifier.padding(32.dp)) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        ResourceImage(Res.drawable.speaker_photo)

                        Spacer(Modifier.width(32.dp))

                        Column(
                            modifier = Modifier
                                .border(1.dp, NICE_BLUE)
                                .background(color = NICE_BLUE.copy(alpha = 0.03f))
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(16.dp)
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            h5 {
                                Text {
                                    appendWithColor(NICE_BLUE, "Radek Panuszewski")
                                }
                            }

                            RevealSequentially {
                                item {
                                    Row {
                                        Text("Senior Software Engineer at  ")

                                        with(LocalDensity.current) {
                                            ResourceImage(
                                                resource = Res.drawable.allegro,
                                                modifier = Modifier.height(MaterialTheme.typography.h6.lineHeight.toDp())
                                            )
                                        }
                                    }
                                }
                                annotatedStringItem {
                                    append("working on ")
                                    appendWithColor(NICE_LIGHT_TURQUOISE, "Developer Experience")
                                }
                                item {
                                    Row {
                                        with(LocalDensity.current) {
                                            ResourceImage(
                                                resource = Res.drawable.kotlin,
                                                modifier = Modifier.height(MaterialTheme.typography.h6.lineHeight.toDp())
                                            )
                                            Text("  and  ")
                                            ResourceImage(
                                                resource = Res.drawable.gradle,
                                                modifier = Modifier.height(MaterialTheme.typography.h6.lineHeight.toDp())
                                            )
                                            Text("  enthusiast!")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}