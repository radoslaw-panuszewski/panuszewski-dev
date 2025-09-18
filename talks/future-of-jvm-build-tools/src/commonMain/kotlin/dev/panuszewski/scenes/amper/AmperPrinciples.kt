package dev.panuszewski.scenes.amper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IDE_STATE
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.template.RevealSequentially
import dev.panuszewski.template.Text
import dev.panuszewski.template.withPrimaryColor
import dev.panuszewski.template.withStateTransition

fun StoryboardBuilder.AmperPrinciples() {
    scene(stateCount = 5) {
        withStateTransition {
            TitleScaffold("Amper Principles") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RevealSequentially(since = 1) {
                            item {
                                Text {
                                    withPrimaryColor { append("Show up when needed") }
                                    append(", stay out of the way when not")
                                }
                            }
                            item {
                                Text {
                                    withPrimaryColor { append("Catch errors early") }
                                    append(", promote configuration that reflects reality")
                                }
                            }
                            item {
                                Text {
                                    append("Minimize ")
                                    withPrimaryColor { append("user interactions") }
                                    append(" with the build config")
                                }
                            }
                            item {
                                Text {
                                    append("Provide smooth ")
                                    withPrimaryColor { append("IDE integration") }
                                }
                            }
                        }
                    }

                    IDE(
                        ideState = IDE_STATE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp, top = 281.dp, bottom = 32.dp)
                    )
                }
            }
        }
    }
}