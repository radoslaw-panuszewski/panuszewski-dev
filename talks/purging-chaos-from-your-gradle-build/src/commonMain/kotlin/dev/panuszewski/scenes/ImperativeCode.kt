package dev.panuszewski.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.components.Agenda
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.typesafe_conventions

fun StoryboardBuilder.ImperativeCode() {

    val files = listOf(
        "build.gradle.kts" to BUILD_GRADLE_KTS,
        "buildSrc" to DIRECTORY.initiallyHidden(),
        "buildSrc/src/main/kotlin" to DIRECTORY.initiallyHidden(),
        "buildSrc/src/main/kotlin/wtf-app.gradle.kts" to WTF_APP_GRADLE_KTS.initiallyHidden(),
        "buildSrc/settings.gradle.kts" to BUILD_SRC_SETTINGS.initiallyHidden(),
        "buildSrc/build.gradle.kts" to BUILD_SRC_BUILDSCRIPT.initiallyHidden(),
    )
    val totalStates = calculateTotalStates(files)

    scene(totalStates) {
        withIntTransition {
            TitleScaffold("Imperative code") {
                val ideState = buildIdeState(files)

                ideState.IdeLayout {
                    adaptiveTopPanel(name = "typesafe-conventions") {
                        ResourceImage(
                            resource = Res.drawable.typesafe_conventions,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(8.dp)
                        )
                    }

                    leftPanel("agenda") { panelState ->
                        panelState.Agenda {
                            item("Groovy", crossedOutSince = 0)
                            item("No type safety", crossedOutSince = 0)
                            item("Imperative code", crossedOutSince = 1)
                            item("Cross configuration")
                            item("Mixed concerns")
                        }
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
        implementation(projects.subProject)
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
        .startWith { hide(wtfAppPlugin, mavenPublishDeclarative, mavenPublishImperative, randomDatabase, groovy, topIfCi, bottomIfCi, topWhen, bottomWhen, monday, postgres, cassandra, masochistIfTop, masochistIfBottom, someImperativeCode) }
        .then { reveal(mavenPublishDeclarative, randomDatabase, groovy) }
        .then { reveal(mavenPublishImperative).hide(mavenPublishDeclarative) }
        .then { reveal(topIfCi, bottomIfCi) }
        .then { revealAndFocusNoStyling(topWhen, bottomWhen, monday) }
        .then { revealAndFocusNoStyling(postgres, cassandra) }
        .then { revealAndFocusNoStyling(masochistIfTop, masochistIfBottom) }
        .then { unfocus().showEmoji("😬") }
        .hideEmoji()
        .openInRightPane("buildSrc/src/main/kotlin/wtf-app.gradle.kts", switchTo = true)
        .then { focus(javaPlugin, mavenPublishImperative, randomDatabase, groovy) }
        .then { hide(javaPlugin, mavenPublishImperative, randomDatabase, groovy).reveal(wtfAppPlugin).focus(wtfAppPlugin) }
        .then { unfocus() }
        .openAgenda()
        .pass()
        .closeAgenda()
}

val WTF_APP_GRADLE_KTS = buildCodeSamples {
    val todo by tag()
    val extractedCode by tag()
    val libsPlugin by tag()
    val libsDep1 by tag()
    val libsDep2 by tag()
    val libsDep3 by tag()
    val libsDep4 by tag()
    val nonTypesafePlugin by tag()
    val nonTypesafeDep1 by tag()
    val nonTypesafeDep2 by tag()
    val nonTypesafeDep3 by tag()
    val nonTypesafeDep4 by tag()

    """
    ${extractedCode}plugins {
        ${libsPlugin}alias(libs.plugins.kotlin.jvm)${libsPlugin}${nonTypesafePlugin}id("org.jetbrains.kotlin.jvm")${nonTypesafePlugin}
    }
    
    if (System.getenv("CI") == "true") {
        apply(plugin = "maven-publish")
    }
    
    dependencies {
        when (today()) {
            MONDAY -> implementation(${libsDep1}libs.mongodb${libsDep1}${nonTypesafeDep1}versionCatalogs.find("libs").get().findLibrary("mongodb").get()${nonTypesafeDep1})
            TUESDAY -> implementation(${libsDep2}libs.postgres${libsDep2}${nonTypesafeDep2}versionCatalogs.find("libs").get().findLibrary("postgres").get()${nonTypesafeDep2})
            else -> implementation(${libsDep3}libs.cassandra${libsDep3}${nonTypesafeDep3}versionCatalogs.find("libs").get().findLibrary("cassandra").get()${nonTypesafeDep3})
        }
        if (masochistModeEnabled()) {
            implementation(${libsDep4}libs.groovy${libsDep4}${nonTypesafeDep4}versionCatalogs.find("libs").get().findLibrary("groovy").get()${nonTypesafeDep4})
        }
    }
    
    ${extractedCode}${todo}// TODO${todo}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(extractedCode, nonTypesafePlugin, nonTypesafeDep1, nonTypesafeDep2, nonTypesafeDep3, nonTypesafeDep4) }
        .hideFileTree()
        .thenTogetherWith("build.gradle.kts") { this }
        .thenTogetherWith("build.gradle.kts") { reveal(extractedCode) }
        .thenTogetherWith("build.gradle.kts") { hide(todo) }
        .then { closeLeftPane().showFileTree() }
        .then { highlightAsError(libsPlugin, libsDep1, libsDep2, libsDep3, libsDep4) }
        .openErrorWindow("e: Unresolved reference 'libs'\n\n\n")
        .closeErrorWindow()
        .switchTo("buildSrc/settings.gradle.kts")
        .then { this }
        .then { focus(libsPlugin) }
        .then { hide(libsPlugin).reveal(nonTypesafePlugin).highlightAsError(libsDep1, libsDep2, libsDep3, libsDep4) }
        .then { unfocus().focusNoScroll(libsDep1, libsDep2, libsDep3, libsDep4) }
        .then { hide(libsDep1, libsDep2, libsDep3, libsDep4).reveal(nonTypesafeDep1, nonTypesafeDep2, nonTypesafeDep3, nonTypesafeDep4).unfocus().hideFileTree() }
        .showEmoji("😩")
        .hideEmoji()
        .openPanel("typesafe-conventions")
        .closePanel("typesafe-conventions")
        .showFileTree()
        .switchTo("buildSrc/settings.gradle.kts")
        .then { this }
        .then { focus(nonTypesafePlugin, nonTypesafeDep1, nonTypesafeDep2, nonTypesafeDep3, nonTypesafeDep4) }
        .then { hide(nonTypesafePlugin, nonTypesafeDep1, nonTypesafeDep2, nonTypesafeDep3, nonTypesafeDep4).reveal(libsPlugin, libsDep1, libsDep2, libsDep3, libsDep4).unfocus() }
        .switchTo("build.gradle.kts")
}

private val BUILD_SRC_BUILDSCRIPT = buildCodeSamples {
    val pluginDependency by tag()
    val pluginMarkerUsage1 by tag()
    val pluginMarkerUsage2 by tag()
    val pluginMarkerFunction by tag()

    $$"""
    plugins {
        `kotlin-dsl`
    }$${pluginMarkerFunction}
    
    fun pluginMarker(provider: Provider<PluginDependency>): String {
        val pluginId = provider.get().pluginId
        val pluginVersion = provider.get().version
        
        return "$pluginId:$pluginId.gradle.plugin:$pluginVersion"
    }$${pluginMarkerFunction}
    
    dependencies { $${pluginDependency}
        implementation($${pluginMarkerUsage1}pluginMarker($${pluginMarkerUsage1}libs.plugins.kotlin.jvm$${pluginMarkerUsage2})$${pluginMarkerUsage2})
    $${pluginDependency}}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(pluginDependency, pluginMarkerUsage1, pluginMarkerUsage2, pluginMarkerFunction) }
        .then { reveal(pluginDependency) }
        .openErrorWindow(
            """
            > Could not resolve all dependencies for configuration ':buildSrc:buildScriptClasspath'.
               > Cannot convert the provided notation to an object of type Dependency: org.jetbrains.kotlin.jvm:2.2.20.
                 The following types/formats are supported:
                    - String or CharSequence values, for example 'org.gradle:gradle-core:1.0'.
                    - Maps, for example [group: 'org.gradle', name: 'gradle-core', version: '1.0'].
                    - FileCollections, for example files('some.jar', 'someOther.jar').
                    - Projects, for example project(':some:project:path').
                    - ClassPathNotation, for example gradleApi().
        """.trimIndent()
        )
        .closeErrorWindow()
        .then { reveal(pluginMarkerUsage1, pluginMarkerUsage2).focus(pluginMarkerUsage1, pluginMarkerUsage2) }
        .then { revealAndFocus(pluginMarkerFunction) }
        .then { unfocus() }
        .switchTo("buildSrc/src/main/kotlin/wtf-app.gradle.kts")
}

private val BUILD_SRC_SETTINGS = buildCodeSamples {
    val typesafeConventions by tag()
    val versionCatalogDeclaration by tag()

    """
    ${typesafeConventions}plugins {
        id("dev.panuszewski.typesafe-conventions") version "0.9.0"
    }
    
    ${typesafeConventions}dependencyResolutionManagement {
        repositories {
            mavenCentral()
        }${versionCatalogDeclaration}
        
        versionCatalogs {
            create("libs") {
                from(files("../gradle/libs.versions.toml"))
            }
        }${versionCatalogDeclaration}
    } 
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(versionCatalogDeclaration, typesafeConventions) }
        .then { reveal(versionCatalogDeclaration) }
        .switchTo("buildSrc/build.gradle.kts")
        .then { this }
        .then { hide(versionCatalogDeclaration) }
        .then { revealAndFocus(typesafeConventions) }
        .then { unfocus() }
        .switchTo("buildSrc/src/main/kotlin/wtf-app.gradle.kts")
}