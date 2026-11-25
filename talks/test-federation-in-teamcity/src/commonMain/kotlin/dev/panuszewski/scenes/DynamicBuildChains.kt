package dev.panuszewski.scenes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.panuszewski.template.extensions.splitByChars
import dev.panuszewski.components.allTests
import dev.panuszewski.components.contractTests
import dev.panuszewski.components.smokeTests
import dev.panuszewski.template.components.AnimatedHorizontalTree
import dev.panuszewski.template.components.ArrowDirection.FORWARD
import dev.panuszewski.template.components.IdeLayout
import dev.panuszewski.template.components.JustName
import dev.panuszewski.template.components.MagicAnnotatedString
import dev.panuszewski.components.SectionScaffold
import dev.panuszewski.template.components.buildCodeSamples
import dev.panuszewski.template.components.buildIdeState
import dev.panuszewski.template.components.buildTree
import dev.panuszewski.template.extensions.SlideFromBottomAnimatedVisibility
import dev.panuszewski.template.extensions.startWith
import dev.panuszewski.template.extensions.subsequentNumbers
import dev.panuszewski.template.extensions.tag
import dev.panuszewski.template.extensions.withIntTransition
import dev.panuszewski.template.theme.LIGHT_PALETTE
import dev.panuszewski.template.theme.palette

fun StoryboardBuilder.DynamicBuildChains() {
    val (
        ideAppears,
        ideFolds,
        staticModeFocuses,
        staticModeUnfocuses,
        finalBuildConfigExpands,
        finalBuildConfigFolds,
        generatorBuildConfigExpands,
        dependenciesAppear,
        dynamicModeUnfocuses,
        ideHides,
        buildsRename,
        variantsAppear,
        generatorAppears,
        perVariantGeneratorsAppear,
        optimizationAppears,
        manyGeneratorsAppear,
        manyGeneratorsDisappear,
        removeWrappers,
        makeVariantsReal,
        selectorAppears,
        selectorSelectsVariants,
        notSelectedItemsAppear,
        selectionChanges,
        selectionChangesAgain,
        tableDisappears,
        lastState
    ) = subsequentNumbers()

    val treeAppears = staticModeFocuses
    val generatorBuildConfigFolds = dynamicModeUnfocuses

    scene(stateCount = lastState) {
        val palette = palette

        withIntTransition {
            SectionScaffold("Dynamic Build Chains") {
                val ideState = buildIdeState("settings.kts" to PROJECT_CONFIG)

                ideState.IdeLayout {
                    adaptiveTopPanel("tree") {
                        Box(Modifier.height(if (currentState >= ideHides) 400.dp else 166.dp)) {
                            val tree = when {
                                currentState >= selectionChangesAgain -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("TestFederationSelector", color = palette.purple, arrow = FORWARD) {
                                            node(smokeTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                            node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                        }
                                    }

                                    node(allTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(contractTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(allTests("Bar"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(contractTests("Bar"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                }

                                currentState >= selectionChanges -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("TestFederationSelector", color = palette.purple, arrow = FORWARD) {
                                            node(allTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                            node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                        }
                                    }

                                    node(smokeTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(contractTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(allTests("Bar"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(contractTests("Bar"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                }

                                currentState >= notSelectedItemsAppear -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("TestFederationSelector", color = palette.purple, arrow = FORWARD) {
                                            node(allTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                            node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                            node(contractTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                        }
                                    }

                                    node(smokeTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(contractTests("Foo"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                    node(allTests("Bar"), arrow = FORWARD, color = palette.gray) { node(finalBuild) }
                                }

                                currentState >= selectorSelectsVariants -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("TestFederationSelector", color = palette.purple, arrow = FORWARD) {
                                            node(allTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                            node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                            node(contractTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                        }
                                    }
                                }

                                currentState >= selectorAppears -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("TestFederationSelector", color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                    }

                                    node(allTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                    node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                    node(contractTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                }

                                currentState >= makeVariantsReal -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node("...", color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                    }

                                    node(allTests("Foo"), arrow = FORWARD) { node(finalBuild) }
                                    node(smokeTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                    node(contractTests("Bar"), arrow = FORWARD) { node(finalBuild) }
                                }

                                currentState >= removeWrappers -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)

                                    node("TestFederationCoordinator") {
                                        node(allTests("Foo"), color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                        node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                        node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                    }
                                }

                                currentState >= manyGeneratorsDisappear -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    val foo = reusableNode("Foo", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }
                                    val bar = reusableNode("Bar", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }

                                    val generatorFoo = reusableNode("Generator (Foo)") {
                                        node(allTests("Foo"), color = palette.purple, arrow = FORWARD) {
                                            node(foo)
                                        }
                                    }

                                    val generatorBar = reusableNode("Generator (Bar)") {
                                        node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                        node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                    }
                                    node(generatorFoo)
                                    node(generatorBar)
                                }

                                currentState >= manyGeneratorsAppear -> buildTree {
                                    node("mvn generate") {
                                        node("Generator (Foo)")
                                        node("Generator (Bar)")
                                        node("Generator (Baz)")
                                        node("Generator (Qux)")
                                        node("Generator (...)")
                                    }
                                }

                                currentState >= optimizationAppears -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    val foo = reusableNode("Foo", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }
                                    val bar = reusableNode("Bar", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }

                                    val generatorFoo = reusableNode("Generator (Foo)") {
                                        node(allTests("Foo"), color = palette.purple, arrow = FORWARD) {
                                            node(foo)
                                        }
                                    }

                                    val generatorBar = reusableNode("Generator (Bar)") {
                                        node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                        node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                    }
                                    node("mvn generate") {
                                        node(generatorFoo)
                                        node(generatorBar)
                                    }
                                }

                                currentState >= perVariantGeneratorsAppear -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    val foo = reusableNode("Foo", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }
                                    val bar = reusableNode("Bar", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }

                                    node("Generator (Foo)") {
                                        node(allTests("Foo"), color = palette.purple, arrow = FORWARD) {
                                            node(foo)
                                        }
                                    }

                                    node("Generator (Bar)") {
                                        node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                        node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                    }
                                }
                                currentState >= generatorAppears -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    val foo = reusableNode("Foo", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }
                                    val bar = reusableNode("Bar", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }

                                    node("TestFederationCoordinator") {
                                        node(allTests("Foo"), color = palette.purple, arrow = FORWARD) {
                                            node(foo)
                                        }
                                        node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                        node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                            node(bar)
                                        }
                                    }
                                }
                                currentState >= variantsAppear -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    val foo = reusableNode("Foo", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }
                                    val bar = reusableNode("Bar", color = palette.turquoise, arrow = FORWARD) {
                                        node(finalBuild)
                                    }

                                    node(allTests("Foo"), color = palette.purple, arrow = FORWARD) {
                                        node(foo)
                                    }
                                    node(smokeTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                        node(bar)
                                    }
                                    node(contractTests("Bar"), color = palette.purple, arrow = FORWARD) {
                                        node(bar)
                                    }
                                }
                                currentState >= buildsRename -> buildTree {
                                    val finalBuild = reusableNode("Aggregate", arrow = FORWARD)
                                    node("Foo", color = palette.turquoise, arrow = FORWARD) { node(finalBuild) }
                                    node("Bar", color = palette.turquoise, arrow = FORWARD) { node(finalBuild) }
                                }
                                currentState >= dynamicModeUnfocuses -> buildTree {
                                    node("GeneratorBuild") {
                                        val finalBuild = reusableNode("FinalBuild", arrow = FORWARD)
                                        node("Foo", color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                        node("Bar", color = palette.purple, arrow = FORWARD) { node(finalBuild) }
                                    }
                                }
                                currentState >= dependenciesAppear -> buildTree {
                                    node("GeneratorBuild") {
                                        val finalBuild = reusableNode("FinalBuild", arrow = FORWARD)
                                        node("Foo", color = palette.orange, arrow = FORWARD) { node(finalBuild) }
                                        node("Bar", color = palette.orange, arrow = FORWARD) { node(finalBuild) }
                                    }
                                }
                                currentState >= staticModeUnfocuses -> buildTree {
                                    node("GeneratorBuild") {
                                        node("FinalBuild", arrow = FORWARD)
                                    }
                                }
                                else -> buildTree {
                                    node("GeneratorBuild", color = palette.orange) {
                                        node("FinalBuild", color = palette.orange, arrow = FORWARD)
                                    }
                                }
                            }

                            SlideFromBottomAnimatedVisibility(visible = { it >= treeAppears }) {
                                AnimatedHorizontalTree(tree) { node ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(width = if (palette.darkMode) 2.dp else 3.dp, color = node.color ?: LIGHT_PALETTE.turquoise, shape = RoundedCornerShape(8.dp))
                                            .background(palette.treeNodeBackground)
                                            .padding(8.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.animateContentSize(tween(durationMillis = 300, delayMillis = 300))
                                        ) {
                                            createChildTransition {
                                                when {
                                                    it in generatorBuildConfigExpands until generatorBuildConfigFolds && node.value == "GeneratorBuild" -> GENERATOR_BUILD_CONFIG[0].String()
                                                    it in finalBuildConfigExpands until finalBuildConfigFolds && node.value == "FinalBuild" -> FINAL_BUILD_CONFIG[0].String()
                                                    else -> JustName(node)
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
        }
    }
}

private val FINAL_BUILD_CONFIG = buildCodeSamples {
    """
    params {
        param("GENERATOR_BUILD_TYPE_ID", "GeneratorBuild")
    }
    """
        .trimIndent()
        .toCodeSample()
        .startWith { this }
}

private val GENERATOR_BUILD_CONFIG = buildCodeSamples {
    """
    steps {
        script {
            "mvn teamcity-configs:generate -Ddynamic=true"
        }
    }
    """
        .trimIndent()
        .toCodeSample()
        .startWith { this }
}

private val PROJECT_CONFIG = buildCodeSamples {
    val dynamicMode by tag()
    val staticMode by tag()

    """
    project {
        if (System.getProperty("dynamic").toBoolean()) {
            subProject {
                ${dynamicMode}buildType(Foo)
                buildType(Bar)${dynamicMode}
            }
        } else {
            ${staticMode}buildType(GeneratorBuild)
            buildType(FinalBuild)${staticMode}
        }
    }
    """
        .trimIndent()
        .toCodeSample()
        .startWith { this }
        .openPanel("tree")
        .then { focus(staticMode) }
        .then { unfocus() }
        .pass(3)
        .then { focus(dynamicMode) }
        .then { unfocus() }
        .hideIde()
        .pass(14)
        .closePanel("tree")
}