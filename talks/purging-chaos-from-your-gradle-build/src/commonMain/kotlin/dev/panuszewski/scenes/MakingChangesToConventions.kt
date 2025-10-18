package dev.panuszewski.scenes

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.extensions.JustName
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.TerminalWithAnnotations
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.FadeInOutAnimatedVisibility
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h5
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.toCode
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.BULLET_1
import dev.panuszewski.template.theme.LocalCodeStyle
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.NICE_BLUE
import dev.panuszewski.template.theme.NICE_GREEN
import dev.panuszewski.template.theme.NICE_ORANGE
import dev.panuszewski.template.theme.NICE_PINK
import dev.panuszewski.template.theme.withColor
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_article_1_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_article_2_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_github_issue_light
import kotlin.math.max

fun StoryboardBuilder.MakingChangesToConventions() {
    val (
        initialState,
        buildSrcAppears,
        badReputationAppears,
        badReputationBullet1,
        badReputationBullet2,
        image1,
        image2,
        image3,
        badReputationDisappears,
        terminalAppears,
        t1,
        t2,
        t3,
        t4,
        terminalDisappears,
        bulletpoint1,
        bulletpoint2,
        bulletpoint3,
        bulletpoint4,
        bulletpoint5,
        bulletpointsDisappear,
        treeAppearsAgain,
        appConventionModifiedInBuildSrc,
        buildSrcModified,
        allSubprojectsReconfigured,
        resetAfterBuildSrcChange,
        buildLogicAppears,
        conventionsChangeToNonTypesafe,
        buildLogicBulletpointsAppear,
        blBulletpoint1,
        blBulletpoint2,
        blBulletpoint3,
        blBulletpoint4,
        blBulletpoint5,
        buildLogicBulletpointsDisappear,
        treeWithBuildLogicAppearsAgain,
        buildLogicSplitsIntoSubprojects,
        appConventionModifiedInBuildLogic,
        buildLogicAppModified,
        onlyAppReconfigured,
        resetAfterBuildLogicChange,
        comparisonAppears,
        comparisonBullet1,
        comparisonBullet2,
        comparisonBullet3,
        comparisonBullet4,
        comparisonBullet5,
        comparisonBullet6,
        comparisonBullet7,
        comparisonDisappears,
        treeIsBackForFinalRecap,
        titleChangesForFinalRecap,
        finalRecapBullet1,
        libProjectsFolded,
        randomCodeAddedToAppProject,
        shrugEmoji,
        resetAfterRandomCode,
        finalRecapBullet2,
        showAppConvention,
        appConventionExpanded,
        sharedDefaultsAdded,
        customBuildLogicAdded,
        resetAfterMixedConcerns,
        hideEverything,
        totalStates,
    ) = subsequentNumbers()

    scene(totalStates) {
        val rootColor = MaterialTheme.colors.primary
        val libColor = NICE_BLUE
        val appColor = MaterialTheme.colors.secondary
        val buildSrcColor = NICE_PINK
        val buildLogicColor = NICE_GREEN
        val highlightColor = NICE_ORANGE
        val neutralColor = Color.Gray

        withIntTransition {
            val title = when {
                currentState >= titleChangesForFinalRecap -> "Is this the ultimate setup?".annotate()
                currentState >= comparisonAppears -> "When to use which?".annotate()
                currentState >= treeWithBuildLogicAppearsAgain -> buildAnnotatedString { append("Making changes: "); withColor(buildLogicColor) { append("build-logic") } }
                currentState >= buildLogicAppears -> buildAnnotatedString { withColor(buildLogicColor) { append("build-logic") } }
                currentState >= treeAppearsAgain -> buildAnnotatedString { append("Making changes: "); withColor(buildSrcColor) { append("buildSrc") } }
                currentState >= buildSrcAppears -> buildAnnotatedString { withColor(buildSrcColor) { append("buildSrc") } }
                else -> "Making changes to convention plugins".annotate()
            }

            TitleScaffold(title) {

                FadeOutAnimatedVisibility({ it < hideEverything }) {
                    FadeOutAnimatedVisibility({ it in badReputationAppears until badReputationDisappears }) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SlideFromTopAnimatedVisibility({ it >= badReputationBullet1 }) {
                                h6 {
                                    Text {
                                        append("A special directory recognized by Gradle...")
                                    }
                                }
                            }

                            SlideFromTopAnimatedVisibility({ it >= badReputationBullet2 }) {
                                h6 {
                                    Text {
                                        append("...with a ")
                                        withColor(buildSrcColor) { append("very bad reputation") }
                                    }
                                }
                            }

                            Box(
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                SlideFromBottomAnimatedVisibility({ it >= image1 }) {
                                    val shift by animateDp({ tween(300) }) { if (it >= image3) 64.dp else if (it >= image2) 32.dp else 0.dp }
                                    val tint by animateFloat({ tween(300) }) { if (it >= image3) 0.66f else if (it >= image2) 0.33f else 0.01f }
                                    ResourceImage(
                                        Res.drawable.buildsrc_article_1_light,
                                        modifier = Modifier.padding(top = 0.dp, end = shift).width(500.dp).padding(end = shift).border(0.5.dp, Color.Gray),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                                    )
                                }
                                SlideFromBottomAnimatedVisibility({ it >= image2 }) {
                                    val shift by animateDp({ tween(300) }) { if (it >= image3) 32.dp else 0.dp }
                                    val tint by animateFloat({ tween(300) }) { if (it >= image3) 0.33f else 0.01f }
                                    ResourceImage(
                                        Res.drawable.buildsrc_article_2_light,
                                        modifier = Modifier.padding(top = 16.dp, end = shift).width(500.dp).padding(end = shift).border(0.5.dp, Color.Gray),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(Color.Black.copy(alpha = tint), blendMode = BlendMode.Darken)
                                    )
                                }
                                SlideFromBottomAnimatedVisibility({ it >= image3 }) {
                                    ResourceImage(
                                        Res.drawable.buildsrc_github_issue_light,
                                        modifier = Modifier.padding(top = 32.dp, end = 0.dp).width(500.dp).border(0.5.dp, Color.Gray),
                                        contentScale = ContentScale.FillWidth,
                                    )
                                }
                            }
                        }
                    }

                    FadeOutAnimatedVisibility({ it in bulletpoint1 - 1 until bulletpointsDisappear }) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RevealSequentially(since = bulletpoint1) {
                                annotatedStringItem {
                                    append("In recent Gradle versions, it's ")
                                    withColor(buildSrcColor) { append("not that bad") }
                                }
                                annotatedStringItem {
                                    append("Now it behaves like an ")
                                    withColor(buildSrcColor) { append("included build") }
                                    append(" (most of the time)")
                                }
                                annotatedStringItem {
                                    append("Modifying code in buildSrc will no longer invalidate everything")
                                }
                                annotatedStringItem {
                                    append("However...")
                                }
                                annotatedStringItem {
                                    append("It's ")
                                    withColor(buildSrcColor) { append("on classpath") }
                                    append(" of every buildscript (regardless if it's used or not)")
                                }
                            }
                        }
                    }

                    FadeOutAnimatedVisibility({ it in buildLogicBulletpointsAppear until buildLogicBulletpointsDisappear }) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RevealSequentially(since = blBulletpoint1) {
                                annotatedStringItem {
                                    append("A regular Gradle ")
                                    withColor(buildLogicColor) { append("included build") }
                                    append(", can be of any name")
                                }
                                annotatedStringItem {
                                    append("Requires manual registration via ")
                                    withColor(buildLogicColor) { append("includeBuild(...)") }
                                }
                                annotatedStringItem {
                                    withColor(buildLogicColor) { append("Doesn't provide ") }
                                    append("typesafe accessors")
                                    append(" for convention plugins")
                                }
                                annotatedStringItem {
                                    append("Only on buildscript's classpath if it's ")
                                    withColor(buildLogicColor) { append("actually used") }
                                }
                                annotatedStringItem {
                                    append("Allows to ")
                                    withColor(buildLogicColor) { append("isolate") }
                                    append(" your conventions")
                                }
                            }
                        }
                    }

                    SlideFromBottomAnimatedVisibility({ it in terminalAppears until terminalDisappears }) {
                        val terminalTexts = listOf(
                            "$ gradle init \\\n\t\t--type kotlin-application \\\n\t\t--incubating \\\n\t\t--dsl kotlin \\\n\t\t--split-project \\\n\t\t--java-version 25 \\\n\t\t--project-name example-project".annotate(),
                            "> Task :init\nBUILD SUCCESSFUL".annotate(),
                            "$ tree .".annotate(),
                            buildAnnotatedString {
                                appendLine(
                                    """
                                    .
                                    ├── build.gradle.kts
                                    ├── settings.gradle.kts
                                    ├── app
                                    │   ├── build.gradle.kts
                                    │   └── src
                                    """.trimIndent()
                                )
                                withColor(NICE_ORANGE) {
                                    append(
                                        """
                                        └── build-logic
                                            ├── build.gradle.kts
                                            ├── settings.gradle.kts
                                            └── src
                                        """.trimIndent()
                                    )
                                }
                            }
                        )
                        TerminalWithAnnotations(
                            textsToDisplay = terminalTexts.take(max(0, currentState - terminalAppears)),
                            bottomSpacerHeight = 120.dp,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 180.dp, vertical = 32.dp)
                        )
                    }

                    SlideFromBottomAnimatedVisibility({ it in comparisonAppears until comparisonDisappears }) {

                        Column {
                            LookaheadScope {
                                Spacer(Modifier.height(16.dp))

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    RevealSequentially(since = comparisonBullet6) {
                                        annotatedStringItem {
                                            append("As a rule of thumb, I always start a new project with ")
                                            withColor(buildSrcColor) { append("buildSrc") }
                                        }
                                        annotatedStringItem {
                                            append("Then I switch to ")
                                            withColor(buildLogicColor) { append("build-logic") }
                                            append(" only if I see the performance gain")
                                        }
                                    }
                                }

                                Spacer(Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxSize().padding(top = 32.dp).animateBounds(this),
                                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .border(1.dp, buildSrcColor)
                                            .background(color = buildSrcColor.copy(alpha = 0.03f))
                                            .fillMaxWidth()
                                            .weight(0.5f)
                                            .padding(16.dp)
                                            .animateContentSize(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        h5 { Text(Modifier.align(Alignment.CenterHorizontally)) { withColor(buildSrcColor) { append("buildSrc") } } }

                                        Spacer(Modifier.height(8.dp))

                                        RevealSequentially(since = comparisonBullet1, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 easy setup, works out-of-the-box")
                                            stringItem("$BULLET_1 typesafe accessors for conventions")
                                            stringItem("$BULLET_1 ideal for helper functions")
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .border(1.dp, buildLogicColor)
                                            .background(color = buildLogicColor.copy(alpha = 0.03f))
                                            .fillMaxWidth()
                                            .weight(0.5f)
                                            .padding(16.dp)
                                            .animateContentSize(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        h5 { Text(Modifier.align(Alignment.CenterHorizontally)) { withColor(buildLogicColor) { append("build-logic") } } }

                                        Spacer(Modifier.height(8.dp))

                                        RevealSequentially(since = comparisonBullet4, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 better performance")
                                            stringItem("$BULLET_1 well-defined behavior")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SlideFromBottomAnimatedVisibility({
                        it !in badReputationAppears..bulletpointsDisappear
                                && it !in buildLogicBulletpointsAppear..buildLogicBulletpointsDisappear
                                && it !in comparisonAppears..comparisonDisappears
                    }) {
                        Column {
                            LookaheadScope {
                                FadeOutAnimatedVisibility({ it >= titleChangesForFinalRecap }) {
                                    Column(
                                        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(bottom = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        RevealSequentially(since = finalRecapBullet1) {
                                            annotatedStringItem {
                                                append("Convention plugins encourages declarativity, but ")
                                                withColor(NICE_ORANGE) { append("do not enforce it") }
                                            }
                                        }
                                        RevealSequentially(since = finalRecapBullet2) {
                                            annotatedStringItem {
                                                append("Also, they often mix ")
                                                withColor(NICE_BLUE) { append("shared defaults") }
                                                append(" with ")
                                                withColor(NICE_PINK) { append("custom build logic") }
                                            }
                                        }
                                    }
                                }

                                val tree = when {
                                    currentState >= showAppConvention -> buildTree {
                                        node("root-project", rootColor) {
                                            node("app", appColor) {
                                                node("app-convention", appColor)
                                            }
                                        }
                                    }
                                    currentState >= libProjectsFolded -> buildTree {
                                        node("root-project", rootColor) {
                                            node("app", appColor)
                                        }
                                    }
                                    currentState >= treeIsBackForFinalRecap -> buildTree {
                                        node("root-project", rootColor) {
                                            node("app", appColor)
                                            node("lib1", libColor)
                                            node("lib2", libColor)
                                        }
                                    }
                                    currentState >= resetAfterBuildLogicChange -> buildTree {
                                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                                            node("app-convention", buildLogicColor)
                                        }
                                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildLogicApp) }
                                            node("lib1", libColor) { node(buildLogicLib) }
                                            node("lib2", libColor) { node(buildLogicLib) }
                                        }
                                    }
                                    currentState >= onlyAppReconfigured -> buildTree {
                                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                                            node("app-convention", highlightColor)
                                        }
                                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", highlightColor) { node(buildLogicApp) }
                                            node("lib1", libColor) { node(buildLogicLib) }
                                            node("lib2", libColor) { node(buildLogicLib) }
                                        }
                                    }
                                    currentState >= buildLogicAppModified -> buildTree {
                                        val buildLogicApp = reusableNode(":build-logic:app", highlightColor) {
                                            node("app-convention", highlightColor)
                                        }
                                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildLogicApp) }
                                            node("lib1", libColor) { node(buildLogicLib) }
                                            node("lib2", libColor) { node(buildLogicLib) }
                                        }
                                    }
                                    currentState >= appConventionModifiedInBuildLogic -> buildTree {
                                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                                            node("app-convention", highlightColor)
                                        }
                                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildLogicApp) }
                                            node("lib1", libColor) { node(buildLogicLib) }
                                            node("lib2", libColor) { node(buildLogicLib) }
                                        }
                                    }
                                    currentState >= buildLogicSplitsIntoSubprojects -> buildTree {
                                        val buildLogicApp = reusableNode(":build-logic:app", buildLogicColor) {
                                            node("app-convention", buildLogicColor)
                                        }
                                        val buildLogicLib = reusableNode(":build-logic:lib", buildLogicColor) {
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildLogicApp) }
                                            node("lib1", libColor) { node(buildLogicLib) }
                                            node("lib2", libColor) { node(buildLogicLib) }
                                        }
                                    }
                                    currentState >= buildLogicAppears -> buildTree {
                                        val buildLogic = reusableNode("build-logic", buildLogicColor) {
                                            node("app-convention", buildLogicColor)
                                            node("lib-convention", buildLogicColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildLogic) }
                                            node("lib1", libColor) { node(buildLogic) }
                                            node("lib2", libColor) { node(buildLogic) }
                                        }
                                    }
                                    currentState >= resetAfterBuildSrcChange -> buildTree {
                                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                                            node("app-convention", buildSrcColor)
                                            node("lib-convention", buildSrcColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildSrc) }
                                            node("lib1", libColor) { node(buildSrc) }
                                            node("lib2", libColor) { node(buildSrc) }
                                        }
                                    }
                                    currentState >= allSubprojectsReconfigured -> buildTree {
                                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                                            node("app-convention", highlightColor)
                                            node("lib-convention", buildSrcColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", highlightColor) { node(buildSrc) }
                                            node("lib1", highlightColor) { node(buildSrc) }
                                            node("lib2", highlightColor) { node(buildSrc) }
                                        }
                                    }
                                    currentState >= buildSrcModified -> buildTree {
                                        val buildSrc = reusableNode("buildSrc", highlightColor) {
                                            node("app-convention", highlightColor)
                                            node("lib-convention", buildSrcColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildSrc) }
                                            node("lib1", libColor) { node(buildSrc) }
                                            node("lib2", libColor) { node(buildSrc) }
                                        }
                                    }
                                    currentState >= appConventionModifiedInBuildSrc -> buildTree {
                                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                                            node("app-convention", highlightColor)
                                            node("lib-convention", buildSrcColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildSrc) }
                                            node("lib1", libColor) { node(buildSrc) }
                                            node("lib2", libColor) { node(buildSrc) }
                                        }
                                    }
                                    currentState >= buildSrcAppears -> buildTree {
                                        val buildSrc = reusableNode("buildSrc", buildSrcColor) {
                                            node("app-convention", buildSrcColor)
                                            node("lib-convention", buildSrcColor)
                                        }
                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(buildSrc) }
                                            node("lib1", libColor) { node(buildSrc) }
                                            node("lib2", libColor) { node(buildSrc) }
                                        }
                                    }
                                    currentState >= initialState -> buildTree {
                                        val appConvention = reusableNode("app-convention", appColor)
                                        val libConvention = reusableNode("lib-convention", libColor)

                                        node("root-project", rootColor) {
                                            node("app", appColor) { node(appConvention) }
                                            node("lib1", libColor) { node(libConvention) }
                                            node("lib2", libColor) { node(libConvention) }
                                        }
                                    }
                                    else -> emptyList()
                                }
                                AnimatedHorizontalTree(tree, modifier = Modifier.animateBounds(this)) { node ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(width = 2.dp, color = node.color ?: Color.Unspecified, shape = RoundedCornerShape(8.dp))
                                            .background(LocalIdeColors.current.paneBackground)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.animateContentSize(tween(durationMillis = 300, delayMillis = 300))
                                        ) {
                                            createChildTransition {
                                                when {
                                                    it >= resetAfterMixedConcerns && node.value == "app-convention" -> buildCodeSamples {
                                                        """
                                                        plugins {
                                                            alias(libs.plugins.kotlin.jvm)
                                                        }
                                                        kotlin {
                                                            jvmToolchain(25)
                                                        }
                                                        tasks.register("customTask") {
                                                            doLast { println("lol") }
                                                        }
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = Language.KotlinDsl)
                                                    }.String()
                                                    it >= customBuildLogicAdded && node.value == "app-convention" -> buildCodeSamples {
                                                        val sharedDefaults by tag()
                                                        val customBuildLogic by tag()
                                                        """
                                                        plugins {
                                                            alias(libs.plugins.kotlin.jvm)
                                                        }
                                                        ${sharedDefaults}kotlin {
                                                            jvmToolchain(25)
                                                        }${sharedDefaults}
                                                        ${customBuildLogic}tasks.register("customTask") {
                                                            doLast { println("lol") }
                                                        }${customBuildLogic}
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = Language.KotlinDsl)
                                                            .focus(customBuildLogic, focusedStyle = SpanStyle(color = NICE_PINK))
                                                    }.String()
                                                    it >= sharedDefaultsAdded && node.value == "app-convention" -> buildCodeSamples {
                                                        val sharedDefaults by tag()
                                                        """
                                                        plugins {
                                                            alias(libs.plugins.kotlin.jvm)
                                                        }
                                                        ${sharedDefaults}kotlin {
                                                            jvmToolchain(25)
                                                        }${sharedDefaults}
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = Language.KotlinDsl)
                                                            .focus(sharedDefaults, focusedStyle = SpanStyle(color = NICE_BLUE))
                                                    }.String()
                                                    it >= appConventionExpanded && node.value == "app-convention" -> buildCodeSamples {
                                                        """
                                                        plugins {
                                                            alias(libs.plugins.kotlin.jvm)
                                                        }
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = Language.KotlinDsl)
                                                    }.String()
                                                    it >= resetAfterRandomCode && node.value == "app" -> JustName(node)
                                                    it >= randomCodeAddedToAppProject && node.value == "app" -> buildCodeSamples {
                                                        val randomCode by tag()
                                                        """
                                                        plugins {
                                                            id("app-convention")
                                                        }
                                                        dependencies {
                                                            implementation(libs.spring.boot.web)
                                                        }${randomCode}
                                                        for (project in subprojects) {
                                                            project.apply(plugin = "kotlin")
                                                        }${randomCode}
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = Language.KotlinDsl)
                                                            .focus(randomCode)
                                                    }.String()
                                                    it >= libProjectsFolded && node.value == "app" -> """
                                                        plugins {
                                                            id("app-convention")
                                                        }
                                                        dependencies {
                                                            implementation(libs.spring.boot.web)
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = Language.KotlinDsl)
                                                    it >= libProjectsFolded && node.value.matches("""lib\d+""".toRegex()) -> JustName(node)
                                                    it >= conventionsChangeToNonTypesafe && node.value.matches("""lib\d+""".toRegex()) -> """
                                                        plugins {
                                                            id("lib-convention")
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = Language.KotlinDsl)
                                                    it >= conventionsChangeToNonTypesafe && node.value == "app" -> """
                                                        plugins {
                                                            id("app-convention")
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = Language.KotlinDsl)
                                                    it >= initialState && node.value.matches("""lib\d+""".toRegex()) -> """
                                                        plugins {
                                                            `lib-convention`
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = Language.KotlinDsl)
                                                    it >= initialState && node.value == "app" -> """
                                                        plugins {
                                                            `app-convention`
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = Language.KotlinDsl)
                                                    else -> JustName(node)
                                                }
                                            }.MagicAnnotatedString(Modifier.padding(8.dp), split = { it.splitByChars() })
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        FadeInOutAnimatedVisibility({ it == shrugEmoji }) {
                            ProvideTextStyle(MaterialTheme.typography.h5.copy(color = NICE_ORANGE)) {
                                androidx.compose.material.Text(
                                    text = """¯\_(ツ)_/¯""",
                                    modifier = Modifier.padding(top = 35.dp, end = 50.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
