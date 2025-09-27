package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.components.IDE
import dev.panuszewski.template.components.IdeState
import dev.panuszewski.template.components.SlidingTitleScaffold
import dev.panuszewski.template.components.addFile
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.extensions.precompose
import dev.panuszewski.template.extensions.safeGet
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag

fun StoryboardBuilder.WhyWouldYouCare() {
    scene(stateCount = BUILD_GRADLE_KTS.size + 2) {
        BUILD_GRADLE_KTS.precompose()

        SlidingTitleScaffold("Why would you care?") {
            val files = buildList {
                addFile(
                    name = "build.gradle.kts",
                    content = transition.createChildTransition {
                        BUILD_GRADLE_KTS.safeGet(it.toState() - 2)
                    }
                )
            }
            IDE(
                IdeState(
                    files = files,
                    selectedFile = "build.gradle.kts",
                ),
            )
        }
    }
}

private val BUILD_GRADLE_KTS = buildCodeSamples {
    """
    plugins {
        id 'org.jetbrains.kotlin.jvm' version '2.2.20'
    }
    
    repositories {
        mavenCentral()
    }
    
    dependencies {
        testImplementation 'org.jetbrains.kotlin:kotlin-test'
    }
    
    test {
        useJUnitPlatform()
    }
    
    kotlin {
        jvmToolchain(25)
    }    
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
}