package dev.panuszewski.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.magic.toWords
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState
import dev.panuszewski.template.CodeSample
import dev.panuszewski.template.Foldable
import dev.panuszewski.template.MagicText
import dev.panuszewski.template.ScrollableMagicCodeSample
import dev.panuszewski.template.SlideFromLeftAnimatedVisibility
import dev.panuszewski.template.SlideFromRightAnimatedVisibility
import dev.panuszewski.template.buildCodeSamples
import dev.panuszewski.template.expand
import dev.panuszewski.template.fold
import dev.panuszewski.template.startWith
import dev.panuszewski.template.tag

private val STATE_COUNT: Int get() = 1 + BUILD_POM.size + CONSUMER_POM.size

fun StoryboardBuilder.Maven() = scene(
    stateCount = STATE_COUNT
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))
        ProvideTextStyle(MaterialTheme.typography.h4) {
            transition.createChildTransition {
                when (it.toState()) {
                    STATE_COUNT - 3, STATE_COUNT - 2 -> buildAnnotatedString {
                        append("Maven ")
                        withStyle(SpanStyle(MaterialTheme.colors.primary)) { append("3") }
                    }
                    STATE_COUNT - 1 -> buildAnnotatedString {
                        append("Maven ")
                        withStyle(SpanStyle(MaterialTheme.colors.secondary)) { append("4") }
                    }
                    else -> AnnotatedString("Maven")
                }
            }.MagicText()
        }
        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(-250.dp, Alignment.CenterHorizontally)) {

            val buildPomTransition = transition.createChildTransition { plan.getSample(BUILD_POM_SLOT, it.toState()) }
            val consumerPomTransition = transition.createChildTransition { plan.getSample(CONSUMER_POM_SLOT, it.toState()) }

            val buildPomTitleTransition = transition.createChildTransition {
                when {
                    it.toState() >= STATE_COUNT - 3 -> "Build POM"
                    else -> "POM"
                }
            }
            val consumerPomTitleTransition = transition.createChildTransition {
                when {
                    it.toState() >= STATE_COUNT - 3 -> "Consumer POM"
                    else -> "POM"
                }
            }

            val boxModifier = Modifier.width(500.dp).padding(8.dp)

            transition.SlideFromLeftAnimatedVisibility({ it.toState() >= 1 }) {
                Column(boxModifier) {
                    ProvideTextStyle(MaterialTheme.typography.h5) { buildPomTitleTransition.MagicText() }
                    Spacer(Modifier.height(16.dp))

                    ProvideTextStyle(MaterialTheme.typography.body2) {
                        buildPomTransition.ScrollableMagicCodeSample(
                            moveDurationMillis = 500,
                            fadeDurationMillis = 500,
                            split = { it.toWords() },
                        )
                    }
                }
            }

            transition.SlideFromRightAnimatedVisibility({ it.toState() >= BUILD_POM.size + 1 }) {
                Column(boxModifier.padding(start = 150.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.h5) { consumerPomTitleTransition.MagicText() }
                    Spacer(Modifier.height(16.dp))

                    consumerPomTransition.ScrollableMagicCodeSample(
                        moveDurationMillis = 500,
                        fadeDurationMillis = 500,
                        split = { it.splitByTags() },
                    )
                }
            }
        }
    }
}

private val CONSUMER_POM = buildCodeSamples {
    val properties by tag()
    val build by tag()

    val codeSample = """
        <project>
            <groupId>...</groupId>
            <artifactId>...</artifactId>
            <version>...</version>${properties}
            <properties>...</properties>${properties}
            <repositories>...</repositories>
            <dependencies>...</dependencies>${build}
            <build>...</build>${build}
        </project>
        """.trimIndent().toCodeSample(language = Language.Xml)

    codeSample
        .then { focus(properties, build, scroll = false) }
        .then { hide(properties, build).unfocus() }
}

private val BUILD_POM_YAML = buildCodeSamples {
    val properties by tag()
    val build by tag()

    val codeSample = """
        id: pl.allegro.tech.common:andamio-starter-core:1.0.0
        properties:
          kotlin.code.style: official
          kotlin.compiler.jvmTarget: 1.8
        repositories:
          - id: mavenCentral
            url: https://repo1.maven.org/maven2/
        dependencies:
          - org.springframework.boot:spring-boot-starter-core:3.5.4@compile
        build:
          plugins:
            - groupId: org.jetbrains.kotlin
              artifactId: kotlin-maven-plugin
              version: 2.2.0
          extensions:
            - groupId: org.apache.maven.extensions
              artifactId: maven-build-cache-extension
              version: 1.2.0
        """.trimIndent().toCodeSample(language = Language.Yaml)

    codeSample
        .then { focus(properties, build, scroll = false) }
        .then { hide(properties, build).unfocus() }
}

private val BUILD_POM = buildCodeSamples {
    val groupIdFolded by tag()
    val groupIdExpanded by tag()
    val artifactIdFolded by tag()
    val artifactIdExpanded by tag()
    val versionFolded by tag()
    val versionExpanded by tag()
    val propertiesFolded by tag()
    val propertiesExpanded by tag()
    val repositoriesFolded by tag()
    val repositoriesExpanded by tag()
    val dependenciesFolded by tag()
    val dependenciesExpanded by tag()
    val buildFolded by tag()
    val buildExpanded by tag()

    val focusableCoordinates by tag()
    val focusableProperties by tag()
    val focusableRepositories by tag()
    val focusableDependencies by tag()
    val focusableBuild by tag()

    val codeSample = """
        <project>${focusableCoordinates}
            <groupId>${groupIdFolded}...${groupIdFolded}${groupIdExpanded}pl.allegro.tech.common${groupIdExpanded}</groupId>
            <artifactId>${artifactIdFolded}...${artifactIdFolded}${artifactIdExpanded}andamio-starter-core${artifactIdExpanded}</artifactId>
            <version>${versionFolded}...${versionFolded}${versionExpanded}1.0.0${versionExpanded}</version>${focusableCoordinates}${focusableProperties}
            <properties>${propertiesFolded}...${propertiesFolded}${propertiesExpanded}
                <kotlin.code.style>official</kotlin.code.style>
                <kotlin.compiler.jvmTarget>1.8</kotlin.compiler.jvmTarget>
            ${propertiesExpanded}</properties>${focusableProperties}${focusableRepositories}
            <repositories>${repositoriesFolded}...${repositoriesFolded}${repositoriesExpanded}
                <repository>
                    <id>mavenCentral</id>
                    <url>https://repo1.maven.org/maven2/</url>
                </repository>
            ${repositoriesExpanded}</repositories>${focusableRepositories}${focusableDependencies}
            <dependencies>${dependenciesFolded}...${dependenciesFolded}${dependenciesExpanded}
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-core</artifactId>
                    <version>3.5.4</version>
                </dependency>
            ${dependenciesExpanded}</dependencies>${focusableDependencies}${focusableBuild}
            <build>${buildFolded}...${buildFolded}${buildExpanded}
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>2.2.0</version>
                    </plugin>
                <plugins>
                <extensions>
                    <extension>
                        <groupId>org.apache.maven.extensions</groupId>
                        <artifactId>maven-build-cache-extension</artifactId>
                        <version>1.2.0</version>
                    </extension>
                </extensions>
            ${buildExpanded}</build>${focusableBuild}
        </project>
        """.trimIndent().toCodeSample(language = Language.Xml)

    val all = Foldable(
        folded = listOf(groupIdFolded, artifactIdFolded, versionFolded, propertiesFolded, repositoriesFolded, dependenciesFolded, buildFolded),
        expanded = listOf(groupIdExpanded, artifactIdExpanded, versionExpanded, propertiesExpanded, repositoriesExpanded, dependenciesExpanded, buildExpanded)
    )
    val coordinates = Foldable(
        folded = listOf(groupIdFolded, artifactIdFolded, versionFolded), expanded = listOf(groupIdExpanded, artifactIdExpanded, versionExpanded)
    )

    val properties = Foldable(propertiesFolded, propertiesExpanded)
    val repositories = Foldable(repositoriesFolded, repositoriesExpanded)
    val dependencies = Foldable(dependenciesFolded, dependenciesExpanded)
    val build = Foldable(buildFolded, buildExpanded)

    codeSample
        .startWith { fold(all) }
        .then { expand(coordinates).focus(focusableCoordinates, scroll = false) }
        .then { fold(coordinates).expand(properties).focus(focusableProperties, scroll = false) }
        .then { fold(properties).expand(repositories).focus(focusableRepositories, scroll = false) }
        .then { fold(repositories).expand(dependencies).focus(focusableDependencies, scroll = false) }
        .then { fold(dependencies).expand(build).focus(focusableBuild, scroll = true) }
        .then { fold(build).unfocus() }
        .then { expand(all) }
}

class CodeSamplesPlan {
    private val slots = mutableSetOf<String>()
    private val revelations = mutableListOf<Pair<String, List<CodeSample>>>()
    private val samples = mutableMapOf<String, MutableList<CodeSample>>()

    /**
     * Register a slot that will hold code samples.
     * Only one slot at a time will be revealing subsequent code samples
     */
    fun registerSlot(slotName: String) {
        slots.add(slotName)
        samples[slotName] = mutableListOf()
    }

    /**
     * Schedule code samples to be revealed in the slot.
     * The order of invoking this method matters. For example:
     * ```kotlin
     * plan.thenRevealInSlot("A", firstSamplesA)
     * plan.thenRevealInSlot("B", firstSamplesB)
     * plan.thenRevealInSlot("A", secondSamplesA)
     * ```
     * In the above example:
     * - first slot A is activated, and it will reveal all samples from the `firstSamplesA` list
     * - then slot B is activated, and it will reveal all samples from the `firstSamplesB` list
     * - then slot A is activated again, and it will reveal all samples from the `secondSamplesA` list
     */
    fun thenRevealInSlot(slotName: String, codeSamples: List<CodeSample>) {
        require(slotName in slots) { "Slot $slotName not registered" }
        revelations.add(slotName to codeSamples)
        samples[slotName]!!.addAll(codeSamples)
    }

    /**
     * - for active slot, calling this method with subsequent states will return subsequent code samples.
     * - for inactive slot, calling this method with subsequent states will return the same code sample.
     */
    fun getSample(slotName: String, state: Int): CodeSample {
        require(slotName in slots) { "Slot $slotName not registered" }
        require(samples[slotName]!!.isNotEmpty()) { "No samples in slot $slotName" }

        // Find which revelation is active for the current state
        var remainingState = state
        var activeRevelationIndex = 0

        // Find the active revelation based on the state
        for (i in revelations.indices) {
            val (_, samples) = revelations[i]
            if (remainingState < samples.size) {
                activeRevelationIndex = i
                break
            }
            remainingState -= samples.size
        }

        // Get the active slot
        val (activeSlot, _) = revelations[activeRevelationIndex]

        // If the requested slot is the active slot, return the appropriate sample
        if (slotName == activeSlot) {
            // Find the index of the sample in the slot's samples list
            var sampleIndex = 0

            // Count samples in previous revelations for this slot
            for (i in 0 until activeRevelationIndex) {
                val (slot, slotSamples) = revelations[i]
                if (slot == slotName) {
                    sampleIndex += slotSamples.size
                }
            }

            // Add the remaining state to get the final index
            sampleIndex += remainingState

            return samples[slotName]!![sampleIndex]
        } else {
            // For inactive slots, find the last revealed sample before the current state
            var lastRevealedIndex = -1
            var samplesProcessed = 0

            for (i in revelations.indices) {
                val (slot, slotSamples) = revelations[i]

                // If we've processed more samples than the current state, we're done
                if (samplesProcessed > state) {
                    break
                }

                // If this revelation is for the requested slot, update the last revealed index
                if (slot == slotName) {
                    // If this revelation would be partially revealed, calculate the exact index
                    if (samplesProcessed + slotSamples.size > state) {
                        val partialIndex = state - samplesProcessed
                        lastRevealedIndex = lastRevealedIndex + partialIndex + 1
                    } else {
                        // Otherwise, all samples in this revelation are revealed
                        lastRevealedIndex += slotSamples.size
                    }
                }

                samplesProcessed += slotSamples.size
            }

            // If no samples have been revealed yet, return the first sample
            return if (lastRevealedIndex <= 0) {
                samples[slotName]!!.first()
            } else {
                // Otherwise, return the last revealed sample
                samples[slotName]!![lastRevealedIndex.coerceAtMost(samples[slotName]!!.size - 1)]
            }
        }
    }
}

const val BUILD_POM_SLOT = "BUILD_POM"
const val CONSUMER_POM_SLOT = "CONSUMER_POM"

val plan = CodeSamplesPlan().apply {
    registerSlot(BUILD_POM_SLOT)
    registerSlot(CONSUMER_POM_SLOT)

    thenRevealInSlot(BUILD_POM_SLOT, BUILD_POM)
    thenRevealInSlot(CONSUMER_POM_SLOT, CONSUMER_POM)
    thenRevealInSlot(BUILD_POM_SLOT, BUILD_POM_YAML)
}

