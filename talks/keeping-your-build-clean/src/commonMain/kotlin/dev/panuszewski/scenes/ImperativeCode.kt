package dev.panuszewski.scenes

import androidx.compose.material.Text
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeStateWithMapping
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

fun StoryboardBuilder.ImperativeCode() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "buildSrc" to DIRECTORY,
        "buildSrc/src/main/kotlin" to DIRECTORY
    )
    val emojiAppears = calculateTotalStates(files)

    scene(100) {
        withStateTransition {
            TitleScaffold("Imperative code") {
                IDE_STATE = buildIdeStateWithMapping(files)

                IdeLayout {
                    centerEmoji(visibleAt = emojiAppears) {
                        Text("😬")
                    }
                }
            }
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
        alias(libs.plugins.kotlin.jvm)${javaPlugin}${mavenPublishDeclarative}
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
        implementation(projects.firstLibrary)
        implementation(libs.spring.boot.web)${randomDatabase}
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
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(wtfAppPlugin, mavenPublishImperative, topIfCi, bottomIfCi, topWhen, bottomWhen, monday, postgres, cassandra, masochistIfTop, masochistIfBottom, someImperativeCode) }
        .then { reveal(mavenPublishImperative).hide(mavenPublishDeclarative) }
        .then { reveal(topIfCi, bottomIfCi) }
        .then { reveal(topWhen, bottomWhen, monday) }
        .then { reveal(postgres, cassandra) }
        .then { reveal(masochistIfTop, masochistIfBottom) }
//        .then { focus(javaPlugin, mavenPublishImperative, randomDatabase, groovy) }
//        .then { hide(javaPlugin, mavenPublishImperative, randomDatabase, groovy).reveal(wtfAppPlugin).focus(wtfAppPlugin) }
//        .then { unfocus() }
//        .then { reveal(someImperativeCode).focus(someImperativeCode) }
//        .then { this }
//        .then { unfocus() }
}

val WTF_APP_GRADLE_KTS = buildCodeSamples {
    val todo by tag()
    val imperativeCode by tag()
    val javaPlugin by tag()
    val implementation1 by tag()
    val implementation2 by tag()
    val implementation3 by tag()
    val implementation4 by tag()

    """
        ${javaPlugin}plugins {
            `java`
        }
        
        ${javaPlugin}${imperativeCode}if (System.getenv("CI") == "true") {
            apply(plugin = "maven-publish")
        }
        
        dependencies {
            when (today()) {
                MONDAY -> ${implementation1}implementation${implementation1}(libs.mongodb)
                TUESDAY -> ${implementation2}implementation${implementation2}(libs.postgres)
                else -> ${implementation3}implementation${implementation3}(libs.cassandra)
            }
            if (masochistModeEnabled()) {
                ${implementation4}implementation${implementation4}(libs.groovy)
            }
        }
        
        ${imperativeCode}${todo}// TODO${todo}
        """
        .trimIndent()
        .toCodeSample(language = Language.Kotlin)
        .startWith { hide(javaPlugin, imperativeCode) }
        .then { this }
        .then { reveal(imperativeCode, javaPlugin) }
}