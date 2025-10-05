package dev.panuszewski.scenes

import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.panuszewski.template.components.DIRECTORY
import dev.panuszewski.template.components.IDE_STATE
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.calculateTotalStates
import dev.panuszewski.template.components.initiallyHidden
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withStateTransition

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
        withStateTransition {
            TitleScaffold("Imperative code") {
                IDE_STATE = buildIdeState(files)

                IdeLayout { }
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
        .then { openLeftPanel().hideFileTree() }
        .then { reveal(mavenPublishImperative).hide(mavenPublishDeclarative) }
        .then { reveal(topIfCi, bottomIfCi) }
        .then { reveal(topWhen, bottomWhen, monday) }
        .then { reveal(postgres, cassandra) }
        .then { reveal(masochistIfTop, masochistIfBottom) }
        .showEmoji("😬")
        .hideEmoji()
        .openInRightPane("buildSrc/src/main/kotlin/wtf-app.gradle.kts", switchTo = true)
        .then { focus(javaPlugin, mavenPublishImperative, randomDatabase, groovy) }
        .then { hide(javaPlugin, mavenPublishImperative, randomDatabase, groovy).reveal(wtfAppPlugin).focus(wtfAppPlugin) }
        .then { unfocus() }
        .then { reveal(someImperativeCode).focus(someImperativeCode) }
        .then { unfocus() }
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
        .then { highlightAsError(libsPlugin, libsDep1, libsDep2, libsDep3, libsDep4).openErrorWindow("e: Unresolved reference 'libs'") }
        .closeErrorWindow()
        .switchTo("buildSrc/settings.gradle.kts")
        .then { this }
        .then { focus(libsPlugin) }
        .then { hide(libsPlugin).reveal(nonTypesafePlugin).unfocus() }
        .then { focusNoScroll(libsDep1, libsDep2, libsDep3, libsDep4) }
        .then { hide(libsDep1, libsDep2, libsDep3, libsDep4).reveal(nonTypesafeDep1, nonTypesafeDep2, nonTypesafeDep3, nonTypesafeDep4).unfocus().hideFileTree() }
        .showEmoji("😩")
        .hideEmoji()
}

private val BUILD_SRC_BUILDSCRIPT = buildCodeSamples {
    val pluginDependency by tag()
    val pluginMarkerUsage1 by tag()
    val pluginMarkerUsage2 by tag()
    val pluginMarkerDeclaration by tag()
    val pluginMarkerArgs by tag()
    val pluginMarkerBody by tag()

    $$"""
    plugins {
        `kotlin-dsl`
    }$${pluginMarkerDeclaration}
    
    fun pluginMarker($${pluginMarkerArgs}provider: Provider<PluginDependency>$${pluginMarkerArgs}): String$${pluginMarkerDeclaration}$${pluginMarkerBody} {
        val pluginId = provider.get().pluginId
        val pluginVersion = provider.get().version
        
        return "$pluginId:$pluginId.gradle.plugin:$pluginVersion"
    }$${pluginMarkerBody}
    
    dependencies { $${pluginDependency}
        implementation($${pluginMarkerUsage1}pluginMarker($${pluginMarkerUsage1}libs.plugins.kotlin.jvm$${pluginMarkerUsage2})$${pluginMarkerUsage2})
    $${pluginDependency}}
    """
        .trimIndent()
        .toCodeSample(language = Language.KotlinDsl)
        .startWith { hide(pluginDependency, pluginMarkerUsage1, pluginMarkerUsage2, pluginMarkerDeclaration, pluginMarkerBody, pluginMarkerArgs) }
        .then { reveal(pluginDependency) }
        .openErrorWindow("""
            > Could not resolve all dependencies for configuration ':buildSrc:buildScriptClasspath'.
               > Cannot convert the provided notation to an object of type Dependency: org.jetbrains.kotlin.jvm:2.2.20.
                 The following types/formats are supported:
                    - String or CharSequence values, for example 'org.gradle:gradle-core:1.0'.
                    - Maps, for example [group: 'org.gradle', name: 'gradle-core', version: '1.0'].
                    - FileCollections, for example files('some.jar', 'someOther.jar').
                    - Projects, for example project(':some:project:path').
                    - ClassPathNotation, for example gradleApi().

        """.trimIndent())
        .closeErrorWindow()
        .then { reveal(pluginMarkerUsage1, pluginMarkerUsage2).focus(pluginMarkerUsage1, pluginMarkerUsage2) }
        .then { reveal(pluginMarkerDeclaration).focus(pluginMarkerDeclaration) }
        .then { reveal(pluginMarkerArgs).focus(pluginMarkerArgs) }
        .then { reveal(pluginMarkerBody).unfocus() }
        .switchTo("buildSrc/src/main/kotlin/wtf-app.gradle.kts")
}

private val BUILD_SRC_SETTINGS = buildCodeSamples {
    val versionCatalogDeclaration by tag()

    """
    dependencyResolutionManagement {
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
        .startWith { hide(versionCatalogDeclaration) }
        .then { reveal(versionCatalogDeclaration) }
        .switchTo("buildSrc/build.gradle.kts")
}