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
    val pluginsBlock by tag()
    val pluginsBlockNewline by tag()
    val mavenPublishDeclarative by tag()
    val mavenPublishImperative by tag()
    val topIfCi by tag()
    val bottomIfCi by tag()
    val topWhen by tag()
    val bottomWhen by tag()
    val monday by tag()
    val postgres by tag()
    val cassandra by tag()
    val masochistIfTop by tag()
    val masochistIfBottom by tag()
    val javaPlugin by tag()
    val wtfAppPlugin by tag()
    val randomDatabase by tag()
    val groovy by tag()
    val someImperativeCode by tag()

    """
        ${pluginsBlock}plugins {${javaPlugin}
            `java`${javaPlugin}${mavenPublishDeclarative}
            `maven-publish`${mavenPublishDeclarative}${wtfAppPlugin}
            `wtf-app`${wtfAppPlugin}${pluginsBlockNewline}
        ${pluginsBlockNewline}}
        
        ${pluginsBlock}${mavenPublishImperative}${topIfCi}if (System.getenv("CI") == "true") {
            ${topIfCi}apply(plugin = "maven-publish")${bottomIfCi}
        }${bottomIfCi}
        
        ${mavenPublishImperative}${someImperativeCode}for (project in subprojects) {
            apply(plugin = "kotlin")
        }
        
        ${someImperativeCode}dependencies {
            implementation(libs.spring.boot)${randomDatabase}
            ${topWhen}
            when (today()) {
                ${topWhen}${monday}MONDAY -> ${monday}implementation(libs.mongodb)${postgres}
                TUESDAY -> implementation(libs.postgres)${postgres}${cassandra}
                else -> implementation(libs.cassandra)${cassandra}${bottomWhen}
            }${bottomWhen}${randomDatabase}${groovy}
            ${masochistIfTop}
            if (masochistModeEnabled()) {
                ${masochistIfTop}implementation(libs.groovy)${masochistIfBottom}
            }${masochistIfBottom}${groovy}
        }
        """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith {
            hide(
                wtfAppPlugin,
                mavenPublishImperative,
                topIfCi,
                bottomIfCi,
                topWhen,
                bottomWhen,
                monday,
                postgres,
                cassandra,
                masochistIfTop,
                masochistIfBottom,
                someImperativeCode
            )
        }
        .then { reveal(mavenPublishImperative).hide(mavenPublishDeclarative) }
        .then { reveal(topIfCi, bottomIfCi) }
        .then { reveal(topWhen, bottomWhen, monday) }
        .then { reveal(postgres, cassandra) }
        .then { reveal(masochistIfTop, masochistIfBottom) }
        .then { focus(javaPlugin, mavenPublishImperative, randomDatabase, groovy) }
        .then {
            hide(javaPlugin, mavenPublishImperative, randomDatabase, groovy).reveal(wtfAppPlugin)
                .focus(wtfAppPlugin)
        }
        .then { unfocus() }
        .then { reveal(someImperativeCode).focus(someImperativeCode) }
        .then { this }
        .then { unfocus() }
}