package dev.panuszewski.scenes.gradle

import androidx.compose.animation.core.createChildTransition
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.MagicCodeSample
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.buildAndRememberCodeSamples
import dev.panuszewski.template.extensions.code2
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag

fun StoryboardBuilder.ExplainingConfigExecutionDifference() {
    scene(5) {
        TitleScaffold("Gradle") {
            with(transition) {
                PhasesBar()

                val phaseSamples = buildAndRememberCodeSamples {
                    val first by tag()
                    val second by tag()
                    val third by tag()
                    val fourth by tag()

                    """
                    plugins {${third}
                        println("Even this place belongs to configuration phase")${third}
                        java
                    }${first}
                    
                    println("Hello from configuration phase!")${first}
                    
                    dependencies {${second}
                        println("It's still configuration phase")${second}
                        implementation("pl.allegro.tech.common:andamio-starter-core:9.0.0")
                    }
                
                    tasks {
                        register("sayHello") {
                            doLast {
                                ${fourth}println("Finally, the execution phase!")${fourth}
                            }
                        }
                    }
                    """
                        .trimIndent()
                        .toCodeSample(language = Language.Kotlin)
                        .startWith { hide(first, second, third, fourth) }
                        .then { reveal(first).focus(first) }
                        .then { reveal(second).focus(second).hide(first) }
                        .then { reveal(third).focus(third).hide(second) }
                        .then { reveal(fourth).focus(fourth).hide(third) }
                }

                SlideFromBottomAnimatedVisibility({ it is Frame.State<*> }) {
                    code2 {
                        createChildTransition { phaseSamples.safeGet(it.toState()) }
                            .MagicCodeSample()
                    }
                }
            }
        }
    }
}