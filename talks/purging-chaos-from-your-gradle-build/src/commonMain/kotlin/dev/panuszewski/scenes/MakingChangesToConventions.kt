package dev.panuszewski.scenes

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.magic.splitByChars
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.template.components.ResourceImage
import dev.panuszewski.template.components.RevealSequentially
import dev.panuszewski.template.components.Terminal
import dev.panuszewski.template.components.TitleScaffold
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.FadeOutAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.SlideFromTopAnimatedVisibility
import dev.panuszewski.template.extensions.Text
import dev.panuszewski.template.extensions.annotate
import dev.panuszewski.template.extensions.h6
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.withIntTransition
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
        appConventionAppears,
        subprojectsExpand,
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
        buildLogicSplitsIntoSubprojects,
        appConventionModifiedInBuildLogic,
        buildLogicAppModified,
        onlyAppReconfigured,
        resetAfterBuildLogicChange,
    ) = subsequentNumbers()

    val totalStates = resetAfterBuildLogicChange + 1

    scene(totalStates) {
        val rootColor = MaterialTheme.colors.primary
        val libColor = NICE_BLUE
        val appColor = MaterialTheme.colors.secondary
        val buildSrcColor = NICE_PINK
        val buildLogicColor = NICE_GREEN
        val highlightColor = NICE_ORANGE
        val neutralColor = Color.Gray
        val textAccentColor = NICE_PINK

        withIntTransition {
            val title = when {
                currentState >= buildLogicAppears -> buildAnnotatedString { withColor(buildLogicColor) { append("build-logic") } }
                currentState >= treeAppearsAgain -> buildAnnotatedString { append("Making changes: "); withColor(buildSrcColor) { append("buildSrc") } }
                currentState >= buildSrcAppears -> buildAnnotatedString { withColor(buildSrcColor) { append("buildSrc") } }
                else -> "Making changes to convention plugins".annotate()
            }

            TitleScaffold(title) {

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
                                    withColor(textAccentColor) { append("very bad reputation") }
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
                                append("Since newer Gradle versions, it's ")
                                withColor(textAccentColor) { append("not that bad") }
                            }
                            annotatedStringItem {
                                append("Now it behaves like an ")
                                withColor(textAccentColor) { append("included build") }
                                append(" (most of the time)")
                            }
                            annotatedStringItem {
                                append("Modifying code in buildSrc no longer invalidates everything")
                            }
                            annotatedStringItem {
                                append("However...")
                            }
                            annotatedStringItem {
                                append("It's still ")
                                withColor(textAccentColor) { append("always on classpath") }
                                append(" of every buildscript in the parent build")
                            }
                        }
                    }
                }

                SlideFromBottomAnimatedVisibility({ it in terminalAppears until terminalDisappears }) {
                    val terminalTexts = listOf(
                        "$ gradle init \\\n\t\t--type kotlin-application \\\n\t\t--incubating \\\n\t\t--dsl kotlin \\\n\t\t--split-project \\\n\t\t--java-version 25 \\\n\t\t--project-name example-project",
                        "> Task :init\nBUILD SUCCESSFUL",
                        "$ tree .",
                        """
                        .
                        ├── build.gradle.kts
                        ├── settings.gradle.kts
                        ├── app
                        │   ├── build.gradle.kts
                        │   └── src
                        └── build-logic
                            ├── build.gradle.kts
                            ├── settings.gradle.kts
                            └── src
                        """.trimIndent()
                    )
                    Terminal(
                        textsToDisplay = terminalTexts.take(max(0, currentState - terminalAppears)),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 180.dp, vertical = 32.dp)
                    )
                }

                SlideFromBottomAnimatedVisibility({ it !in badReputationAppears..bulletpointsDisappear }) {
                    val tree = when {
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
                        currentState >= appConventionAppears -> buildTree {
                            val appConvention = reusableNode("app-convention", appColor)
                            val libConvention = reusableNode("lib-convention", libColor)

                            node("root-project", rootColor) {
                                node("app", appColor) { node(appConvention) }
                                node("lib1", libColor) { node(libConvention) }
                                node("lib2", libColor) { node(libConvention) }
                            }
                        }
                        currentState >= initialState -> buildTree {
                            val libConvention = reusableNode("lib-convention", libColor)

                            node("root-project", rootColor) {
                                node("app", appColor)
                                node("lib1", libColor) { node(libConvention) }
                                node("lib2", libColor) { node(libConvention) }
                            }
                        }
                        else -> emptyList()
                    }

                    AnimatedHorizontalTree(tree) { node ->
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
                                        it >= conventionsChangeToNonTypesafe && node.value.matches("""lib\d+""".toRegex()) -> buildAnnotatedString {
                                            appendLine("plugins {")
                                            append("    id(")
                                            withColor(LocalCodeStyle.current.string.color) { append("\"lib-convention\"") }
                                            appendLine(")")
                                            append("}")
                                        }
                                        it >= conventionsChangeToNonTypesafe && node.value == "app" -> buildAnnotatedString {
                                            appendLine("plugins {")
                                            append("    id(")
                                            withColor(LocalCodeStyle.current.string.color) { append("\"app-convention\"") }
                                            appendLine(")")
                                            append("}")
                                        }
                                        it >= subprojectsExpand && node.value.matches("""lib\d+""".toRegex()) -> buildAnnotatedString {
                                            appendLine("plugins {")
                                            withColor(libColor) { appendLine("    `lib-convention`") }
                                            append("}")
                                        }
                                        it >= subprojectsExpand && node.value == "app" -> buildAnnotatedString {
                                            appendLine("plugins {")
                                            withColor(appColor) { appendLine("    `app-convention`") }
                                            append("}")
                                        }
                                        else -> buildAnnotatedString {
                                            withColor(Color.White) { append(node.value) }
                                        }
                                    }
                                }.MagicAnnotatedString(Modifier.padding(8.dp), split = { it.splitByChars() })
                            }
                        }
                    }
                }
            }
        }
    }
}