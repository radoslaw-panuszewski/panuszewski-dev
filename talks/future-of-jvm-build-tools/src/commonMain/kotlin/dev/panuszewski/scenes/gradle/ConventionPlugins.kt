package dev.panuszewski.scenes.gradle

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.toState
import dev.panuszewski.components.IDE
import dev.panuszewski.components.IdeState
import dev.panuszewski.components.TitleScaffold
import dev.panuszewski.components.addDirectory
import dev.panuszewski.components.addFile
import dev.panuszewski.template.FadeInOutAnimatedVisibility
import dev.panuszewski.template.FadeOutAnimatedVisibility
import dev.panuszewski.template.ResourceImage
import dev.panuszewski.template.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.Text
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.h6
import dev.panuszewski.template.safeGet
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag
import dev.panuszewski.template.withPrimaryColor
import talks.future_of_jvm_build_tools.generated.resources.Res
import talks.future_of_jvm_build_tools.generated.resources.typesafe_conventions

fun StoryboardBuilder.ConventionPlugins() {
    val buildGradleKtsBeforeSplitPane = buildGradleKts.take(6)
    val buildGradleKtsOnSplitPane = buildGradleKts.drop(buildGradleKtsBeforeSplitPane.size - 1).take(4)
    val buildGradleKtsInSmallIde = buildGradleKts.takeLast(4)

    val ideAppears = 1
    val grimacingEmojiVisible = buildGradleKtsBeforeSplitPane.size
    val ideIsShrinkedSince = grimacingEmojiVisible + 2
    val titleChangesToConventionPlugins = ideIsShrinkedSince + 1
    val startExplaining = titleChangesToConventionPlugins + 1
    val bulletpointCount = 3
    val ideIsBackToNormalSince = startExplaining + bulletpointCount

    val buildSrcAdded = ideIsBackToNormalSince + 1
    val srcMainKotlinAdded = buildSrcAdded + 1
    val conventionFileAdded = srcMainKotlinAdded + 1
    val conventionFileEnlarged = conventionFileAdded
    val splitPaneEnabledSince = conventionFileEnlarged + 1
    val fileTreeHiddenSince = splitPaneEnabledSince + 1
    val splitPaneClosedSince = fileTreeHiddenSince + buildGradleKtsOnSplitPane.size
    val fileTreeRevealedSince = splitPaneClosedSince
    val typesafeConventionsAppear = fileTreeRevealedSince + 1
    val typesafeConventionsDisappear = typesafeConventionsAppear + 1

    val ideIsShrinkedAgainSince = typesafeConventionsDisappear + 1

    val ideBackToNormalAgainSince = ideIsShrinkedAgainSince + 3
    val noIdeaEmojiVisible = ideBackToNormalAgainSince + 2
    val ideIsShrinked3Since = noIdeaEmojiVisible + 2
    val ideHiddenSince = ideIsShrinked3Since + 2
    val stateCount = ideHiddenSince + 1

    scene(stateCount) {
        with(transition) {
            val ideTopPadding by animateDp {
                when {
                    it.toState() >= ideIsShrinked3Since -> 275.dp
                    it.toState() >= ideBackToNormalAgainSince -> 150.dp
                    it.toState() >= ideIsShrinkedAgainSince -> 275.dp
                    it.toState() >= typesafeConventionsDisappear -> 0.dp
                    it.toState() >= typesafeConventionsAppear -> 150.dp
                    it.toState() >= ideIsBackToNormalSince -> 0.dp
                    it.toState() >= ideIsShrinkedSince -> 275.dp
                    else -> 0.dp
                }
            }

            val title = when {
                currentState.toState() >= titleChangesToConventionPlugins -> "Convention Plugins"
                currentState.toState() >= ideAppears -> "The biggest issue with Gradle"
                else -> null
            }

            TitleScaffold(title) {
                FadeOutAnimatedVisibility({ it is Frame.State<*> }) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

                        FadeOutAnimatedVisibility({ it.toState() in startExplaining until ideIsBackToNormalSince }) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SlideFromTopAnimatedVisibility({ it.toState() >= startExplaining }) {
                                    h6 {
                                        Text {
                                            append("A ")
                                            withPrimaryColor { append("convention plugin") }
                                            append(" is just a Gradle plugin local to your project")
                                        }
                                    }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= startExplaining + 1 }) {
                                    h6 {
                                        Text {
                                            append("It can be used to encapsulate an ")
                                            withPrimaryColor { append("imperative build logic") }
                                        }
                                    }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= startExplaining + 2 }) {
                                    h6 {
                                        Text {
                                            append("Or share some ")
                                            withPrimaryColor { append("common defaults") }
                                            append(" (like the JVM version)")
                                        }
                                    }
                                }
                            }
                        }

                        FadeOutAnimatedVisibility({ it.toState() in ideIsShrinkedAgainSince until ideHiddenSince }) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SlideFromTopAnimatedVisibility({ it.toState() >= ideIsShrinkedAgainSince + 1 }) {
                                    h6 {
                                        Text {
                                            withPrimaryColor { append("It's cool") }
                                            append(" but... is it enough?")
                                        }
                                    }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= ideIsShrinkedAgainSince + 2 }) {
                                    h6 {
                                        Text {
                                            append("It encourages declarativity, but ")
                                            withPrimaryColor { append("does not enforce it") }
                                        }
                                    }
                                }

                                SlideFromTopAnimatedVisibility({ it.toState() >= ideIsShrinked3Since + 1 }) {
                                    h6 {
                                        Text {
                                            append("Also, conventions often ")
                                            withPrimaryColor { append("mix") }
                                            append(" build logic with shared defaults")
                                        }
                                    }
                                }
                            }
                        }

                        SlideFromBottomAnimatedVisibility({ it.toState() in typesafeConventionsAppear until typesafeConventionsDisappear }) {
                            Box {
                                ResourceImage(
                                    resource = Res.drawable.typesafe_conventions,
                                    modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                )
                            }
                        }

                        Box(contentAlignment = Alignment.Center) {
                            SlideFromBottomAnimatedVisibility({ it.toState() in ideAppears until ideHiddenSince }) {
                                val files = buildList {
                                    addFile(
                                        name = "build.gradle.kts",
                                        content = createChildTransition {
                                            when {
                                                it.toState() < fileTreeHiddenSince -> buildGradleKtsBeforeSplitPane.safeGet(it.toState())
                                                it.toState() < ideBackToNormalAgainSince -> buildGradleKtsOnSplitPane.safeGet(it.toState() - fileTreeHiddenSince)
                                                else -> buildGradleKtsInSmallIde.safeGet(it.toState() - ideBackToNormalAgainSince)
                                            }
                                        }
                                    )
                                    if (currentState.toState() >= buildSrcAdded) {
                                        addDirectory("buildSrc")
                                    }
                                    if (currentState.toState() >= srcMainKotlinAdded) {
                                        addDirectory(name = "src/main/kotlin", path = "buildSrc/src/main/kotlin")
                                    }
                                    if (currentState.toState() >= conventionFileAdded) {
                                        addFile(
                                            name = "wtf-app.gradle.kts",
                                            path = "buildSrc/src/main/kotlin/wtf-app.gradle.kts",
                                            content = createChildTransition { wtfApp.safeGet(it.toState() - fileTreeHiddenSince) }
                                        )
                                    }
                                }

                                val selectedFile = when {
                                    currentState.toState() < splitPaneEnabledSince -> "build.gradle.kts"
                                    currentState.toState() == splitPaneEnabledSince -> "buildSrc/src/main/kotlin/wtf-app.gradle.kts"
                                    currentState.toState() >= splitPaneClosedSince -> "build.gradle.kts"
                                    else -> null
                                }

                                val leftPaneFile = when {
                                    currentState.toState() >= splitPaneClosedSince -> null
                                    currentState.toState() >= splitPaneEnabledSince -> "build.gradle.kts"
                                    else -> null
                                }

                                val rightPaneFile = when {
                                    currentState.toState() >= splitPaneClosedSince -> null
                                    currentState.toState() >= splitPaneEnabledSince -> "buildSrc/src/main/kotlin/wtf-app.gradle.kts"
                                    else -> null
                                }

                                IDE(
                                    IdeState(
                                        files = files,
                                        selectedFile = selectedFile,
                                        leftPaneFile = leftPaneFile,
                                        rightPaneFile = rightPaneFile,
                                        fileTreeHidden = currentState.toState() in fileTreeHiddenSince until fileTreeRevealedSince,
                                    ),
                                    modifier = Modifier.padding(
                                        start = 32.dp,
                                        end = 32.dp,
                                        top = ideTopPadding,
                                        bottom = 32.dp
                                    ),
                                )
                            }

                            FadeInOutAnimatedVisibility({ it.toState() == grimacingEmojiVisible }) {
                                ProvideTextStyle(MaterialTheme.typography.h1) {
                                    Text(text = "😬")
                                }
                            }

                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                                FadeInOutAnimatedVisibility({ it.toState() == noIdeaEmojiVisible }) {
                                    ProvideTextStyle(MaterialTheme.typography.h5) {
                                        Text(
                                            text = """¯\_(ツ)_/¯""",
                                            modifier = Modifier.padding(top = 64.dp, end = 230.dp)
                                        )
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

val buildGradleKts = buildCodeSamples {
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

val wtfApp = buildCodeSamples {
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