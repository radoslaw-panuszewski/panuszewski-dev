package dev.panuszewski.scenes

import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language.KotlinDsl
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.extensions.JustName
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.CenteredColumn
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.StackedImageGallery
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
import dev.panuszewski.template.theme.LocalIdeColors
import dev.panuszewski.template.theme.appendAsCode
import dev.panuszewski.template.theme.appendWithColor
import dev.panuszewski.template.theme.palette
import dev.panuszewski.template.theme.withColor
import talks.purging_chaos_from_your_gradle_build.generated.resources.Res
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_article_1_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_article_2_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_github_issue_light
import talks.purging_chaos_from_your_gradle_build.generated.resources.buildsrc_included_build_gh_issue
import talks.purging_chaos_from_your_gradle_build.generated.resources.gradle8_docs
import talks.purging_chaos_from_your_gradle_build.generated.resources.gradle8_release_notes
import talks.purging_chaos_from_your_gradle_build.generated.resources.gradle_best_practices
import kotlin.math.max

fun StoryboardBuilder.MakingChangesToConventions() {
    val (
        initialState,
        buildSrcAppears,
        complicatedPastAppears,
        complicatedPastBullet1,
        complicatedPastBullet2,
        complicatedPastDisappears,
        yearsAppear,
        year2020Activates,
        firstArticle,
        secondArticle,
        thirdArticle,
        year2020Deactivates,
        year2023Activates,
        buildSrcIncludedBuildGithubIssue,
        gradle8Release,
        gradle8Docs,
        year2023Deactivates,
        year2025Activates,
        gradleBestPractices,
        year2025Deactivates,
        yearsDisappear,
        comparisonAppears,
        comparisonBullet1,
        comparisonBullet2,
        comparisonBullet3,
        comparisonBullet4,
        comparisonBullet5,
        comparisonBullet6,
        comparisonBullet7,
        comparisonBullet8,
        comparisonTopBullet1,
        comparisonTopBullet2,
        comparisonTopBullet3,
        comparisonDisappears,
        treeAppearsAgain,
        appConventionModifiedInBuildSrc,
        buildSrcModified,
        allSubprojectsReconfigured,
        resetAfterBuildSrcChange,
        buildLogicAppears,
        conventionsChangeToNonTypesafe,
        treeWithBuildLogicAppearsAgain,
        buildLogicSplitsIntoSubprojects,
        appConventionModifiedInBuildLogic,
        buildLogicAppModified,
        onlyAppReconfigured,
        resetAfterBuildLogicChange,
        whichToUseStarts,
        terminalAppears,
        t1,
        t2,
        terminalDisappears,
        whichToUseBullet1,
        whichToUseBullet2,
        whichToUseEnds,
        treeIsBackForFinalRecap,
        titleChangesForFinalRecap,
        buildLogicDisappears,
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
        val libColor = palette.blue
        val appColor = MaterialTheme.colors.secondary
        val buildSrcColor = palette.pink
        val buildLogicColor = palette.green
        val highlightColor = palette.orange
        val neutralColor = Color.Gray

        withIntTransition {
            val title = when {
                currentState >= titleChangesForFinalRecap -> "So... is this the ultimate setup?".annotate()
                currentState >= treeIsBackForFinalRecap -> "We stay with build-logic!".annotate()
                currentState >= whichToUseStarts -> "Which one to use?".annotate()
                currentState >= treeWithBuildLogicAppearsAgain -> buildAnnotatedString { append("Making changes: "); withColor(buildLogicColor) { append("build-logic") } }
                currentState >= buildLogicAppears -> buildAnnotatedString { withColor(buildLogicColor) { append("build-logic") } }
                currentState >= treeAppearsAgain -> buildAnnotatedString { append("Making changes: "); withColor(buildSrcColor) { append("buildSrc") } }
                currentState >= comparisonAppears -> "What's the difference?".annotate()
                currentState in year2025Activates until year2025Deactivates -> buildAnnotatedString { append("Gradle Best Practices updated") }
                currentState in year2023Activates until year2025Activates -> buildAnnotatedString { append("Gradle 8 delivers fixes for "); appendWithColor(buildSrcColor, "buildSrc") }
                currentState >= buildSrcAppears -> buildAnnotatedString { withColor(buildSrcColor) { append("buildSrc") } }
                else -> "Making changes to convention plugins".annotate()
            }

            TitleScaffold(title) {

                FadeOutAnimatedVisibility({ it < hideEverything }) {
                    FadeOutAnimatedVisibility({ it in complicatedPastAppears until complicatedPastDisappears }) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SlideFromTopAnimatedVisibility({ it >= complicatedPastBullet1 }) {
                                h6 {
                                    Text {
                                        append("A special directory recognized by Gradle...")
                                    }
                                }
                            }

                            SlideFromTopAnimatedVisibility({ it >= complicatedPastBullet2 }) {
                                h6 {
                                    Text {
                                        append("...with a ")
                                        withColor(buildSrcColor) { append("complicated story") }
                                    }
                                }
                            }
                        }
                    }

                    Column {
                        FadeInOutAnimatedVisibility({ it in yearsAppear until yearsDisappear }) {
                            val highlightColor = palette.blue
                            val badColor = palette.red
                            val goodColor = palette.green

                            val treeYears = when {
                                currentState in year2025Activates until year2025Deactivates -> buildTree { node("2020") { node("2023") { node("2025", highlightColor) } } }
                                currentState in year2023Activates until year2023Deactivates -> buildTree { node("2020") { node("2023", highlightColor) { node("2025") } } }
                                currentState in year2020Activates until year2020Deactivates -> buildTree { node("2020", highlightColor) { node("2023") { node("2025") } } }
                                else -> buildTree { node("2020") { node("2023") { node("2025") } } }
                            }

                            AnimatedHorizontalTree(
                                treeYears,
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier.fillMaxWidth().height(100.dp)
                            ) { node ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(width = 2.dp, color = node.color ?: Color.Gray, shape = RoundedCornerShape(8.dp))
                                        .background(palette.treeNodeBackground)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.animateContentSize(tween(durationMillis = 300, delayMillis = 300))
                                    ) {
                                        createChildTransition {
                                            when {
                                                it in year2025Activates until year2025Deactivates && node.value == "2025" -> buildAnnotatedString {
                                                    withStyle(SpanStyle(color = highlightColor, fontSize = 20.sp)) { append("2025") }
                                                    withStyle(ParagraphStyle(lineHeight = 30.sp)) { append(" \n") }
                                                    append("buildSrc is ")
                                                    appendWithColor(badColor, "bad")
                                                    append(" again")
                                                }
                                                it in year2023Activates until year2023Deactivates && node.value == "2023" -> buildAnnotatedString {
                                                    withStyle(SpanStyle(color = highlightColor, fontSize = 20.sp)) { append("2023") }
                                                    withStyle(ParagraphStyle(lineHeight = 30.sp)) { append(" \n") }
                                                    append("buildSrc is ")
                                                    appendWithColor(goodColor, "good")
                                                    append("?")
                                                }
                                                it in year2020Activates until year2020Deactivates && node.value == "2020" -> buildAnnotatedString {
                                                    withStyle(SpanStyle(color = highlightColor, fontSize = 20.sp)) { append("2020") }
                                                    withStyle(ParagraphStyle(lineHeight = 30.sp)) { append(" \n") }
                                                    append("buildSrc is ")
                                                    appendWithColor(badColor, "bad")
                                                }
                                                else -> JustName(node)
                                            }
                                        }.MagicAnnotatedString(
                                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            split = { it.splitByChars() }
                                        )
                                    }
                                }
                            }
                        }

                        FadeInOutAnimatedVisibility({ it in year2020Activates until year2020Deactivates }) {
                            StackedImageGallery {
                                image(Res.drawable.buildsrc_article_1_light, since = firstArticle)
                                image(Res.drawable.buildsrc_article_2_light, since = secondArticle)
                                image(Res.drawable.buildsrc_github_issue_light, since = thirdArticle)
                            }
                        }

                        FadeInOutAnimatedVisibility({ it in year2023Activates until year2023Deactivates }) {
                            StackedImageGallery {
                                image(Res.drawable.buildsrc_included_build_gh_issue, since = buildSrcIncludedBuildGithubIssue)
                                image(Res.drawable.gradle8_release_notes, since = gradle8Release)
                                image(Res.drawable.gradle8_docs, since = gradle8Docs)
                            }
                        }

                        FadeInOutAnimatedVisibility({ it in year2025Activates until year2025Deactivates }) {
                            StackedImageGallery {
                                image(Res.drawable.gradle_best_practices, since = gradleBestPractices)
                            }
                        }
                    }

                    SlideFromBottomAnimatedVisibility({ it in terminalAppears until terminalDisappears }) {
                        val terminalTexts = listOf(
                            "$ gradle init \\\n\t\t--type kotlin-application \\\n\t\t--incubating \\\n\t\t--dsl kotlin \\\n\t\t--split-project \\\n\t\t--java-version 25 \\\n\t\t--project-name example-project \\\n\t\t&& tree .".annotate(),
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
                                withColor(palette.orange) {
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

                                CenteredColumn(modifier = Modifier.fillMaxWidth()) {
                                    RevealSequentially(since = comparisonTopBullet1) {
                                        annotatedStringItem {
                                            append("The ")
                                            withColor(palette.orange) { append("buildscript's classpath") }
                                            append(" is so important, because...")
                                        }
                                        annotatedStringItem {
                                            append("...when it changes, all non-builtin tasks will go out-of-date")
                                        }
                                        annotatedStringItem {
                                            append("It means that ")
                                            appendAsCode("compileJava")
                                            append(" will stay UP-TO-DATE, but ")
                                            appendAsCode("bootJar")
                                            append(" will not")
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
                                            .border(2.dp, buildSrcColor)
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
                                            stringItem("$BULLET_1 recognized out-of-the-box")
                                        }

                                        RevealSequentially(since = comparisonBullet3, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 typesafe accessors for conventions")
                                        }

                                        RevealSequentially(since = comparisonBullet5, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 sometimes included build, sometimes not")
                                        }

                                        RevealSequentially(since = comparisonBullet7, textStyle = MaterialTheme.typography.body1) {
                                            annotatedStringItem { appendWithColor(palette.orange, "$BULLET_1 ALWAYS on buildscript's classpath") }
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .border(2.dp, buildLogicColor)
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

                                        RevealSequentially(since = comparisonBullet2, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 needs manual registration")
                                        }

                                        RevealSequentially(since = comparisonBullet4, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 no typesafe accessors for conventions")
                                        }

                                        RevealSequentially(since = comparisonBullet6, textStyle = MaterialTheme.typography.body1) {
                                            stringItem("$BULLET_1 behaves 100% as external dependency")
                                        }

                                        RevealSequentially(since = comparisonBullet8, textStyle = MaterialTheme.typography.body1) {
                                            annotatedStringItem { appendWithColor(palette.orange, "$BULLET_1 ONLY on buildscript's classpath if used") }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    FadeInOutAnimatedVisibility({ it in whichToUseStarts until whichToUseEnds }) {
                        CenteredColumn(Modifier.fillMaxWidth()) {
                            RevealSequentially(since = whichToUseBullet1) {
                                annotatedStringItem {
                                    append("According to the official guidelines, we should be using ")
                                    appendWithColor(buildLogicColor, "build-logic")
                                }
                                annotatedStringItem {
                                    append("The behavior of ")
                                    appendWithColor(buildSrcColor, "buildSrc")
                                    append(" is unpredictable, and its future unclear")
                                }
                            }
                        }
                    }

                    SlideFromBottomAnimatedVisibility({
                        it !in complicatedPastAppears..complicatedPastDisappears
                                && it !in yearsAppear..yearsDisappear
                                && it !in terminalAppears..terminalDisappears
                                && it !in comparisonAppears..comparisonDisappears
                                && it !in whichToUseStarts..whichToUseEnds
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
                                                withColor(palette.orange) { append("do not enforce it") }
                                            }
                                        }
                                        RevealSequentially(since = finalRecapBullet2) {
                                            annotatedStringItem {
                                                append("Also, they often mix ")
                                                withColor(palette.blue) { append("shared defaults") }
                                                append(" with ")
                                                withColor(palette.pink) { append("custom build logic") }
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
                                    currentState >= buildLogicDisappears -> buildTree {
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
                                            .border(width = 2.dp, color = node.color ?: Color.Gray, shape = RoundedCornerShape(8.dp))
                                            .background(palette.treeNodeBackground)
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
                                                            .toCodeSample(language = KotlinDsl)
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
                                                            .toCodeSample(language = KotlinDsl)
                                                            .focus(customBuildLogic, focusedStyle = { SpanStyle(color = palette.pink) })
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
                                                            .toCodeSample(language = KotlinDsl)
                                                            .focus(sharedDefaults, focusedStyle = { SpanStyle(color = palette.blue) })
                                                    }.String()
                                                    it >= appConventionExpanded && node.value == "app-convention" -> buildCodeSamples {
                                                        """
                                                        plugins {
                                                            alias(libs.plugins.kotlin.jvm)
                                                        }
                                                        """
                                                            .trimIndent()
                                                            .toCodeSample(language = KotlinDsl)
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
                                                            .toCodeSample(language = KotlinDsl)
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
                                                        .toCode(language = KotlinDsl)
                                                    it >= libProjectsFolded && node.value.matches("""lib\d+""".toRegex()) -> JustName(node)
                                                    it >= conventionsChangeToNonTypesafe && node.value.matches("""lib\d+""".toRegex()) -> """
                                                        plugins {
                                                            id("lib-convention")
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = KotlinDsl)
                                                    it >= conventionsChangeToNonTypesafe && node.value == "app" -> """
                                                        plugins {
                                                            id("app-convention")
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = KotlinDsl)
                                                    it >= initialState && node.value.matches("""lib\d+""".toRegex()) -> """
                                                        plugins {
                                                            `lib-convention`
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = KotlinDsl)
                                                    it >= initialState && node.value == "app" -> """
                                                        plugins {
                                                            `app-convention`
                                                        }
                                                        """
                                                        .trimIndent()
                                                        .toCode(language = KotlinDsl)
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
                            ProvideTextStyle(MaterialTheme.typography.h5.copy(color = palette.orange)) {
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
