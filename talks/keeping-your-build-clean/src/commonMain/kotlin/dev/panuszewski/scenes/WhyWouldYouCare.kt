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
import dev.panuszewski.template.extensions.TagType.NORMAL
import dev.panuszewski.template.extensions.TagType.WARNING
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
    val allCode by tag(NORMAL)

    """
    ${allCode}buildscript {
        classpath('org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20')
    }
    
    allprojects {
        apply plugin: 'kotlin'
    }
        
    subprojects
        .findAll {it.name.endsWith('-library') }
        .forEach {
            it.apply plugin: 'java-library'
        }
    
    println "I'm Groovy lol"
    
    dependencies {
        implementation project(':first-library')
        testImplementation 'org.jetbrains.kotlin:kotlin-test'
    }${allCode}
    """
        .trimIndent()
        .toCodeSample(language = Language.Groovy)
        .startWith { this }
        .then { changeTagType(allCode, WARNING) }
}